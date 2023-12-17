package com.festu.blogify.controller;


import com.festu.blogify.pojo.Comment;
import com.festu.blogify.pojo.Essay;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.CommentService;
import com.festu.blogify.service.EssayService;
import com.festu.blogify.service.TagService;
import com.festu.blogify.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/display")
public class DisplayController {
    @Autowired
    EssayService essayService;

    @Autowired
    TagService tagService;

    @Autowired
    TypeService typeService;

    @Autowired
    CommentService commentService;

    @PostMapping("/essays")
    public Result findEssayByTypeAndTag(@RequestBody Map<String, Object> req){
        String type = (String) req.get("type");
        String tag = (String) req.get("tag");
        if((type == null || type.isEmpty()) && (tag == null || tag.isEmpty())){
            return ResultFactory.buildSuccessResult(essayService.loadDisplayEssays());
        } else {
            Result selectRes = essayService.selectDisplayEssayByTypeAndTag(type, tag);
            List<Essay> essays;
            if(selectRes.getCode() != 200){
                essays = new ArrayList<>();
            } else {
                essays = (List<Essay>) selectRes.getData();
            }
            return ResultFactory.buildSuccessResult(essays);
        }
    }

    @PostMapping("/essays/keyword")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(essayService.selectDisplayByKeyword((String) req.get("keyword")));
    }

    @GetMapping("/essays")
    public Result loadDisplayEssays(){  // 展示被公开的博文
        return ResultFactory.buildSuccessResult(essayService.loadDisplayEssays());
    }

    @GetMapping("/essays/praise/{id}")
    public Result praiseEssay(@PathVariable("id") int id){  // 点赞
        return essayService.praiseEssay(id);
    }

    @GetMapping("/essays/view/{id}")
    public Result viewEssay(@PathVariable("id") int id){  // 浏览量增加
        return essayService.viewEssay(id);
    }

    @GetMapping("/essays/{id}")
    public Result loadDisplayEssaysById(@PathVariable("id") int id){  // 展示被公开的博文
        Essay essay = essayService.loadDisplayEssayById(id);
        if(essay == null){
            return ResultFactory.buildFailResult("博文不存在或已被隐藏");
        }
        return ResultFactory.buildSuccessResult(essay);
    }

    @GetMapping("/tags")
    public Result loadFullAndAllTags(){
        return ResultFactory.buildSuccessResult(tagService.selectAllAndFullTags());
    }

    @GetMapping("/types")
    public Result loadFullAndAllTypes(){
        return ResultFactory.buildSuccessResult(typeService.selectAllAndFullTypes());
    }

    @GetMapping("/comments/{essayId}")
    public Result loadDisplayComments(@PathVariable("essayId") int essayId){
        return ResultFactory.buildSuccessResult(commentService.loadDisplayCommentsByEssayId(essayId));
    }
}
