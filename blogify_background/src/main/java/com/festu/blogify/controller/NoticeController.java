package com.festu.blogify.controller;


import com.festu.blogify.pojo.NoticeMapping;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.NoticeService;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;
    @Autowired
    OperationService operationService;
    @Autowired
    UserService userService;

    @GetMapping("")
    public Result loadNotice(){
        return ResultFactory.buildSuccessResult(noticeService.selectAllAndFullNotices());
    }


    @PostMapping("")
    public Result createNotice(@RequestBody NoticeMapping noticeMapping){
        userService.updateActiveTimeForSubject();
        return noticeService.addNotice(noticeMapping);
    }

    @GetMapping("/{id}")
    public Result selectNoticeById(@PathVariable("id") int id){
        return ResultFactory.buildSuccessResult(noticeService.selectFullNoticeById(id));
    }

    @DeleteMapping("/{id}")
    public Result deleteBatchByIds(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除了一条通知: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return noticeService.deleteNoticeById(id);
    }

    @PostMapping("/batch/delete")
    public Result deleteBatchByIds(@RequestBody List<NoticeMapping> noticeMappings){

        StringBuilder notice = new StringBuilder("管理员删除了多条通知: ID=[");
        for(int i=0; i<noticeMappings.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(noticeMappings.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);

        List<Integer> ids = noticeMappings.stream().map(NoticeMapping::getId).toList();
        userService.updateActiveTimeForSubject();
        return noticeService.deleteBatchByIds(ids);
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(noticeService.selectByKeyword((String) req.get("keyword")));
    }
}
