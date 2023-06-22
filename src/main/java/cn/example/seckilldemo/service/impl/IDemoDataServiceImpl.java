package cn.example.seckilldemo.service.impl;

import cn.example.seckilldemo.entity.DemoData;
import cn.example.seckilldemo.mapper.DemoDataMapper;
import cn.example.seckilldemo.service.IDemoDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Description: excel 持久化
 * @Author:
 * @Date: 2023-06-22
 */

@Service
@Primary
@Slf4j
public class IDemoDataServiceImpl extends ServiceImpl<DemoDataMapper, DemoData> implements IDemoDataService {
}
