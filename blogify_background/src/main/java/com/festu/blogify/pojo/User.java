package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("`users`")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int roleId;
    private int avatarPicId;
    private String introductions;
    private String email;
    private String gender;
    private String nickname;
    private String username;
    private String password;
    private String salt;
    private Date createTime;
    private Date latestActiveTime;
    @TableField(exist = false)
    private Role role;
    @TableField(exist = false)
    private BlogFile blogFile;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getRoleId(){ return roleId; }
    public void setRoleId(int roleId){ this.roleId = roleId; }
    public int getAvatarPicId(){ return avatarPicId; }
    public void setAvatarPicId(int avatarPicId){ this.avatarPicId = avatarPicId; }
    public String getIntroductions(){ return introductions; }
    public void setIntroductions(String introductions){ this.introductions = introductions; }
    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }
    public String getGender(){ return gender; }
    public void setGender(String gender){ this.gender = gender; }
    public String getNickname(){ return nickname; }
    public void setNickname(String nickname){ this.nickname = nickname; }
    public String  getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
    public String getSalt(){ return salt; }
    public void setSalt(String salt){ this.salt = salt; }
    public Date getCreateTime(){ return createTime; }
    public void setCreateTime(Date createTime){ this.createTime = createTime; }
    public Date getLatestActiveTime(){ return latestActiveTime; }
    public void setLatestActiveTime(Date latestActiveTime){ this.latestActiveTime = latestActiveTime; }

    public BlogFile getBlogFile() {
        return blogFile;
    }

    public void setBlogFile(BlogFile blogFile) {
        this.blogFile = blogFile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
