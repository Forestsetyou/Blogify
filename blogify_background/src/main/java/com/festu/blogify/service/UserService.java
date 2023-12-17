package com.festu.blogify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.festu.blogify.mapper.RolesMapper;
import com.festu.blogify.mapper.UsersMapper;
import com.festu.blogify.pojo.Role;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UsersMapper usersMapper;

    @Autowired
    RolesMapper rolesMapper;

    public Result register(User user){
        List<User> users = this.selectByUsername(user.getUsername());
        if(user.getUsername().isBlank() || user.getPassword().isBlank()){
            return ResultFactory.buildFailResult("用户名或密码不能为空");
        }
        user.setUsername(HtmlUtils.htmlEscape(user.getUsername())); // HTML转义防止XSS
        if(!users.isEmpty()){
            return ResultFactory.buildFailResult("用户已存在");
        }

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, 2).toString();
        user.setRoleId(2);  // 默认注册一般用户
        user.setGender("未知");
        user.setNickname(user.getUsername());
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        Date cur_datetime = new Date();
        user.setCreateTime(cur_datetime);
        user.setLatestActiveTime(cur_datetime);
        try{
            usersMapper.insert(user);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(user);
    }

    public List<User> selectAllUsers(){
        return usersMapper.selectList(null);
    }

    public List<User> selectFullAndAllUsers(){
        return usersMapper.selectFullUsers();
    }

    public User selectFullUserById(int id){
        return usersMapper.selectFullUsersById(id);
    }

    public List<User> selectByUsername(String username){
        Map<String, Object> usernameQuery = new HashMap<String, Object>();
        usernameQuery.put("username", username);
        return usersMapper.selectByMap(usernameQuery);
    }

    public User selectById(int id){
        return usersMapper.selectById(id);
    }

    public Result updateById(User user){
        user.setLatestActiveTime(new Date());
        try{
            usersMapper.updateById(user);
        } catch(Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(usersMapper.selectById(user.getId()));
    }

    public Result deleteById(int user_id){
        try{
            usersMapper.deleteById(user_id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("用户删除成功");
    }

    public Boolean isBanned(User user){    // RoleId 为0默认为被禁用
        return user.getRoleId() == 0;
    }

    public Result deleteBatchByIds(List<Integer> ids){
        try{
            usersMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("删除成功");
    }

    public List<User> selectByKeyword(String keyword){
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.like("name", keyword);
        List<Role> roles = rolesMapper.selectList(roleQuery);
        List<Integer> roleIds = roles.stream().map(Role::getId).toList();
        if(!roleIds.isEmpty()){
            userQuery.in("role_id", roleIds).or();
        }
        userQuery.like("username", keyword).or();
        userQuery.like("introductions", keyword).or();
        userQuery.like("nickname", keyword).or();
        userQuery.like("email", keyword).or();
        userQuery.like("id", keyword);
        List<User> users = usersMapper.selectList(userQuery);
        List<Integer> userIds = users.stream().map(User::getId).toList();
        if(userIds.isEmpty()){
            return null;
        }
        return usersMapper.selectFullUsersByIds(userIds);
    }

    public Boolean updateActiveTimeForSubject(){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setLatestActiveTime(new Date());
        try{
            usersMapper.updateById(user);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
