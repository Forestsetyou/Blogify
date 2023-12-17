package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.util.List;

@TableName("essays")
public class Essay {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int typeId;
    @TableField(exist = false)
    private Type type;
    private int userId;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private List<EssayTagMapping> essayTagMappings;
    private int backgroundPicId;
    @TableField(exist = false)
    private BlogFile blogFile;
    private String title;
    private String content;
    private String abstractions;
    private int views;
    private int praise;
    private Date createTime;
    private Date updateTime;
    @TableField("is_public")
    private Boolean isPublic;

    public Boolean getIsPublic(){return isPublic; }
    public void setIsPublic(Boolean isPublic){ this.isPublic = isPublic; }
    public Date getUpdateTime(){return updateTime; }
    public void setUpdateTime(Date updateTime){ this.updateTime = updateTime; }
    public Date getCreateTime(){return createTime; }
    public void setCreateTime(Date createTime){ this.createTime = createTime; }
    public int getPraise(){return praise; }
    public void setPraise(int praise){ this.praise = praise; }
    public int getViews(){return views; }
    public void setViews(int views){ this.views = views; }
    public String getAbstractions(){return abstractions; }
    public void setAbstractions(String abstractions){ this.abstractions = abstractions; }
    public String getContent(){return content; }
    public void setContent(String content){ this.content = content; }
    public String getTitle(){return title; }
    public void setTitle(String title){ this.title = title; }
    public int getBackgroundPicId(){return backgroundPicId; }
    public void setBackgroundPicId(int backgroundPicId){ this.backgroundPicId = backgroundPicId; }
    public int getUserId(){return userId; }
    public void setUserId(int userId){ this.userId = userId; }
    public int getTypeId(){return typeId; }
    public void setTypeId(int typeId){ this.typeId = typeId; }
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public BlogFile getBlogFile() {
        return blogFile;
    }

    public void setBlogFile(BlogFile blogFile) {
        this.blogFile = blogFile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<EssayTagMapping> getEssayTagMappings() {
        return essayTagMappings;
    }

    public void setEssayTagMappings(List<EssayTagMapping> essayTagMappings) {
        this.essayTagMappings = essayTagMappings;
    }
}
