package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.Comment;
import com.festu.blogify.pojo.Essay;
import com.festu.blogify.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    @Results({
            @Result(column = "parent_essay_id", property = "parentEssayId"),
            @Result(column = "parent_essay_id", property = "essay",
                    one = @One(select = "com.festu.blogify.mapper.EssayMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "parent_comment_id", property = "parentCommentId"),
            @Result(column = "parent_comment_id", property = "comment",
                    many = @Many(select = "com.festu.blogify.mapper.CommentMapper.selectFullCommentById"))
    })
    @Select("SELECT * FROM comments")
    List<Comment> selectFullComments();

    @Results({
            @Result(column = "parent_essay_id", property = "parentEssayId"),
            @Result(column = "parent_essay_id", property = "essay",
                    one = @One(select = "com.festu.blogify.mapper.EssayMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "parent_comment_id", property = "parentCommentId"),
            @Result(column = "parent_comment_id", property = "comment",
                    many = @Many(select = "com.festu.blogify.mapper.CommentMapper.selectFullCommentById"))
    })
    @Select("SELECT * FROM comments where id = #{id}")
    Comment selectFullCommentById(int id);


    @Results({
            @Result(column = "parent_essay_id", property = "parentEssayId"),
            @Result(column = "parent_essay_id", property = "essay",
                    one = @One(select = "com.festu.blogify.mapper.EssayMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "parent_comment_id", property = "parentCommentId"),
            @Result(column = "parent_comment_id", property = "comment",
                    many = @Many(select = "com.festu.blogify.mapper.CommentMapper.selectFullCommentById"))
    })
    @Select("<script>" +
            "SELECT * FROM comments where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<Comment> selectFullCommentsByIds(List<Integer> ids);

    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectFullDisplayUsersById"))
    })
    @Select("<script>" +
            "SELECT * FROM comments where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<Comment> selectFullDisplayCommentsByIds(List<Integer> ids);
}
