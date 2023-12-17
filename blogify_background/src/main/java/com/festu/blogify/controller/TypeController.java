package com.festu.blogify.controller;


import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.Type;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.TypeService;
import com.festu.blogify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/type")
public class TypeController {
    @Autowired
    TypeService typeService;
    @Autowired
    OperationService operationService;
    @Autowired
    UserService userService;

    @GetMapping("")
    public Result loadAllTypes(){
        return ResultFactory.buildSuccessResult(typeService.selectAllAndFullTypes());
    }

    @GetMapping("/{id}")
    public Result findType(@PathVariable("id") int id){
        return ResultFactory.buildSuccessResult(typeService.selectFullTypeById(id));
    }

    @PostMapping("")
    public Result uploadType(@RequestBody Type type){
        operationService.saveOperationLog("管理员新建了一个分类: Name=" + type.getName(), OperationLog.CREATE);
        userService.updateActiveTimeForSubject();
        return typeService.addType(type);
    }

    @DeleteMapping("/{id}")
    public Result deleteType(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除了一个分类: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return typeService.deleteTypeById(id);
    }

    @PutMapping("/{id}")
    public Result updateType(@PathVariable("id") int id, @RequestBody Type type){
        operationService.saveOperationLog("管理员更新了一个分类: ID=" + id, OperationLog.UPDATE);
        type.setId(id);
        userService.updateActiveTimeForSubject();
        return typeService.updateType(type);
    }

    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<Type> types){
        StringBuilder notice = new StringBuilder("管理员删除了多个分类: ID=[");
        for(int i=0; i<types.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(types.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);

        List<Integer> ids = types.stream().map(Type::getId).toList();
        userService.updateActiveTimeForSubject();
        return typeService.deleteBatchByIds(ids);
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(typeService.selectByKeyword((String) req.get("keyword")));
    }

}
