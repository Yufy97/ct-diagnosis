package cn.nineseven.service.impl;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.Result;
import cn.nineseven.handler.exception.SystemException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class OssServiceImpl {
    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${oss.bucket}")
    private String bucket;



    public String upload(MultipartFile multipartFile, String fileName) {
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);
        String filePath = "ct/" + fileName;

        try {
            InputStream inputStream = multipartFile.getInputStream();
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, filePath, upToken,null, null);
                return "http://" + SystemConstant.OSS_URL + filePath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    throw  new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                }
            }
        } catch (Exception ex) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return null;
    }
}
