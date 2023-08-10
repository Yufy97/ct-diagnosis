package cn.nineseven.service.impl;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import cn.nineseven.entity.po.User;
import cn.nineseven.entity.vo.CtAnalyseVo;
import cn.nineseven.entity.vo.CtRecordVo;
import cn.nineseven.entity.vo.ImgVo;
import cn.nineseven.entity.vo.PageVo;
import cn.nineseven.handler.exception.SystemException;
import cn.nineseven.mapper.CtRecordMapper;
import cn.nineseven.service.CtRecordService;
import cn.nineseven.service.ImgService;
import cn.nineseven.service.UserService;
import cn.nineseven.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;
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

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CtRecordService ctRecordService;

    @Value("${python-env}")
    private String PYTHON_ENV;

    @Value("${conda-env}")
    private String CONDA_ENV;

    @Value("${py-path}")
    private String path;

    private final Integer nThreads = Runtime.getRuntime().availableProcessors();

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads + 1, nThreads * 2,
            3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(nThreads + 1));

    @Override
    public Result analyse(CtRecordDto ctRecordDto) {
        Img img = imgService.getById(ctRecordDto.getImgId());
        String url = img.getUrl();
        CtRecord ctRecord = new CtRecord();
        ctRecord.setUserId(ctRecordDto.getUserId());
        ctRecord.setImgId(img.getId());
        ctRecordService.save(ctRecord);

        executor.submit(() ->{
            try {
                analyseCt(url, ctRecord);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        return Result.okResult(200, "正在进行分析，请稍后在个人记录中查看");
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
        return Result.okResult(new PageVo(ctRecordVos, page.getTotal()));
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

    @Override
    public Result dataAnalyse(Integer option) {
        CtRecordMapper mapper = getBaseMapper();
        switch (option) {
            case 1:
                return Result.okResult(mapper.dataAnalyseAll());
            case 2:
                return Result.okResult(mapper.dataAnalyseByGender("男"));
            case 3:
                return Result.okResult(mapper.dataAnalyseByGender("女"));
        }
        return null;
    }

    public void analyseCt(String url, CtRecord ctRecord) throws Exception {
        ctRecord.setIsAnalysing(1);

        String[] cmd;

//        cmd = new String[]{PYTHON_ENV, new ClassPathResource("Predict.py").getFile().getPath(), url};
        cmd = new String[]{"conda", "run", "-n", CONDA_ENV, PYTHON_ENV, path, url};
        //todo

        String result = executePy(cmd);
        String[] split = result.split(";");
        Integer status = Integer.valueOf(split[0]);
        String analyse = split[1];

        ctRecord.setIsAnalysing(0);
        ctRecord.setStatus(status);
        ctRecord.setAnalyse(analyse);
        ctRecordService.updateById(ctRecord);
    }

    private String executePy(String[] arr) throws Exception {
        Process proc;
        StringBuilder sb = new StringBuilder();
        proc = Runtime.getRuntime().exec(arr);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        proc.waitFor();
        return sb.toString();
    }
}

