package com.festu.blogify.controller;


import com.festu.blogify.pojo.BlogFile;
import com.festu.blogify.pojo.OperationLog;
import com.festu.blogify.pojo.User;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.FileService;
import com.festu.blogify.service.OperationService;
import com.festu.blogify.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    OperationService operationService;
    @Autowired
    UserService userService;

    @GetMapping("/image")
    public Result LoadPictureList(){
        return ResultFactory.buildSuccessResult(fileService.selectAllImages());
    }

    @GetMapping("/image/{id}")
    public Result findPictureById(@PathVariable("id") int id){
        return ResultFactory.buildSuccessResult(fileService.selectImageById(id));
    }

    @DeleteMapping("/image/{id}")
    public Result deletePicture(@PathVariable int id){
        operationService.saveOperationLog("管理员删除了一张图片: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return fileService.deleteImageById(id);
    }

    @PostMapping("/image")
    public Result uploadPicture(@RequestParam("file")MultipartFile file){
        Logger logger = LoggerFactory.getLogger(getClass());
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Result uploadResult = fileService.uploadImage(file);
        if(uploadResult.getCode() != 200){
            return ResultFactory.buildFailResult(uploadResult.getMessage());
        }
        BlogFile blogFile = new BlogFile();
        String imageName = (String) uploadResult.getData();
        blogFile.setName(imageName);
        blogFile.setUserId(user.getId());

        operationService.saveOperationLog("管理员上传了一张图片: Name=" + imageName, OperationLog.CREATE);

        userService.updateActiveTimeForSubject();
        return fileService.addBlogImage(blogFile);
    }
}
