package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.service.ImgService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/img")
public class ImgController {

    @Autowired
    ImgService imgService;

    @PostMapping("/upload")
    @ApiOperation("上传ct图片")
    public Result upload(MultipartFile multipartFile){
        return imgService.upload(multipartFile);
    }

    @PutMapping("/update")
    @ApiOperation("修改文件名")
    public Result updateFileName(String id,String newFileName){
        return imgService.updateFileName(id, newFileName);
    }
}
