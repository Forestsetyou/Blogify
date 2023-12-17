package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.OperationLogsMapper;
import com.festu.blogify.mapper.UsersMapper;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationService {
    @Autowired
    OperationLogsMapper operationLogsMapper;

    @Autowired
    UsersMapper usersMapper;

    // Subject 在任何地方都可以合法获取，因此可以封装用户信息；注意日志记录只能放在有身份验证的接口中调用
    public Boolean saveOperationLog(String remarks, int type){
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationTime(new Date());
        operationLog.setUserId(((User)SecurityUtils.getSubject().getPrincipal()).getId());
        operationLog.setRemarks(remarks);
        operationLog.setType(type);
        try{
            operationLogsMapper.insert(operationLog);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public List<OperationLog> selectAllAndFullLogs(){
        return operationLogsMapper.selectFullLogs();
    }

    public List<OperationLog> selectLogByKeyword(String keyword){
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.like("LOWER(username)", keyword.toLowerCase());
        List<User> users = usersMapper.selectList(userQuery);
        List<Integer> userIds = users.stream().map(User::getId).toList();
        QueryWrapper<OperationLog> blurQuery = new QueryWrapper<>();
        if(!userIds.isEmpty()){ // 为空时传入 () 会使得 MySQL 语法出错
            blurQuery.in("user_id", userIds).or();
        }
        blurQuery.like("LOWER(remarks)", keyword.toLowerCase()).or();
        blurQuery.like("DATE_FORMAT(operation_time, '%y-%m-%d %h:%i:%s')", keyword);
        List<Integer> logIds = operationLogsMapper.selectList(blurQuery).stream().map(OperationLog::getId).toList();
        if(logIds.isEmpty()){
            return null;
        }
        return operationLogsMapper.selectFullLogsByIds(logIds);
    }

    public OperationLog selectLogById(int id){
        return operationLogsMapper.selectById(id);
    }

    public Result addLogs(OperationLog operationLog){
        try{
            operationLogsMapper.insert(operationLog);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(operationLog);
    }

    public Result deleteLogById(int id){
        try{
            operationLogsMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("记录删除成功");
    }

    public Result updateLog(OperationLog operationLog){
        try{
            operationLogsMapper.updateById(operationLog);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("记录更新成功");
    }

    public Result deletBatchByIds(List<Integer> ids){
        try{
            operationLogsMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
