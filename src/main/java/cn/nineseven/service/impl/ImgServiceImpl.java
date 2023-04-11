package cn.nineseven.service.impl;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.po.Img;
import cn.nineseven.mapper.ImgMapper;
import cn.nineseven.service.ImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * (Img)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 21:05:57
 */
@Service("imgService")
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements ImgService {

    @Override
    public Result upload(MultipartFile multipartFile) {
        return null;
    }
}

