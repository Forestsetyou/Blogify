package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.Tag;
import com.festu.blogify.pojo.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TagsMapper extends BaseMapper<Tag> {

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many=@Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByTagId"))
    })
    @Select("SELECT * FROM tags")
    public List<Tag> selectAllAndFullTags();

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many=@Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByTagId"))
    })
    @Select("SELECT * FROM tags where id = #{id}")
    public Tag selectFullTagById(int id);

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essayTagMappings",
                    many=@Many(select = "com.festu.blogify.mapper.EssayTagMapper.selectFullEssayTagMappingsByTagId"))
    })
    @Select("<script>" +
            "SELECT * FROM tags WHERE id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    public List<Tag> selectFullTagsByIds(List<Integer> ids);
}
