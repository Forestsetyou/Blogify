package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("roles")
public class Role {
    static final int NOT_LOGIN = 0;
    static final int ADMIN = 1;
    static final int USER = 2;
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    String name;
    Boolean commentPermission;
    Boolean essayPublishPermission;
    Boolean uploadPermission;
    Boolean hideEssayAccessPermission;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public Boolean getCommentPermission(){ return commentPermission; }
    public void setCommentPermission(Boolean commentPermission){ this.commentPermission = commentPermission; }
    public Boolean getEssayPublishPermission(){ return essayPublishPermission; }
    public void setEssayPublishPermission(Boolean essayPublishPermission){ this.essayPublishPermission = essayPublishPermission; }
    public Boolean getUploadPermission(){ return uploadPermission; }
    public void setUploadPermission(Boolean uploadPermission){ this.uploadPermission = uploadPermission; }
    public Boolean getHideEssayAccessPermission(){ return hideEssayAccessPermission; }
    public void setHideEssayAccessPermission(Boolean hideEssayAccessPermission){ this.hideEssayAccessPermission = hideEssayAccessPermission; }
}
