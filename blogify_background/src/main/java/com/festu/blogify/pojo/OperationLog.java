package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("operation_logs")
public class OperationLog {
    public static final int CREATE = 1;
    public static final int DELETE = 2;
    public static final int UPDATE = 3;
    public static final int SELECT = 4;
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int userId;
    private String remarks;
    @TableField(value = "`type`")
    private int type;
    private Date operationTime;

    @TableField(exist = false)
    private User user;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }
    public String getRemarks(){ return remarks; }
    public void setRemarks(String remarks){ this.remarks = remarks; }
    public int getType(){ return type; }
    public void setType(int type){ this.type = type; }
    public Date getOperationTime(){ return operationTime; }
    public void setOperationTime(Date operationTime){ this.operationTime = operationTime; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
