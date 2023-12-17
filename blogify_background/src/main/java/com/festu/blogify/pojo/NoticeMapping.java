package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("notice_mapping")
public class NoticeMapping {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int notifierId;
    private int noticeeId;
    @TableField("is_checked")
    private Boolean isChecked;
    private String content;
    private Date noticeTime;
    @TableField(exist = false)
    private User notifier;
    @TableField(exist = false)
    private User noticee;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getNotifierId(){ return notifierId; }
    public void setNotifierId(int notifierId){ this.notifierId = notifierId; }
    public int getNoticeeId(){ return noticeeId; }
    public void setNoticeeId(int noticeeId){ this.noticeeId = noticeeId; }
    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content; }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public User getNotifier() {
        return notifier;
    }

    public void setNotifier(User notifier) {
        this.notifier = notifier;
    }

    public User getNoticee() {
        return noticee;
    }

    public void setNoticee(User noticee) {
        this.noticee = noticee;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }
}
