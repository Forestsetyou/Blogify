package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.EssayTagMapping;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EssayTagMapper extends BaseMapper<EssayTagMapping> {
    @Results({
            @Result(column = "tag_id", property = "tagId"),
            @Result(column = "tag_id", property = "tag",
                    one = @One(select = "com.festu.blogify.mapper.TagsMapper.selectById"))
    })
    @Select("SELECT * FROM essay_tag_mapping where essay_id = #{id}")
    public EssayTagMapping selectFullEssayTagMappingsByEssayId(int id);

    @Results({
            @Result(column = "essay_id", property = "essayId"),
            @Result(column = "essay_id", property = "essay",
                    one = @One(select = "com.festu.blogify.mapper.EssayMapper.selectById"))
    })
    @Select("SELECT * FROM essay_tag_mapping where tag_id = #{id}")
    public EssayTagMapping selectFullEssayTagMappingsByTagId(int id);

    @Results({
            @Result(column = "tag_id", property = "tagId"),
            @Result(column = "tag_id", property = "tag",
                    one = @One(select = "com.festu.blogify.mapper.TagsMapper.selectById"))
    })
    @Select("SELECT * FROM essay_tag_mapping")
    public List<EssayTagMapping> selectFullAndAllTagMappings();
}
