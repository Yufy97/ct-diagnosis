package cn.nineseven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2WebMvc
@MapperScan("cn.nineseven.mapper")
public class CtApplication {
    public static void main(String[] args) {
        SpringApplication.run(CtApplication.class, args);
    }
}
