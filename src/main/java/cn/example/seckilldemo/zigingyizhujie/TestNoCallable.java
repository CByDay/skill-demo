package cn.example.seckilldemo.zigingyizhujie;

import cn.example.seckilldemo.entity.TGoods;
import cn.example.seckilldemo.mapper.TGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 串行查询
 * @Author:
 * @Date: 2023-06-16
 */

@Service
public class TestNoCallable {

    @Autowired
    private TGoodsMapper tGoodsMapper;

    public List<Object> test1(String cardId) throws Exception{
        long start = System.currentTimeMillis();

        //三个串行查询
        TGoods result1 = tGoodsMapper.selectById(cardId);
        TGoods result2 = tGoodsMapper.selectById(cardId);
        TGoods result3 = tGoodsMapper.selectById(cardId);

        Thread.sleep(3000);     //此处模拟每个查询添加1s耗时

        List<Object> result = new ArrayList<>();
        result.add(result1);
        result.add(result2);
        result.add(result3);

        long end = System.currentTimeMillis();
        System.out.println("串行执行：" + (end-start));

        result.add("串行执行：" + (end-start));
        return result;
    }
}
