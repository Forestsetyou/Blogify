package com.festu.blogify.controller;

import com.festu.blogify.pojo.*;
import com.festu.blogify.result.Result;
import com.festu.blogify.result.ResultFactory;
import com.festu.blogify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/api/v1/essay")
public class EssayController {
    @Autowired
    EssayService essayService;
    @Autowired
    EssayTagService essayTagService;
    @Autowired
    FileService fileService;
    @Autowired
    OperationService operationService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    UserService userService;

    @GetMapping("")
    public Result loadAllAndFullEssays(){
        return ResultFactory.buildSuccessResult(essayService.selectFullAndALlEssays());
    }

    @GetMapping("/{id}")
    public Result findEssay(@PathVariable("id") int id){
        Essay essay = essayService.selectFullEssayById(id);
        if(essay == null){
            return ResultFactory.buildFailResult("博文不存在!");
        }
        return ResultFactory.buildSuccessResult(essay);
    }

    @PostMapping("")
    public Result addEssay(@RequestBody Essay essay){
        essay.setId(0);
        if(essay.getBlogFile().getPublishUrl() != null){
            String imgUrl = essay.getBlogFile().getPublishUrl();
            Result imgResult = fileService.selectImageByUrl(imgUrl);
            if(imgResult.getCode() == 200){
                BlogFile image = (BlogFile)imgResult.getData();
                essay.setBackgroundPicId(image.getId());
            }
        }
        Result essaySave = essayService.insertEssay(essay);
        if(essaySave.getCode() != 200) {
            return ResultFactory.buildFailResult("博文保存失败!");
        }
        Essay saveEssay = (Essay) essaySave.getData();
        if(saveEssay.getEssayTagMappings() != null && !saveEssay.getEssayTagMappings().isEmpty()){
            for(EssayTagMapping essayTagMapping:saveEssay.getEssayTagMappings()){
                essayTagMapping.setEssayId(saveEssay.getId());
            }
            Result essayTagMappingSave = essayTagService.addTagMappings(essay.getEssayTagMappings());
            if(essayTagMappingSave.getCode() != 200){
                return ResultFactory.buildFailResult("博文保存失败!");
            }
        }
        operationService.saveOperationLog("管理员发布了一篇博文: ID=" + saveEssay.getId(), OperationLog.CREATE);
        userService.updateActiveTimeForSubject();
        return ResultFactory.buildSuccessResult("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteEssay(@PathVariable("id") int id){
        operationService.saveOperationLog("管理员删除了一篇博文: ID=" + id, OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return essayService.deleteEssayById(id);
    }

    @PutMapping("/{id}")
    public Result updateEssay(@PathVariable("id") int id, @RequestBody Essay essay){
        essay.setId(id);
        String imgUrl = essay.getBlogFile().getPublishUrl();
        Essay originalEssay = essayService.selectFullEssayById(id);
        if(imgUrl == null){
            if(originalEssay.getBlogFile() != null){
                essay.setBackgroundPicId(0);    // 置空
            }
        } else if(originalEssay.getBlogFile() == null || !imgUrl.equals(originalEssay.getBlogFile().getPublishUrl())){
            Result imgResult = fileService.selectImageByUrl(imgUrl);
            if(imgResult.getCode() == 200){
                BlogFile image = (BlogFile)imgResult.getData();
                essay.setBackgroundPicId(image.getId());
            } else {
                essay.setBackgroundPicId(0);
            }
        }
        Result saveEssayResult = essayService.updateEssay(essay);
        if(saveEssayResult.getCode() != 200){
            return ResultFactory.buildFailResult("博文保存失败!");
        }
        Result saveEssayTagMappingResult = essayService.updateEssayTagsMapping(essay);
        if(saveEssayTagMappingResult.getCode() != 200) {
            return ResultFactory.buildFailResult("标签映射更新失败!");
        }
        operationService.saveOperationLog("管理员更新了一篇博文: ID=" + id, OperationLog.UPDATE);
        userService.updateActiveTimeForSubject();
        return ResultFactory.buildSuccessResult("更新成功");
    }

    @PostMapping("/keyword/select")
    public Result selectByKeyword(@RequestBody Map<String, Object> req){
        return ResultFactory.buildSuccessResult(essayService.selectByKeyword((String) req.get("keyword")));
    }

    @PostMapping("/batch/delete")
    public Result batchDeleteByIds(@RequestBody List<Essay> essays){
        List<Integer> ids = essays.stream().map(Essay::getId).toList();

        StringBuilder notice = new StringBuilder("管理员删除了多篇博文: ID=[");
        for(int i=0; i<essays.size(); i++){
            if(i != 0) notice.append(", ");
            notice.append(essays.get(i).getId());
        }
        notice.append("]");
        operationService.saveOperationLog(notice.toString(), OperationLog.DELETE);
        userService.updateActiveTimeForSubject();
        return essayService.deletBatchByIds(ids);
    }
}
