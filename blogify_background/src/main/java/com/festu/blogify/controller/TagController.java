package com.festu.blogify.controller;


import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.Tag;
import com.festu.blogify.pojo.Type;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @Autowired
    EssayTagService essayTagService;

    @Autowired
    OperationService operationService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public Result loadFullAndAllTags(){
        return ResultFactory.buildSuccessResult(tagService.selectAllAndFullTags());
    }

    @GetMapping("/pure")
    public Result loadAllTags(){
        return ResultFactory.buildSuccessResult(tagService.selectAllTags());
    }

    @GetMapping("/{id}")
    public Result findTag(@PathVariable("id") int id){
        return ResultFactory.buildSuccessResult(tagService.selectFullTagById(id));
    }

    @PostMapping("")
    public Result uploadTag(@RequestBody Tag tag){
        operationService.saveOperationLog("管理员新建了一个标签: Name=" + tag.getName(), OperationLog.CREATE);
        userService.updateActiveTimeForSubject();
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public Result deleteTag(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除了一个标签: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return tagService.deleteTagById(id);
    }

    @PutMapping("/{id}")
    public Result updateTag(@PathVariable("id") int id, @RequestBody Tag tag){
        operationService.saveOperationLog("管理员更新了一个标签: ID=" + id, OperationLog.UPDATE);
        tag.setId(id);
        userService.updateActiveTimeForSubject();
        return tagService.updateTag(tag);
    }

    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<Tag> tags){
        StringBuilder notice = new StringBuilder("管理员删除了多个标签: ID=[");
        for(int i=0; i<tags.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(tags.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);

        List<Integer> ids = tags.stream().map(Tag::getId).toList();
        userService.updateActiveTimeForSubject();
        return tagService.deleteBatchByIds(ids);
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(tagService.selectByKeyword((String) req.get("keyword")));
    }

    @GetMapping("/mapping")
    public Result selectAllEssayTagMappingWithTag(){
        return ResultFactory.buildSuccessResult(essayTagService.selectFullAndAllTagMappings());
    }

}
