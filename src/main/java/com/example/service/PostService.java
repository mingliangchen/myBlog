package com.example.service;

import com.example.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mingliang.chen@hand-china.com
 * @since 2019-04-23
 */
public interface PostService extends IService<Post> {

    Post get(long id);

    @CacheEvict(allEntries = true)
    boolean update(Post post);

    boolean delete(long id);

    void initIndexWeekRank();

    void zUnionAndStoreLast7DaysForLastWeekRank();

    void incrZsetValueAndUnionForLastWeekRank(Long postId);
}
