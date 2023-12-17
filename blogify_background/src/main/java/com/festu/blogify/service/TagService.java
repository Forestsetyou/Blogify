package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.TagsMapper;
import com.festu.blogify.pojo.Tag;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagsMapper tagsMapper;

    public List<Tag> selectAllTags(){
        return tagsMapper.selectList(null);
    }

    public List<Tag> selectAllAndFullTags(){
        return tagsMapper.selectAllAndFullTags();
    }

    public Tag selectTagById(int id){
        return tagsMapper.selectById(id);
    }

    public Tag selectFullTagById(int id){
        return tagsMapper.selectFullTagById(id);
    }


    public Result addTag(Tag tag){
        try{
            tagsMapper.insert(tag);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(tag);
    }

    public Result deleteTagById(int id){
        try{
            tagsMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("标签删除成功");
    }

    public Result updateTag(Tag tag){
        try{
            tagsMapper.updateById(tag);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("标签更新成功");

    }

    public List<Tag> selectByKeyword(String keyword){
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.like("name", keyword);
        List<Integer> tagIds = tagsMapper.selectList(tagQueryWrapper).stream().map(Tag::getId).toList();
        if(tagIds.isEmpty()){
            return null;
        }
        return tagsMapper.selectFullTagsByIds(tagIds);
    }

    public Result deleteBatchByIds(List<Integer> ids){
        try{
            tagsMapper.deleteBatchIds(ids);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

}
