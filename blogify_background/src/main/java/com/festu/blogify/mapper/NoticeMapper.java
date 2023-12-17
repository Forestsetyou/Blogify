package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.NoticeMapping;
import com.festu.blogify.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NoticeMapper extends BaseMapper<NoticeMapping> {

    @Results({
            @Result(column = "notifier_id", property = "notifierId"),
            @Result(column = "notifier_id", property = "notifier",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "noticee_id", property = "noticeeId"),
            @Result(column = "noticee_id", property = "noticee",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById"))
    })
    @Select("SELECT * FROM notice_mapping")
    List<NoticeMapping> selectFullNotice();

    @Results({
            @Result(column = "notifier_id", property = "notifierId"),
            @Result(column = "notifier_id", property = "notifier",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "noticee_id", property = "noticeeId"),
            @Result(column = "noticee_id", property = "noticee",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById"))
    })
    @Select("SELECT * FROM notice_mapping WHERE id = #{id}")
    NoticeMapping selectFullNoticeById(int id);


    @Results({
            @Result(column = "notifier_id", property = "notifierId"),
            @Result(column = "notifier_id", property = "notifier",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "noticee_id", property = "noticeeId"),
            @Result(column = "noticee_id", property = "noticee",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById"))
    })
    @Select("<script>" +
            "SELECT * FROM notice_mapping WHERE id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<NoticeMapping> selectFullNoticeByIds(@Param("ids")List<Integer> ids);
}
