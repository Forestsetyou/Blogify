package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.TypesMapper;
import com.festu.blogify.pojo.Type;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    TypesMapper typesMapper;

    public List<Type> selectAllTypes(){
        return typesMapper.selectList(null);
    }

    public List<Type> selectAllAndFullTypes(){
        return typesMapper.selectAllAndFullTypes();
    }

    public Type selectTypeById(int id){
        return typesMapper.selectById(id);
    }

    public Type selectFullTypeById(int id){
        return typesMapper.selectFullTypeById(id);
    }

    public Result addType(Type type){
        try{
            typesMapper.insert(type);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(type);
    }

    public Result deleteTypeById(int id){
        try{
            typesMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("分类删除成功");
    }

    public Result updateType(Type type){
        try{
            typesMapper.updateById(type);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("分类更新成功");
    }

    public List<Type> selectByKeyword(String keyword){
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.like("name", keyword);
        List<Integer> typeIds = typesMapper.selectList(typeQueryWrapper).stream().map(Type::getId).toList();
        if(typeIds.isEmpty()){
            return null;
        }
        return typesMapper.selectFullTypesByIds(typeIds);
    }

    public Result deleteBatchByIds(List<Integer> ids){
        try{
            typesMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
