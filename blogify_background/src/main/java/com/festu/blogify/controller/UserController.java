package com.festu.blogify.controller;

import com.festu.blogify.pojo.BlogFile;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.FileService;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private OperationService operationService;

    @GetMapping("/logout")
    public Result logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("登出成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        user.setUsername(HtmlUtils.htmlEscape(user.getUsername()));
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        User loginUser;
        try{
            subject.login(usernamePasswordToken);
            loginUser = userService.selectByUsername(user.getUsername()).get(0);
            if(userService.isBanned(loginUser)){
                return ResultFactory.buildFailResult("用户已被封禁");
            }
        } catch (IncorrectCredentialsException e) {
            return ResultFactory.buildFailResult("用户名或密码错误");
        } catch (UnknownAccountException e){
            return ResultFactory.buildFailResult("用户名或密码错误");
        }
        operationService.saveOperationLog("用户 " + loginUser.getUsername() + " 登录", OperationLog.SELECT);
        userService.updateActiveTimeForSubject();
        loginUser = userService.selectFullUserById(loginUser.getId());
        return ResultFactory.buildSuccessResult(loginUser);
    }

    @GetMapping("")    // 获取所有用户
    public Result loadUsers(){
        return ResultFactory.buildSuccessResult(userService.selectFullAndAllUsers());
    }

    @GetMapping("/{id}")
    public Result findUser(@PathVariable("id") int user_id){
        User user = userService.selectFullUserById(user_id);
        if(user != null){
            return ResultFactory.buildSuccessResult(user);
        } else {
            return ResultFactory.buildFailResult("用户不存在");
        }
    }

    @PostMapping("")   // 注册 = 添加用户
    public Result register(@RequestBody User user){
        operationService.saveOperationLog("用户 " + user.getUsername() + " 注册", OperationLog.CREATE);
        return userService.register(user);
    }

    @PutMapping("/{id}")   // 更新用户
    public Result updateUser(@PathVariable("id") int user_id, @RequestBody User user){
        user.setId(user_id);
        String password = user.getPassword();
        String origin_password = userService.selectById(user_id).getPassword();
        if(user.getBlogFile() != null){
            BlogFile blogFile = (BlogFile) fileService.selectImageByUrl(user.getBlogFile().getPublishUrl()).getData();
            if(blogFile != null){
                user.setAvatarPicId(blogFile.getId());
            }
        }
        if(!password.equals(origin_password)){
            String salt = user.getSalt();
            String algorithmName = "md5";   // 加密算法
            String encodePassword = new SimpleHash(algorithmName, password, salt, 2).toString();
            user.setPassword(encodePassword);
        }
        operationService.saveOperationLog("用户 " + user.getUsername() + " 信息更新", OperationLog.UPDATE);
        return userService.updateById(user);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") int user_id){
        operationService.saveOperationLog("管理员删除了一名用户: ID=" + user_id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return userService.deleteById(user_id);
    }


    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<User> users){
        StringBuilder notice = new StringBuilder("管理员删除了多名用户: ID=[");
        for(int i=0; i<users.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(users.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);
        List<Integer> ids = users.stream().map(User::getId).toList();
        userService.updateActiveTimeForSubject();
        return userService.deleteBatchByIds(ids);
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(userService.selectByKeyword((String) req.get("keyword")));
    }
}
