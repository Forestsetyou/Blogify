package com.festu.blogify.realm;

import com.festu.blogify.pojo.User;
import com.festu.blogify.service.RoleService;
import com.festu.blogify.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String user_name = principalCollection.getPrimaryPrincipal().toString();
        List<User> users = userService.selectByUsername(user_name);
        if(users.isEmpty()){
            throw new UnknownAccountException();
        }
        User user = users.get(0);
        // 角色
        Set<String> roles_set = new HashSet<>();
        int role_id = user.getRoleId();
        roles_set.add(roleService.selectById(role_id).getName());
        simpleAuthorizationInfo.setRoles(roles_set);
        // 权限
        simpleAuthorizationInfo.setStringPermissions(roleService.selectRolePermissionsById(role_id));

        return simpleAuthorizationInfo;
    }


    @Override   // 登录认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        List<User> users = userService.selectByUsername(username);
        if(users.isEmpty()){
            throw new UnknownAccountException();
        }
        User user = users.get(0);
        String password = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(salt), getName());
    }
}
