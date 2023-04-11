package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/img")
public class ImgController {

    @Autowired
    ImgService imgService;

    @PostMapping("/upload")
    public Result upload(MultipartFile multipartFile){
        return imgService.upload(multipartFile);
    }

}
