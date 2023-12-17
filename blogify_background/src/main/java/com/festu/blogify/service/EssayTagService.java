package com.festu.blogify.service;

import com.festu.blogify.mapper.EssayTagMapper;
import com.festu.blogify.mapper.TagsMapper;
import com.festu.blogify.pojo.EssayTagMapping;
import com.festu.blogify.pojo.Tag;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.List;

@Service
public class EssayTagService {
    @Autowired
    EssayTagMapper essayTagMapper;

    public List<EssayTagMapping> selectAllTagMappings(){
        return essayTagMapper.selectList(null);
    }

    public List<EssayTagMapping> selectFullAndAllTagMappings(){
        return essayTagMapper.selectFullAndAllTagMappings();
    }
    public EssayTagMapping selectTagMappingById(int id){
        return essayTagMapper.selectById(id);
    }

    public Result addTagMapping(EssayTagMapping essayTagMapping){
        try{
            essayTagMapper.insert(essayTagMapping);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("标签映射添加成功");
    }

    public Result addTagMappings(List<EssayTagMapping> essayTagMappings){
        try{
            for(EssayTagMapping essayTagMapping:essayTagMappings){
                essayTagMapper.insert(essayTagMapping);
            }
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(essayTagMappings);
    }

    public Result deleteTagMappingById(int id){
        try{
            essayTagMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("标签移除成功");
    }

}
