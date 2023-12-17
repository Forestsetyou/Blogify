package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.CommentMapper;
import com.festu.blogify.mapper.UsersMapper;
import com.festu.blogify.pojo.*;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UsersMapper usersMapper;

    public Long getCommentCount(){
        return commentMapper.selectCount(null);
    }

    public List<Comment> selectAllComments(){
        return commentMapper.selectList(null);
    }

    public List<Comment> selectAllAndFullComments(){
        return commentMapper.selectFullComments();
    }

    public List<Comment> selectAllCheckedComments(){
        Map<String, Object> query = new HashMap<>();
        query.put("is_checked", true);
        return commentMapper.selectByMap(query);
    }

    public Comment selectCommentById(int id){
        return commentMapper.selectById(id);
    }

    public Comment selectFullCommentById(int id){
        return commentMapper.selectFullCommentById(id);
    }

    public Boolean checkComment(Comment comment){
        List<String> black_list = new ArrayList<>(Arrays.asList(
                "微信", "QQ", "qq", "联系", "党", "主席", "+", "加"
        ));
        for(String words:black_list){
            if(comment.getContent().contains(words)){
                return false;
            }
        }
        return true;
    }

    public Result addComment(Comment comment){
        if(checkComment(comment)){
            comment.setCheckState(1);
        } else {
            comment.setCheckState(0);
        }
        comment.setCreateTime(new Date());
        comment.setUserId(((User)SecurityUtils.getSubject().getPrincipal()).getId());
        try{
            commentMapper.insert(comment);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(comment);
    }

    public Result deleteCommentById(int id){
        try{
            commentMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("评论删除成功");
    }

    public Result updateComment(Comment comment){
        try{
            commentMapper.updateById(comment);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("评论更新成功");

    }

    public Result getEssayCommentCountByMonth(){
        QueryWrapper<Comment> commentCountByMonth = new QueryWrapper<>();
        commentCountByMonth.select("date_format(create_time, '%Y-%m') as date_month, count(id) as count")
                .groupBy("date_month").orderByAsc("date_month");
        List<Map<String, Object>> commentCountByMonthMap = commentMapper.selectMaps(commentCountByMonth);
        return ResultFactory.buildSuccessResult(commentCountByMonthMap);
    }


    public List<Comment> selectByKeyword(String keyword){
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username", keyword).or();
        userQueryWrapper.like("nickname", keyword);
        List<Integer> userIds = usersMapper.selectList(userQueryWrapper).stream().map(User::getId).toList();
        if(!userIds.isEmpty()){
            commentQueryWrapper.in("user_id", userIds).or();
        }
        commentQueryWrapper.like("content", keyword);
        List<Integer> commentIds = commentMapper.selectList(commentQueryWrapper).stream().map(Comment::getId).toList();
        if(commentIds.isEmpty()){
            return null;
        }
        return commentMapper.selectFullCommentsByIds(commentIds);
    }


    public Result deleteBatchByIds(List<Integer> ids){
        try{
            commentMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

    public List<Comment> loadDisplayCommentsByEssayId(int essayId){
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("parent_essay_id", essayId);
        commentQueryWrapper.eq("check_state", 1);   // 审核通过
        commentQueryWrapper.orderByDesc("create_time");
        List<Integer> commentIds = commentMapper.selectList(commentQueryWrapper).stream().map(Comment::getId).toList();
        if(commentIds.isEmpty()){
            return new ArrayList<Comment>();
        }
        return commentMapper.selectFullDisplayCommentsByIds(commentIds);
    }

}
