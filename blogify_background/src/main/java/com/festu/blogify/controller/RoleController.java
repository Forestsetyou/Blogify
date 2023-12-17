package com.festu.blogify.controller;


import com.festu.blogify.pojo.Role;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/auth")
    public Result getAuthRole(){
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            return ResultFactory.buildFailResult("尚未登录");
        }
        User user = (User) subject.getPrincipal();
        Role role = roleService.selectById(user.getRoleId());
        return ResultFactory.buildSuccessResult(role);
    }

}
