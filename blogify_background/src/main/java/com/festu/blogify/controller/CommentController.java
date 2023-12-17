package com.festu.blogify.controller;


import com.festu.blogify.pojo.Comment;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.Type;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    OperationService operationService;

    @Autowired
    UserService userService;
    @GetMapping("")
    public Result loadAllComments(){
        return ResultFactory.buildSuccessResult(commentService.selectAllAndFullComments());
    }

    @GetMapping("/{id}")
    public Result findComment(@PathVariable("id") int id){
        return ResultFactory.buildSuccessResult(commentService.selectFullCommentById(id));
    }

    @PostMapping("")
    public Result uploadComment(@RequestBody Comment comment){
        operationService.saveOperationLog("用户发表评论: ID=" + comment.getId(), OperationLog.CREATE);
        if(commentService.checkComment(comment)){
            noticeService.releaseNotice(comment.getUserId(), "您已成功发表一篇评论");
            comment.setCheckState(1);
        } else {
            noticeService.releaseNotice(comment.getUserId(), "您刚发表的评论等待审核中");
            comment.setCheckState(0);
        }
        userService.updateActiveTimeForSubject();
        return commentService.addComment(comment);
    }

    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除评论: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return commentService.deleteCommentById(id);
    }

    @PutMapping("/{id}")
    public Result updateComment(@PathVariable("id") int id, @RequestBody Comment comment){
        comment.setId(id);
        if(comment.getCheckState() == 1) {
            operationService.saveOperationLog("管理员审核通过了评论: ID=" + id, OperationLog.UPDATE);
            noticeService.releaseNotice(comment.getUserId(), "您的评论已通过审核");
        } else {
            operationService.saveOperationLog("管理员审核驳回了评论: ID=" + id, OperationLog.UPDATE);
            noticeService.releaseNotice(comment.getUserId(), "您的评论已被驳回");
        }
        userService.updateActiveTimeForSubject();
        return commentService.updateComment(comment);
    }

    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<Comment> comments){
        List<Integer> ids = comments.stream().map(Comment::getId).toList();

        StringBuilder notice = new StringBuilder("管理员删除了多条评论: ID=[");
        for(int i=0; i<comments.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(comments.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);

        userService.updateActiveTimeForSubject();

        return commentService.deleteBatchByIds(ids);
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(commentService.selectByKeyword((String) req.get("keyword")));
    }

}
