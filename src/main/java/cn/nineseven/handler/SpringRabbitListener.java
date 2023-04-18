package cn.nineseven.handler;

import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import cn.nineseven.service.CtRecordService;
import cn.nineseven.service.ImgService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class SpringRabbitListener {
    @Autowired
    ImgService imgService;
    @Autowired
    CtRecordService ctRecordService;

    @RabbitListener(queues = SystemConstant.QUEUE_NAME, concurrency = "5-10")
    public void analyseCt(String json){

        CtRecordDto ctRecordDto =  JSON.parseObject(json, CtRecordDto.class);
        Img img = imgService.getById(ctRecordDto.getImgId());
        String url = img.getUrl();

        CtRecord ctRecord = new CtRecord();
        ctRecord.setUserId(ctRecordDto.getUserId());
        ctRecord.setImgId(img.getId());
        ctRecordService.save(ctRecord);

        String[] analyseArr = new String[0];
        try {
            analyseArr = new String[]{SystemConstant.PYTHON_ENVIRONMENT, new ClassPathResource("analyse.py").getFile().getPath(), url};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String s = executePy(analyseArr);
        String[] split = s.split("=");
        Integer status = Integer.valueOf(split[0]);
        String analyse = split[1];
        String[] forecastArr = new String[0];
        try {
            forecastArr = new String[]{SystemConstant.PYTHON_ENVIRONMENT, new ClassPathResource("forecast.py").getFile().getPath(), url};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String forecast = executePy(forecastArr);

        ctRecord.setIsAnalysing(0);
        ctRecord.setStatus(status);
        ctRecord.setAnalyse(analyse);
        ctRecord.setForecast(forecast);
        ctRecordService.updateById(ctRecord);
    }

    private String executePy(String[] arr){
        Process proc = null;
        StringBuilder sb = new StringBuilder();
        try {
            proc = Runtime.getRuntime().exec(arr);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
