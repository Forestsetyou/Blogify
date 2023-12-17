package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("comments")
public class Comment {
    static final int TO_BE_AUDITED = 0;
    static final int PASS = 1;
    static final int REJECTED = 2;
    @TableId(type = IdType.AUTO)
    private int id;
    private int parentCommentId;
    @TableField(exist = false)
    private Comment comment;
    private int parentEssayId;
    @TableField(exist = false)
    private Essay essay;
    private int userId;
    @TableField(exist = false)
    private User user;
    private int checkState;
    String content;
    Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(int parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParentEssayId() {
        return parentEssayId;
    }

    public void setParentEssayId(int parentEssayId) {
        this.parentEssayId = parentEssayId;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
