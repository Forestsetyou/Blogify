package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UsersMapper extends BaseMapper<User> {

    @Results({
            @Result(column = "role_id", property = "roleId"),
            @Result(column = "role_id", property = "role",
                    one = @One(select = "com.festu.blogify.mapper.RolesMapper.selectById")),
            @Result(column = "avatar_pic_id", property = "avatarPicId"),
            @Result(column = "avatar_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById"))
    })
    @Select("SELECT * FROM users")

    List<User> selectFullUsers();
    @Results({
            @Result(column = "role_id", property = "roleId"),
            @Result(column = "role_id", property = "role",
                    one = @One(select = "com.festu.blogify.mapper.RolesMapper.selectById")),
            @Result(column = "avatar_pic_id", property = "avatarPicId"),
            @Result(column = "avatar_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById"))
    })
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectFullUsersById(int id);


    @Results({
            @Result(column = "avatar_pic_id", property = "avatarPicId"),
            @Result(column = "avatar_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById"))
    })
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectFullDisplayUsersById(int id);

    @Results({
            @Result(column = "role_id", property = "roleId"),
            @Result(column = "role_id", property = "role",
                    one = @One(select = "com.festu.blogify.mapper.RolesMapper.selectById")),
            @Result(column = "avatar_pic_id", property = "avatarPicId"),
            @Result(column = "avatar_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById"))
    })
    @Select("<script>" +
            "SELECT * FROM users WHERE id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<User> selectFullUsersByIds(@Param("ids")List<Integer> ids);

}
