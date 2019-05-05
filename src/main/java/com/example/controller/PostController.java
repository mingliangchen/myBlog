package com.example.controller;


import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.service.PostService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.controller.BaseController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mingliang.chen@hand-china.com
 * @since 2019-04-23
 */
@Controller
public class PostController extends BaseController {
    @Autowired
    PostService postService;

    @Autowired
    RedissonClient redissonClient;

    @ResponseBody
    @RequestMapping("/post/{id:\\d*}")
    public Object view(@PathVariable Long id) {
        Post post = postService.get(id);
        return post;
    }

    @ResponseBody
    @RequestMapping("/update/post/{id:\\d*}")
    public Object update(@PathVariable Long id) {
        Post post = new Post();
        post.setId(id);
        post.setTitle("cml1");
        postService.update(post);
        return post;
    }
    @ResponseBody
    @GetMapping("/post/hots")
    public Result hotPost() {

        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtil.getZSetRank("last_week_rank", 0, 6);

        List<Map<String, Object>> hotPosts = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {

            Map<String, Object> map = new HashMap<>();
            map.put("comment_count", typedTuple.getScore());
            map.put("id", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:id"));
            map.put("title", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:title"));

            hotPosts.add(map);
        }

        return Result.succ(hotPosts);
    }
}
