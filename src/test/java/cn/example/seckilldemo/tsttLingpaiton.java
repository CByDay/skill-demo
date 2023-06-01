package cn.example.seckilldemo;

import cn.example.seckilldemo.utils.TokenBucket;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

/**
 * @Description: 测试令牌桶
 * @Author:
 * @Date: 2023-05-30
 */
@SpringBootTest
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

        for (int i = 0; i < 10; i++) {
            Thread.sleep(100L);
            boolean ac = limiter.tryAcquire();
            if(ac){
                System.out.println("拿到了令牌");
            }
        }
    }
}
