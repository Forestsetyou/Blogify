package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.Essay;
import com.festu.blogify.pojo.EssayTagMapping;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EssayMapper extends BaseMapper<Essay> {
    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId")),
    })
    @Select("SELECT * FROM essays")
    List<Essay> selectFullEssays();

    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId"))
    })
    @Select("SELECT * FROM essays where id = #{id}")
    Essay selectFullEssayById(int id);

    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectFullDisplayUsersById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId"))
    })
    @Select("SELECT * FROM essays where id = #{id}")
    Essay selectFullDisplayEssayById(int id);


    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId"))
    })
    @Select("<script>" +
            "SELECT * FROM essays where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<Essay> selectFullEssaysByIds(List<Integer> ids);

    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectFullDisplayUsersById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId"))
    })
    @Select("<script>" +
            "SELECT * FROM essays where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    List<Essay> selectFullDisplayEssaysByIds(List<Integer> ids);


    @Results({
            @Result(column = "type_id", property = "typeId"),
            @Result(column = "type_id", property = "type",
                    one = @One(select = "com.festu.blogify.mapper.TypesMapper.selectById")),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.festu.blogify.mapper.UsersMapper.selectFullDisplayUsersById")),
            @Result(column = "background_pic_id", property = "backgroundPicId"),
            @Result(column = "background_pic_id", property = "blogFile",
                    one = @One(select = "com.festu.blogify.mapper.BlogFileMapper.selectById")),
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many = @Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByEssayId")),
    })
    @Select("SELECT * FROM essays")
    List<Essay> selectFullDisplayEssays();

    @Select("SELECT * FROM essays WHERE type_id = #{type_id}")
    List<Essay> selectByTypeId(int type_id);
}
