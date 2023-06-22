package cn.example.seckilldemo.zigingyizhujie;

import cn.example.seckilldemo.entity.TGoods;
import cn.example.seckilldemo.mapper.TGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description: 测试并行查询
 * Runnable任务没有返回值，而Callable任务有返回值
 * @Author:
 * @Date: 2023-06-16
 */

@Service
public class TestCallable {

    @Autowired
    private TGoodsMapper tGoodsMapper;

    public int nums = 22; // 无实际意义

    public List<Object> test2(String cardId) throws Exception {

        // ExecutorService是Java提供的线程池，每次我们需要使用线程的时候，可以通过ExecutorService获得线程
        // 三个线程的线程池，核心线程=最大线程，没有临时线程，阻塞队列无界
        // ExecutorService的execute和submit方法区别 submit有返回值，而execute没有
        ExecutorService executorService = Executors.newFixedThreadPool(3); // 创建了一个最大能容纳三条线程的线程池

        // 查询参数
        Object[] objArgs = {cardId};

        long start = System.currentTimeMillis();

        // 开启线程执行
        // Future接口用于获取异步计算的结果，可通过get()获取结果、cancel()取消、isDone()判断是否完成等操作
        // 注意，此处Future对象接收线程执行结果不会阻塞，只有future.get()时候才会阻塞（直到线程执行完返回结果）
        // 反射机制
        // this引用指向当前对象（谁调用这个方法，谁就是当前对象）, 此时的 this 里面有 nums = 22 以及 注入的 TGoodsMapper tGoodsMapper
        // 如果 将 this 替换成 TestCallable.class.newInstance() 就相当于你 自己重新 new 了 TestCallable 这对象，是没有 mapper 注入的，只有一个 变量 22 存在

        Future future1 = executorService.submit(new SelectTask(this, "selectIndustryData1", objArgs));

        Future future2 = executorService.submit(new SelectTask(this, "selectIndustryData2", objArgs));

        Future future3 = executorService.submit(new SelectTask(this, "selectIndustryData3", objArgs));

        //此处用循环保证三个线程执行完毕，再去拼接三个结果
        do {
        } while (!(future1.isDone() && future2.isDone() && future3.isDone()));

        List<Object> result = new ArrayList<>();
        result.add(future1.get());
        result.add(future2.get());
        result.add(future3.get());

        long end = System.currentTimeMillis();
        System.out.println("并行执行：" + (end - start));
        // 关闭执行服务
        executorService.shutdown();
        result.add("并行执行：" + (end - start));
        System.out.println("--this" + this);
        return result;
    }

    //下面是三个真正执行任务（查数据库）的方法
    public TGoods selectIndustryData1(String cardId) throws Exception {
        TGoods result = tGoodsMapper.selectById(cardId);
//        Thread.sleep(1000);    //模拟添加1s耗时
        return result;
    }

    public TGoods selectIndustryData2(String cardId) throws Exception {
        TGoods result = tGoodsMapper.selectById(cardId);
//        Thread.sleep(1000);    //模拟添加1s耗时
        return result;
    }

    public TGoods selectIndustryData3(String cardId) throws Exception {
        TGoods result = tGoodsMapper.selectById(cardId);
//        Thread.sleep(1000);    //模拟添加1s耗时
        return result;
    }

}
