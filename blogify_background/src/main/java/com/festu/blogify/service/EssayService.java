package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.*;
import com.festu.blogify.pojo.*;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EssayService {
    @Autowired
    EssayTagMapper essayTagMapper;
    @Autowired
    EssayMapper essayMapper;
    @Autowired
    TagsMapper tagsMapper;
    @Autowired
    TypesMapper typesMapper;
    @Autowired
    UsersMapper usersMapper;

    public Long getEssayCount(){ // 文章数量
        return essayMapper.selectCount(null);
    }

    public Long getEssayPraiseCount(){
        List<Essay> essays = essayMapper.selectList(null);
        Long count = 0L;
        for(Essay essay:essays){
            count += essay.getPraise();
        }
        return count;
    }

    public Long getEssayViewCount(){
        List<Essay> essays = essayMapper.selectList(null);
        Long count = 0L;
        for(Essay essay:essays){
            count += essay.getViews();
        }
        return count;
    }

    public Result praiseEssay(int id) {
        Essay essay = essayMapper.selectById(id);
        if(essay == null) {
            return ResultFactory.buildFailResult("博客不存在!");
        }
        essay.setPraise(essay.getPraise() + 1);
        try{
            essayMapper.updateById(essay);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(essay);
    }

    public Result viewEssay(int id) {
        Essay essay = essayMapper.selectById(id);
        if(essay == null) {
            return ResultFactory.buildFailResult("博客不存在!");
        }
        essay.setViews(essay.getViews() + 1);
        try{
            essayMapper.updateById(essay);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(essay);
    }
    public List<Essay> selectAllEssays(){
        return essayMapper.selectList(null);
    }

    public List<Essay> selectFullAndALlEssays(){
        return essayMapper.selectFullEssays();
    }

    public Essay selectEssayById(int id){
        return essayMapper.selectById(id);
    }

    public Essay selectFullEssayById(int id){
        return essayMapper.selectFullEssayById(id);
    }

    public Result selectEssayByTypeAndTag(String type, String tags){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        if(type != null){
            Map<String, Object> queryTypeId = new HashMap<>();
            queryTypeId.put("name", type);
            List<Type> types = typesMapper.selectByMap(queryTypeId);
            if(types.isEmpty()){
                return ResultFactory.buildFailResult("类别不存在");
            }
            essayQueryWrapper.eq("type_id", types.get(0).getId());
        }
        if(tags != null){   // tag 查询逻辑其实是错的，多个 tag 是与的关系，但是实际使用只考虑单 tag 查询
            List<String> tagNames = Arrays.asList(tags.split(","));
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.in("name", tagNames);
            List<Tag> tagList = tagsMapper.selectList(tagQueryWrapper);
            if(tagList.isEmpty()){
                return ResultFactory.buildFailResult("标签不存在");
            }
            List<Integer> tagIdList = tagList.stream().map( Tag::getId ).toList();
            QueryWrapper<EssayTagMapping> essayTagMappingQueryWrapper = new QueryWrapper<>();
            essayTagMappingQueryWrapper.in("tag_id", tagIdList);
            List<EssayTagMapping> essayTagMappingList = essayTagMapper.selectList(essayTagMappingQueryWrapper);
            if(essayTagMappingList.isEmpty()){
                return ResultFactory.buildFailResult("不存在符合条件的博文");
            }
            List<Integer> essayIdList = essayTagMappingList.stream().map( EssayTagMapping::getEssayId ).distinct().toList();
            essayQueryWrapper.in("id", essayIdList);
        }
        List<Essay> essays = essayMapper.selectList(essayQueryWrapper);
        if(essays.isEmpty()){
            return ResultFactory.buildFailResult("不存在符合条件的博文");
        }
        List<Integer> essayIds = essays.stream().map(Essay::getId).toList();
        return ResultFactory.buildSuccessResult(essayMapper.selectFullEssaysByIds(essayIds));
    }
    public Result selectDisplayEssayByTypeAndTag(String type, String tags){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        if(type != null){
            Map<String, Object> queryTypeId = new HashMap<>();
            queryTypeId.put("name", type);
            List<Type> types = typesMapper.selectByMap(queryTypeId);
            if(types.isEmpty()){
                return ResultFactory.buildFailResult("类别不存在");
            }
            essayQueryWrapper.eq("type_id", types.get(0).getId());
        }
        if(tags != null){   // tag 查询逻辑其实是错的，多个 tag 是与的关系，但是实际使用只考虑单 tag 查询
            List<String> tagNames = Arrays.asList(tags.split(","));
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.in("name", tagNames);
            List<Tag> tagList = tagsMapper.selectList(tagQueryWrapper);
            if(tagList.isEmpty()){
                return ResultFactory.buildFailResult("标签不存在");
            }
            List<Integer> tagIdList = tagList.stream().map( Tag::getId ).toList();
            QueryWrapper<EssayTagMapping> essayTagMappingQueryWrapper = new QueryWrapper<>();
            essayTagMappingQueryWrapper.in("tag_id", tagIdList);
            List<EssayTagMapping> essayTagMappingList = essayTagMapper.selectList(essayTagMappingQueryWrapper);
            if(essayTagMappingList.isEmpty()){
                return ResultFactory.buildFailResult("不存在符合条件的博文");
            }
            List<Integer> essayIdList = essayTagMappingList.stream().map( EssayTagMapping::getEssayId ).distinct().toList();
            essayQueryWrapper.in("id", essayIdList);
        }
        essayQueryWrapper.eq("is_public", 1);
        List<Essay> essays = essayMapper.selectList(essayQueryWrapper);
        if(essays.isEmpty()){
            return ResultFactory.buildFailResult("不存在符合条件的博文");
        }
        List<Integer> essayIds = essays.stream().map(Essay::getId).toList();
        return ResultFactory.buildSuccessResult(essayMapper.selectFullDisplayEssaysByIds(essayIds));
    }

    public Result insertEssay(Essay essay){
        Date now = new Date();
        essay.setCreateTime(now);
        essay.setUpdateTime(now);
        essay.setViews(0);
        essay.setPraise(0);
        try{
            essayMapper.insert(essay);
        } catch(Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(essay);
    }

    public Result updateEssay(Essay essay){
        essay.setUpdateTime(new Date());
        try{
            essayMapper.updateById(essay);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("博文更新成功");
    }

    public Result deleteEssayById(int id){
        try{
            essayMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult("博文删除失败");
        }
        return ResultFactory.buildSuccessResult("博文删除成功");
    }

    public Result getEssayViewCountByMonth(){
        QueryWrapper<Essay> viewCountByMonth = new QueryWrapper<>();
        viewCountByMonth.select("date_format(create_time, '%Y-%m') as date_month, sum(views) as count")
                .groupBy("date_month").orderByAsc("date_month");
        List<Map<String, Object>> viewCountByMonthMap = essayMapper.selectMaps(viewCountByMonth);
        return ResultFactory.buildSuccessResult(viewCountByMonthMap);
    }

    public Result getEssayCountByMonth(){
        QueryWrapper<Essay> countByMonth = new QueryWrapper<>();
        countByMonth.select("date_format(create_time, '%Y-%m') as date_month, count(views) as count")
                .groupBy("date_month").orderByAsc("date_month");
        List<Map<String, Object>> countByMonthMap = essayMapper.selectMaps(countByMonth);
        return ResultFactory.buildSuccessResult(countByMonthMap);
    }

    public Result getEssayPraiseCountByMonth(){
        QueryWrapper<Essay> praiseCountByMonth = new QueryWrapper<>();
        praiseCountByMonth.select("date_format(create_time, '%Y-%m') as date_month, sum(praise) as count")
                .groupBy("date_month").orderByAsc("date_month");
        List<Map<String, Object>> praiseCountByMonthMap = essayMapper.selectMaps(praiseCountByMonth);
        return ResultFactory.buildSuccessResult(praiseCountByMonthMap);
    }



    public List<Essay> selectDisplayByKeyword(String keyword){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.like("name", keyword);
        List<Integer> typeIds = typesMapper.selectList(typeQueryWrapper).stream().map(Type::getId).toList();
        if(!typeIds.isEmpty()){
            essayQueryWrapper.in("type_id", typeIds).or();
        }
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.like("name", keyword);
        List<Integer> tagIds = tagsMapper.selectList(tagQueryWrapper).stream().map(Tag::getId).toList();
        if(!tagIds.isEmpty()){
            QueryWrapper<EssayTagMapping> essayTagMappingQueryWrapper = new QueryWrapper<>();
            essayTagMappingQueryWrapper.in("tag_id", tagIds);
            List<Integer> essayIds = essayTagMapper.selectList(essayTagMappingQueryWrapper).stream().map(EssayTagMapping::getEssayId).toList();
            if(!essayIds.isEmpty()){
                essayQueryWrapper.in("id", essayIds).or();
            }
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username", keyword).or();
        userQueryWrapper.like("nickname", keyword);
        List<Integer> userIds = usersMapper.selectList(userQueryWrapper).stream().map(User::getId).toList();
        if(!userIds.isEmpty()){
            essayQueryWrapper.in("user_id", userIds).or();
        }
        essayQueryWrapper.like("title", keyword).or();
        essayQueryWrapper.like("content", keyword).or();
        essayQueryWrapper.like("abstractions", keyword);
        essayQueryWrapper.eq("is_public", 1);   // display 要求
        List<Integer> essayIds = essayMapper.selectList(essayQueryWrapper).stream().map(Essay::getId).toList();
        if(essayIds.isEmpty()){
            return null;
        }
        return essayMapper.selectFullDisplayEssaysByIds(essayIds);
    }

    public List<Essay> selectByKeyword(String keyword){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.like("name", keyword);
        List<Integer> typeIds = typesMapper.selectList(typeQueryWrapper).stream().map(Type::getId).toList();
        if(!typeIds.isEmpty()){
            essayQueryWrapper.in("type_id", typeIds).or();
        }
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.like("name", keyword);
        List<Integer> tagIds = tagsMapper.selectList(tagQueryWrapper).stream().map(Tag::getId).toList();
        if(!tagIds.isEmpty()){
            QueryWrapper<EssayTagMapping> essayTagMappingQueryWrapper = new QueryWrapper<>();
            essayTagMappingQueryWrapper.in("tag_id", tagIds);
            List<Integer> essayIds = essayTagMapper.selectList(essayTagMappingQueryWrapper).stream().map(EssayTagMapping::getEssayId).toList();
            if(!essayIds.isEmpty()){
                essayQueryWrapper.in("id", essayIds).or();
            }
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username", keyword).or();
        userQueryWrapper.like("nickname", keyword);
        List<Integer> userIds = usersMapper.selectList(userQueryWrapper).stream().map(User::getId).toList();
        if(!userIds.isEmpty()){
            essayQueryWrapper.in("user_id", userIds).or();
        }
        essayQueryWrapper.like("title", keyword).or();
        essayQueryWrapper.like("content", keyword).or();
        essayQueryWrapper.like("abstractions", keyword);
        List<Integer> essayIds = essayMapper.selectList(essayQueryWrapper).stream().map(Essay::getId).toList();
        if(essayIds.isEmpty()){
           return null;
        }
        return essayMapper.selectFullEssaysByIds(essayIds);
    }

    public Result deletBatchByIds(List<Integer> ids){
        try{
            essayMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

    public Result updateEssayTagsMapping(Essay essay){
        List<EssayTagMapping> essayTagMappings = essay.getEssayTagMappings();
        QueryWrapper<EssayTagMapping> essayTagMappingQueryWrapper = new QueryWrapper<>();
        essayTagMappingQueryWrapper.eq("essay_id", essay.getId());
        try{
            essayTagMapper.delete(essayTagMappingQueryWrapper);
            for(EssayTagMapping essayTagMapping:essayTagMappings){
                essayTagMapping.setId(0);
                essayTagMapper.insert(essayTagMapping);
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(essayTagMappings);
    }

    public List<Essay> loadDisplayEssays(){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        essayQueryWrapper.eq("is_public", 1);
        essayQueryWrapper.orderByDesc("update_time");
        List<Integer> essayIds = essayMapper.selectList(essayQueryWrapper).stream().map(Essay::getId).toList();
        return essayMapper.selectFullDisplayEssaysByIds(essayIds);
    }

    public Essay loadDisplayEssayById(int id){
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<>();
        essayQueryWrapper.eq("is_public", 1);
        essayQueryWrapper.eq("id", id);
        List<Integer> essayIds = essayMapper.selectList(essayQueryWrapper).stream().map(Essay::getId).toList();
        if(essayIds.isEmpty()){
            return null;
        }
        return essayMapper.selectFullDisplayEssayById(essayIds.get(0));
    }

}
