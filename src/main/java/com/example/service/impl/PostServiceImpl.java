package com.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.mapper.PostMapper;
import com.example.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mingliang.chen@hand-china.com
 * @since 2019-04-23
 */
@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    RedisUtil redisUtil;

    @Cacheable(cacheNames = "cache_post", key = "'post_' + #id")
    @Override
    public Post get(long id) {
        log.info("-----------> 查库了");
        Post post = this.getById(id);
        Assert.notNull(post, "该博客不存在~");
        return post;
    }

    @Override
    public boolean update(Post post) {
        log.info("-----------> 删除缓存了·");
        return updateById(post);
    }

    @Override
    public boolean delete(long id) {
        return removeById(id);
    }

    @Override
    public void initIndexWeekRank() {
        //缓存最近7天的文章评论数量
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .select("id, title, user_id, comment_count, view_count, created"));

        for (Post post : last7DayPosts) {
            String key = "day_rank:" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);

            //设置有效期
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());
            //设置有效期
            redisUtil.expire(key, expireTime);

            //缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);
        }
        this.zUnionAndStoreLast7DaysForLastWeekRank();

    }

    /**
     * hash结构缓存文章标题和id
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {

        boolean isExist = redisUtil.hasKey("rank_post_" + post.getId());
        if(!isExist) {
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章基本信息（hash结构）
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:comment_count", post.getCommentCount(), expireTime);
        }
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    @Override
    public void zUnionAndStoreLast7DaysForLastWeekRank() {
        String prifix = "day_rank:";

        List<String> keys  = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        for(int i = -6 ; i < 0; i++) {
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            keys.add(prifix + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        }

        redisUtil.zUnionAndStore(key, keys, "last_week_rank");
    }

    /**
     * 给set里的文章评论加1，并且重新union7天的评论数量
     * @param postId
     */
    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId) {
        String dayRank = "day_rank:" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        //文章阅读加一
        redisUtil.zIncrementScore(dayRank, postId, 1);

        this.hashCachePostIdAndTitle(this.getById(postId));

        //重新union最近7天
        this.zUnionAndStoreLast7DaysForLastWeekRank();
    }
}
