package com.festu.blogify.controller;

import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.CommentService;
import com.festu.blogify.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {
    @Autowired
    EssayService essayService;

    @Autowired
    CommentService commentService;

    @GetMapping("/statistics")
    public Result getStatistics(@RequestParam("q") String query){
        Map<String, Object> map = new HashMap<>();
        if(query.equals("all")){
            map.put("essays", essayService.getEssayCount());
            map.put("appreciation", essayService.getEssayPraiseCount());
            map.put("views", essayService.getEssayViewCount());
            map.put("comment", commentService.getCommentCount());
        } else if(query.equals("month")){
            map.put("essays", essayService.getEssayCountByMonth().getData());
            map.put("appreciation", essayService.getEssayPraiseCountByMonth().getData());
            map.put("views", essayService.getEssayViewCountByMonth().getData());
            map.put("comment", commentService.getEssayCommentCountByMonth().getData());
        }
        return ResultFactory.buildSuccessResult(map);
    }
}
