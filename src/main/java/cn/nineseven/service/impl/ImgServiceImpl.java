package cn.nineseven.service.impl;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import cn.nineseven.entity.vo.ImgVo;
import cn.nineseven.handler.exception.SystemException;
import cn.nineseven.mapper.ImgMapper;
import cn.nineseven.service.CtRecordService;
import cn.nineseven.service.ImgService;
import cn.nineseven.utils.BeanCopyUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;


@Service("imgService")
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements ImgService {

    @Autowired
    OssServiceImpl ossService;

    @Override
    public Result upload(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".pdf")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String md5 = null;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            md5 = DigestUtils.md5DigestAsHex(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        Img img = lambdaQuery().eq(Img::getId, md5).one();
        if(img == null){
            img = new Img();
            img.setId(md5);
            img.setFileName(md5.substring(16));
            img.setUrl(ossService.upload(multipartFile, md5 + suffix));
            save(img);
        }
        ImgVo imgVo = BeanCopyUtils.copyBean(img, ImgVo.class);
        return Result.okResult(imgVo);
    }

    @Override
    public Result updateFileName(String id, String newFileName) {
        boolean fl = lambdaUpdate().set(Img::getFileName, newFileName).eq(Img::getId, id).update();
        if(!fl){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }
}

