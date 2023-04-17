package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.service.ImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/img")
@Api(tags = "ct图片相关接口")
public class ImgController {

    @Autowired
    ImgService imgService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传ct图片", notes = "图片最大不超过2MB，仅支持.pdf .png .jpg")
    public Result upload(MultipartFile multipartFile){
        return imgService.upload(multipartFile);
    }

    @PutMapping("/update")
    @ApiOperation("修改文件名")
    public Result updateFileName(String id,String newFileName){
        return imgService.updateFileName(id, newFileName);
    }
}
