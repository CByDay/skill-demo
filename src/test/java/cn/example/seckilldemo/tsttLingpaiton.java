package cn.example.seckilldemo;

import cn.example.seckilldemo.controller.zhujie.ReplaceParser;
import cn.example.seckilldemo.controller.zhujie.UserListParam;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 测试令牌桶
 * @Author:
 * @Date: 2023-05-30
 */
@SpringBootTest
@Slf4j
public class tsttLingpaiton {

    RateLimiter limiter = RateLimiter.create(5);

    @Test
    @DisplayName("testName")
    void testName() throws Exception {

//        // 每秒产生10个令牌
//        for (int i = 0; i < 12; i++) {
//            new Thread(() -> {
//                // 获取一个令牌
//                limiter.acquire();
//                System.out.println("正常执行方法，ts：" + Instant.now());
//            }).start();
//        }

//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(100L);
//            boolean ac = limiter.tryAcquire();
//            if(ac){
//                System.out.println("拿到了令牌");
//            }
//        }
        int temp = 0;
        int[] arr = {2,33,4,66,8,100,0};
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = 0; j < arr.length-1; j++) {
                if(arr[j] > arr[j+1]){
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;

                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    @Target({ElementType.TYPE,ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface checkNum{
        int nums();
    }

    @Test
    void params(){
        UserListParam param = new UserListParam("  KKKKKKK@DSDSI.COM");
        log.info(param.getUserId());
        Object parseObject =new ReplaceParser().parse(param);
        if(parseObject instanceof UserListParam){
            UserListParam userListParam = (UserListParam) parseObject;
            Assertions.assertNotNull(userListParam);
            log.info("-----------------------------");
            log.info(param.getUserId());
        }
    }
}
