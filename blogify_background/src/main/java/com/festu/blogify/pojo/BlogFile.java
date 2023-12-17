package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("files")
public class BlogFile {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private String name;
    private String description;
    private Date uploadTime;
    @TableField("is_publish")
    private Boolean isPublish;
    private String publishUrl;
    @TableField("`type`")
    private int type;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id;}
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId;}
    public String getName(){ return name; }
    public void setName(String name){ this.name = name;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUploadTime(){ return uploadTime; }
    public void setUploadTime(Date uploadTime){ this.uploadTime = uploadTime;}
    public Boolean getIsPublish(){ return isPublish; }
    public void setIsPublish(Boolean isPublish){ this.isPublish = isPublish;}
    public String getPublishUrl(){ return publishUrl; }
    public void setPublishUrl(String publishUrl){ this.publishUrl = publishUrl;}
    public int getType(){ return type; }
    public void setType(int type){ this.type = type;}
}
