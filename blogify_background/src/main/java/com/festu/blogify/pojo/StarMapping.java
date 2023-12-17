package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("star_mapping")
public class StarMapping {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int essayId;
    private int userId;
    @TableField(value = "is_praised")
    private Boolean isPraised;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getEssayId(){ return essayId; }
    public void setEssayId(int essayId){ this.essayId = essayId; }
    public int getUserId(){ return userId; }
    public void setUserId(int userId){ this.userId = userId; }
    public Boolean getIsPraised(){ return isPraised; }
    public void setIsPraised(Boolean isPraised){ this.isPraised = isPraised; }
}
