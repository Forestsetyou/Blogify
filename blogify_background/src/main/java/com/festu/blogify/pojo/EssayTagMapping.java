package com.festu.blogify.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

@TableName("essay_tag_mapping")
public class EssayTagMapping {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int essayId;
    private int tagId;
    @TableField(exist = false)
    private Tag tag;
    @TableField(exist = false)
    private Essay essay;
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public int getEssayId(){ return essayId; }
    public void setEssayId(int essayId){ this.essayId = essayId; }
    public int getTagId(){ return tagId; }
    public void setTagId(int tagId){ this.tagId = tagId; }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }
}
