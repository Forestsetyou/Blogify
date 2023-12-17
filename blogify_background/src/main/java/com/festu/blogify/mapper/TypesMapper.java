package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TypesMapper extends BaseMapper<Type> {
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essays",
                    many=@Many(select = "com.festu.blogify.mapper.EssayMapper.selectByTypeId"))
    })
    @Select("SELECT * FROM types")
    public List<Type> selectAllAndFullTypes();

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essays",
                    many=@Many(select = "com.festu.blogify.mapper.EssayMapper.selectByTypeId"))
    })
    @Select("SELECT * FROM types where id = #{id}")
    public Type selectFullTypeById(int id);

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "essays",
                    many=@Many(select = "com.festu.blogify.mapper.EssayMapper.selectByTypeId"))
    })
    @Select("<script>" +
            "SELECT * FROM types WHERE id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}</foreach></script>")
    public List<Type> selectFullTypesByIds(List<Integer> ids);
}
