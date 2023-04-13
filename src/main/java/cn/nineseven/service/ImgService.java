package cn.nineseven.service;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.po.Img;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


/**
 * (Img)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 21:05:57
 */
public interface ImgService extends IService<Img> {

    Result upload(MultipartFile multipartFile);

    Result updateFileName(String id, String newFileName);
}

