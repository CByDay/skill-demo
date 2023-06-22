package cn.example.seckilldemo.excels;

import cn.example.seckilldemo.entity.DemoData;
import cn.example.seckilldemo.mapper.DemoDataMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 假设这个是你的DAO存储。当然还要这个类让spring管理，当然你不用需要存储，也不需要这个类。
 **/

public class DemoDAO {


    @Autowired
    DemoDataMapper demoDataMapper;

    static int i = 0;

    public void save(List<DemoData> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
        list.forEach(s -> demoDataMapper.insert(s));

    }
}