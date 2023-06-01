package cn.example.seckilldemo.rabbitmq;


import cn.example.seckilldemo.config.MQConfig;
import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.entity.TSeckillGoods;
import cn.example.seckilldemo.entity.TSeckillOrder;
import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.exception.GlobalException;
import cn.example.seckilldemo.mapper.TOrderMapper;
import cn.example.seckilldemo.service.ITGoodsService;
import cn.example.seckilldemo.service.ITOrderService;
import cn.example.seckilldemo.utils.JsonUtil;
import cn.example.seckilldemo.vo.GoodsVo;
import cn.example.seckilldemo.vo.SeckillMessage;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 消息的消费者
 *
 * @author: LC
 * @date 2022/3/7 7:44 下午
 * @ClassName: MQReceiver
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private ITGoodsService itGoodsServicel;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITOrderService itOrderService;

    @Autowired
    private TOrderMapper tOrderMapper;

    /**
     * 下单操作
     * RabbitListener 监听队列
     *
     * @param
     * @return void
     * @author LiChao
     * @operation add
     * @date 6:48 下午 2022/3/8
     **/
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        TUser user = seckillMessage.getTUser();
        GoodsVo goodsVo = itGoodsServicel.findGoodsVobyGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        TSeckillOrder tSeckillOrder = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (tSeckillOrder != null) {
            return;
        }
        //下单操作
        itOrderService.secKill(user, goodsVo);
    }

    /**
     * 功能描述: mq消费，判断订单是否付款（超时）
     *
     * @param message:
     * @return void
     * @Date: 2023/5/31
     **/
    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            name = MQConfig.QUEUE_ORDER,
            durable = "true",
            autoDelete = "false",
            exclusive = "false"),
            exchange = @Exchange(value = MQConfig.EXCHANGE_DELAY_ORDER, type = "direct"),
            key = MQConfig.ROUTING_KEY_ORDER  //这个值对应的就是x-dead-letter-routing-key
    ))
    public void receiveDelayMessage(String message) throws GlobalException {
        UpdateWrapper<TOrder> updateWrapper = new UpdateWrapper<>();
        TOrder tOrder = JSON.parseObject(message, TOrder.class);
        //判断订单是否已超时,超时则更新订单状态
        //订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成
        if (tOrder.getStatus() != 1) {
            long validPay = System.currentTimeMillis() - tOrder.getCreateDate().getTime();
            log.info("相差时间：", validPay);
            if (1 * 1000 < validPay) {//判断当前是否还在1分钟以内，超过1分钟订单状态改变为超时
                tOrder.setStatus(4);
                updateWrapper.eq("goods_id", tOrder.getGoodsId()).set("status", 4);
                tOrderMapper.update(null, updateWrapper);
                log.info("{} 订单{}未支付已超时", new Date());
            }
        } else {
            log.info("{}订单没有超时", new Date());

        }
    }

    /**
     *功能描述: mq接收消息
     *      注意：queues = "queue" 后接的内容 要和你发送的时所传的 一致，否则接收不到
     *@Date 2023-05-28
     *@Param * @param msg
     *@Return void
     */
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        System.out.println("接收到的消息" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void receive07(Message message) {
//        log.info("QUEUE01接收消息 message对象" + message);
//        log.info("QUEUE01接收消息" + new String(message.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void receive08(Message message) {
//        log.info("QUEUE02接收消息 message对象" + message);
//        log.info("QUEUE02接收消息" + new String(message.getBody()));
//    }

}
