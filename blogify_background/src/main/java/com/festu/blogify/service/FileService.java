package com.festu.blogify.service;

import com.festu.blogify.mapper.BlogFileMapper;
import com.festu.blogify.pojo.BlogFile;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileService {
    @Value("${blogify.server_host}")
    private String serverHost;

    @Value("${blogify.upload_path}")
    private String uploadPath;

    @Value("${blogify.path_pattern}")
    private String pathPattern;

    @Value("${blogify.valid_suffix}")
    private String validSuffix;

    @Value("${blogify.image_path_prefix}")
    private String imagePathPrefix;

    @Autowired
    BlogFileMapper blogFileMapper;

    public String getUploadPath(){ return uploadPath; }

    public String getPathPattern(){ return pathPattern; }
    public List<BlogFile> selectAllBlogFiles(){
        return blogFileMapper.selectList(null);
    }

    public List<BlogFile> selectAllImages(){
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("type", 1);    // 1 代表图片
        return blogFileMapper.selectByMap(queryMap);
    }

    public Result selectImageById(int id){
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("type", blogFileMapper.TYPE_IMAGE);    // 1 代表图片
        queryMap.put("id", id);    // 1 代表图片
        List<BlogFile> blogFiles = blogFileMapper.selectByMap(queryMap);
        if(blogFiles.isEmpty()){
            return ResultFactory.buildFailResult("目标图片不存在");
        }
        return ResultFactory.buildSuccessResult(blogFiles.get(0));
    }

    public Result selectImageByUrl(String url){
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("type", blogFileMapper.TYPE_IMAGE);    // 1 代表图片
        queryMap.put("publish_url", url);    // 1 代表图片
        List<BlogFile> blogFiles = blogFileMapper.selectByMap(queryMap);
        if(blogFiles.isEmpty()){
            return ResultFactory.buildFailResult("目标图片不存在");
        }
        return ResultFactory.buildSuccessResult(blogFiles.get(0));
    }

    public BlogFile selectBlogFileById(int id){
        return blogFileMapper.selectById(id);
    }

    public Result addBlogImage(BlogFile blogFile){
        blogFile.setIsPublish(true);
        blogFile.setUploadTime(new Date());
        blogFile.setType(blogFileMapper.TYPE_IMAGE);
        blogFile.setPublishUrl(this.serverHost + this.pathPattern + this.imagePathPrefix + blogFile.getName());
        try{
            blogFileMapper.insert(blogFile);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult(blogFile.getPublishUrl());
    }

    public Result deleteBlogFileById(int id){
        try{
            blogFileMapper.deleteById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("文件记录删除成功");
    }

    public Result updateBlogFile(BlogFile blogFile){
        try{
            blogFileMapper.updateById(blogFile);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("文件记录更新成功");

    }

    public Result uploadImage(MultipartFile file){
        if(file.isEmpty()){
            return ResultFactory.buildFailResult("上传文件为空");
        }
        List<String> validSuffix = Arrays.asList(this.validSuffix.replace(" ", "").split(","));
        try{
            // 检查文件后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            if(!validSuffix.contains(suffix)){
                return ResultFactory.buildFailResult("文件类型错误，只能上传图片文件!");
            }
            // 检查生成父目录
            File parentFolder = new File(this.uploadPath);
            if(!parentFolder.exists()){
                if(!parentFolder.mkdirs()){
                    return ResultFactory.buildFailResult("文件上传功能故障!");
                }
            }
            String confusionName = new SecureRandomNumberGenerator().nextBytes().toString().replace("/", "")
                    + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                    + "." + suffix;
            String fullPath = this.uploadPath + this.imagePathPrefix + confusionName;
            file.transferTo(new File(fullPath));
            return ResultFactory.buildSuccessResult(confusionName);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result deleteImageById(int id){
        BlogFile blogFile = blogFileMapper.selectById(id);
        File file = new File(this.uploadPath + this.imagePathPrefix + blogFile.getName());
        try{
            if(!file.delete()){
                return ResultFactory.buildFailResult("图片删除失败!");
            }
            this.deleteBlogFileById(id);
        } catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("成功删除");
    }
}
