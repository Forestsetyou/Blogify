package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.OperationLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OperationLogsMapper extends BaseMapper<OperationLog> {
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById"))
    })
    @Select("SELECT * FROM operation_logs")
    List<OperationLog> selectFullLogs();

    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById"))
    })
    @Select("<script>" +
            "SELECT * FROM operation_logs WHERE id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<OperationLog> selectFullLogsByIds(@Param("ids")List<Integer> ids);
}
