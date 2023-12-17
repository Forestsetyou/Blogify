package com.festu.blogify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.festu.blogify.pojo.BlogFile;

public interface BlogFileMapper extends BaseMapper<BlogFile> {
    static final int TYPE_IMAGE = 1;
}
