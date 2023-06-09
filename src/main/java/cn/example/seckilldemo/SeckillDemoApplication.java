package cn.example.seckilldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author zhd
 * @Date 2023/5/24
 * @Description:
 */
@SpringBootApplication
//@MapperScan("cn.example.seckilldemo.mapper")
//@EnableSwagger2
@EnableScheduling   // @EnableScheduling 在配置类上使用，开启计划任务的支持（类上）
public class SeckillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }
}
