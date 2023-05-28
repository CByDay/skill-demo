package cn.example.seckilldemo.service.impl;

import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.entity.TSeckillGoods;
import cn.example.seckilldemo.entity.TSeckillOrder;
import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.exception.GlobalException;
import cn.example.seckilldemo.mapper.TOrderMapper;
import cn.example.seckilldemo.service.ITGoodsService;
import cn.example.seckilldemo.service.ITOrderService;
import cn.example.seckilldemo.service.ITSeckillGoodsService;
import cn.example.seckilldemo.service.ITSeckillOrderService;
import cn.example.seckilldemo.utils.MD5Util;
import cn.example.seckilldemo.enums.RespBeanEnum;
import cn.example.seckilldemo.utils.UUIDUtil;
import cn.example.seckilldemo.vo.GoodsVo;
import cn.example.seckilldemo.vo.OrderDeatilVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现类
 *
 * @author LiChao
 * @since 2022-03-03
 */
@Service
@Primary
@Slf4j
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @Autowired
    private ITSeckillGoodsService itSeckillGoodsService;
    @Autowired
    private TOrderMapper tOrderMapper;
    @Autowired
    private ITSeckillOrderService itSeckillOrderService;
    @Autowired
    private ITGoodsService itGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public TOrder secKill(TUser user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 获取秒杀商品的库存信息
        TSeckillGoods seckillGoods = itSeckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goodsVo.getId()));
        // 减库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        // 执行 数据库层面的 减去库存
        boolean seckillGoodsResult = itSeckillGoodsService.update(new UpdateWrapper<TSeckillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goodsVo.getId())
                .gt("stock_count", 0)
        );
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;
        }

        //生成订单
        TOrder order = new TOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        tOrderMapper.insert(order);
        // 生成订单end

        //生成秒杀订单
        TSeckillOrder tSeckillOrder = new TSeckillOrder();
        tSeckillOrder.setUserId(user.getId());
        tSeckillOrder.setOrderId(order.getId());
        tSeckillOrder.setGoodsId(goodsVo.getId());
        itSeckillOrderService.save(tSeckillOrder);
        // 存 redis
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), tSeckillOrder);
        log.info("order:" + user.getId() + ":" + goodsVo.getId()+" , "+ tSeckillOrder);
        return order;
    }

    /**
     *功能描述: 订单详情
     *      
     *@Date 2023-05-28
     *@Param orderId
     *@Return cn.example.seckilldemo.vo.OrderDeatilVo
    */
    @Override
    public OrderDeatilVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        TOrder tOrder = tOrderMapper.selectById(orderId);
        GoodsVo goodsVobyGoodsId = itGoodsService.findGoodsVobyGoodsId(tOrder.getGoodsId());
        OrderDeatilVo orderDeatilVo = new OrderDeatilVo();
        orderDeatilVo.setTOrder(tOrder);
        orderDeatilVo.setGoodsVo(goodsVobyGoodsId);
        return orderDeatilVo;
    }

    @Override
    public String createPath(TUser user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public boolean checkPath(TUser user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue()
                .get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    @Override
    public boolean checkCaptcha(TUser user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue()
                .get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
