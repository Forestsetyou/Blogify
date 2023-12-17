package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

@TableName("tags")
public class Tag {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String name;
    @TableField(exist = false)
    private List<EssayTagMapping> essayTagMappings;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public List<EssayTagMapping> getEssayTagMappings() {
        return essayTagMappings;
    }

    public void setEssayTagMappings(List<EssayTagMapping> essayTagMappings) {
        this.essayTagMappings = essayTagMappings;
    }
}
