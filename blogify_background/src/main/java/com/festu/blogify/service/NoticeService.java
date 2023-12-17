package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.NoticeMapper;
import com.festu.blogify.mapper.TagsMapper;
import com.festu.blogify.mapper.UsersMapper;
import com.festu.blogify.pojo.NoticeMapping;
import com.festu.blogify.pojo.Role;
import com.festu.blogify.pojo.Tag;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Date;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    UsersMapper usersMapper;

    public List<NoticeMapping> selectAllNotices(){
        return noticeMapper.selectList(null);
    }

    public List<NoticeMapping> selectAllAndFullNotices(){
        return noticeMapper.selectFullNotice();
    }

    public NoticeMapping selectNoticeById(int id){
        return noticeMapper.selectById(id);
    }

    public NoticeMapping selectFullNoticeById(int id){
        return noticeMapper.selectFullNoticeById(id);
    }

    public Boolean releaseNotice(int noticee_id, String content){
        NoticeMapping noticeMapping = new NoticeMapping();
        noticeMapping.setContent(content);
        noticeMapping.setIsChecked(false);
        noticeMapping.setNoticeeId(noticee_id);
        noticeMapping.setNotifierId(((User)SecurityUtils.getSubject().getPrincipal()).getId());
        noticeMapping.setNoticeTime(new Date());
        try{
            noticeMapper.insert(noticeMapping);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public Result addNotice(NoticeMapping noticeMapping){
        noticeMapping.setNoticeTime(new Date());
        try{
            noticeMapper.insert(noticeMapping);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(noticeMapping);
    }

    public Result deleteNoticeById(int id){
        try{
            noticeMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("通知删除成功");
    }

    public Result deleteBatchByIds(List<Integer> ids){
        try{
            noticeMapper.deleteBatchIds(ids);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("通知删除成功");
    }

    public Result updateNotice(NoticeMapping noticeMapping){
        try{
            noticeMapper.updateById(noticeMapping);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("通知更新成功");
    }

    public List<NoticeMapping> selectByKeyword(String keyword){
        QueryWrapper<NoticeMapping> noticeQuery = new QueryWrapper<>();
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.like("username", keyword).or();
        userQuery.like("nickname", keyword);
        List<User> users = usersMapper.selectList(userQuery);
        List<Integer> userIds = users.stream().map(User::getId).toList();
        if(!userIds.isEmpty()){
            noticeQuery.in("notifier_id", userIds).or();
            noticeQuery.in("noticee_id", userIds).or();
        }
        noticeQuery.like("content", keyword).or();
        noticeQuery.like("id", keyword);
        List<NoticeMapping> noticeMappings = noticeMapper.selectList(noticeQuery);
        List<Integer> noticeIds = noticeMappings.stream().map(NoticeMapping::getId).toList();
        return noticeMapper.selectFullNoticeByIds(noticeIds);
    }
}
