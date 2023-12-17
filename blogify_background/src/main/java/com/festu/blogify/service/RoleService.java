package com.festu.blogify.service;

import com.festu.blogify.mapper.RolesMapper;
import com.festu.blogify.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    RolesMapper rolesMapper;

    public Role selectById(int id){
        return rolesMapper.selectById(id);
    }

    // 返回权限集合，用于shiro认证
    public Set<String> selectRolePermissionsById(int id){
        Set<String> perm_set = new HashSet<>();
        Role role = rolesMapper.selectById(id);
        if(role.getCommentPermission()) perm_set.add("comment");
        if(role.getUploadPermission()) perm_set.add("upload");
        if(role.getEssayPublishPermission()) perm_set.add("essay_publish");
        if(role.getHideEssayAccessPermission()) perm_set.add("hide_essay_access");
        return perm_set;
    }
}
