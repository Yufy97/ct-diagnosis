package cn.nineseven.service.impl;

import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import cn.nineseven.entity.po.User;
import cn.nineseven.entity.vo.CtAnalyseVo;
import cn.nineseven.entity.vo.CtRecordVo;
import cn.nineseven.entity.vo.ImgVo;
import cn.nineseven.entity.vo.PageVo;
import cn.nineseven.mapper.CtRecordMapper;
import cn.nineseven.service.CtRecordService;
import cn.nineseven.service.ImgService;
import cn.nineseven.service.UserService;
import cn.nineseven.utils.BeanCopyUtils;
import cn.nineseven.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (CtRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 21:05:10
 */
@Service("ctRecordService")
public class CtRecordServiceImpl extends ServiceImpl<CtRecordMapper, CtRecord> implements CtRecordService {

    @Autowired
    ImgService imgService;
    @Autowired
    UserService userService;
    @Override
    public Result analyse(String url) {
        String[] analyseArr = new String[]{SystemConstant.PYTHON_ENVIRONMENT, "D:\\Code\\CT\\src\\main\\resources\\analyse.py", url};
        String s = executePy(analyseArr);
        String[] split = s.split("=");
        Integer status = Integer.valueOf(split[0]);
        String analyse = split[1];
        String[] forecastArr = new String[]{SystemConstant.PYTHON_ENVIRONMENT, "D:\\Code\\CT\\src\\main\\resources\\forecast.py", url};
        String forecast = executePy(forecastArr);

        User user = SecurityUtils.getLoginUser().getUser();
        CtAnalyseVo ctAnalyseVo = BeanCopyUtils.copyBean(user, CtAnalyseVo.class);
        ctAnalyseVo.setStatus(status);
        ctAnalyseVo.setAnalyse(analyse);
        ctAnalyseVo.setForecast(forecast);
        ctAnalyseVo.setUrl(url);
        ctAnalyseVo.setCreateTime(LocalDateTime.now());
        return Result.okResult(ctAnalyseVo);
    }

    @Override
    public Result saveRecord(CtRecordDto ctRecordDto) {
        CtRecord ctRecord = BeanCopyUtils.copyBean(ctRecordDto, CtRecord.class);
        ctRecord.setUserId(SecurityUtils.getUserId());

        save(ctRecord);
        return Result.okResult();
    }

    @Override
    public Result getByUserId(Long userId, Integer pageNum, Integer pageSize, Integer timeSort) {
        LambdaQueryWrapper<CtRecord> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CtRecord::getUserId, userId);
        lqw.orderByAsc(timeSort == 1, CtRecord::getCreateTime);
        lqw.orderByDesc(timeSort == 2, CtRecord::getCreateTime);

        Page<CtRecord> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<CtRecord> records = page.getRecords();
        List<CtRecordVo> ctRecordVos = records.stream().map(record -> {
            CtRecordVo ctRecordVo = new CtRecordVo();
            ctRecordVo.setId(record.getId());
            ImgVo imgVo = BeanCopyUtils.copyBean(imgService.getById(record.getImgId()), ImgVo.class);
            ctRecordVo.setImgVo(imgVo);
            return ctRecordVo;
        }).collect(Collectors.toList());
        return  Result.okResult(new PageVo(ctRecordVos, page.getTotal()));
    }

    @Override
    public Result selectById(Long id) {
        CtRecord ctRecord = getById(id);
        User user = userService.getById(ctRecord.getUserId());
        CtAnalyseVo ctAnalyseVo = BeanCopyUtils.copyBean(user, CtAnalyseVo.class);

        ctAnalyseVo.setUrl(imgService.getById(ctRecord.getImgId()).getUrl());
        BeanUtils.copyProperties(ctRecord, ctAnalyseVo);
        return Result.okResult(ctAnalyseVo);
    }

    private String executePy(String[] arr){
        Process proc = null;
        StringBuilder sb = new StringBuilder();
        try {
            proc = Runtime.getRuntime().exec(arr);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
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

