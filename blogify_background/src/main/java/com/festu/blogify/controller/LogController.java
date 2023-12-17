package com.festu.blogify.controller;


import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/log")
public class LogController {
    @Autowired
    OperationService operationService;
    @Autowired
    UserService userService;
    @GetMapping("")
    public Result selectAllAndFullLogs(){
        return ResultFactory.buildSuccessResult(operationService.selectAllAndFullLogs());
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(operationService.selectLogByKeyword((String) req.get("keyword")));
    }

    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<OperationLog> operationLogs){

        StringBuilder notice = new StringBuilder("管理员删除了多条日志: ID=[");
        for(int i=0; i<operationLogs.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(operationLogs.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);

        List<Integer> ids = operationLogs.stream().map(OperationLog::getId).toList();
        userService.updateActiveTimeForSubject();
        return operationService.deletBatchByIds(ids);
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除了一条日志: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return operationService.deleteLogById(id);
    }
}
