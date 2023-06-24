package cn.example.seckilldemo.rabbitmq;

import cn.example.seckilldemo.config.MQConfig;
import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.utils.JsonUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 *
 * @author: LC
 * @date 2022/3/7 7:42 下午
 * @ClassName: MQSender
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送秒杀信息
     *
     * @param message
     * @return void
     * @operation add
     **/
    public void sendSeckillMessage(String message) {
        log.info("发送消息" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }

    /**
     * 发送秒杀信息（核心）
     *
     * @param message
     * @return void
     * @operation add
     **/
    public void sendPayGoodsMessage(String message) {
        log.info("发送消息" + message);
//        rabbitTemplate.convertAndSend("payGoodsExchange", "payGoods.message", message);
    }


    //发送延时消息，10分钟后若未支付则删除order(发送死信消息)
    public void sengDelayMessage(TOrder order) {
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        log.info("订单已创建({})，1分钟后检查是否支付", time);
        String msg = JSON.toJSONString(order);
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_DELAY_ORDER, MQConfig.ROUTING_KEY_DELAY_ORDER, msg, message -> {//设置交换机和路由key
            message.getMessageProperties().setExpiration((1 * 60 * 1000) + "");//设置消息生存时间ttl十分钟
            return message;
        });
    }

    /**
     *功能描述: mq 发送消息
     *          queue 要与接收位置匹配
     *@Date 2023-05-28
     *@Param * @param msg
     *@Return void
     */
//    public void send(Object msg) {
//        log.info("发送消息：" + msg);
//        rabbitTemplate.convertAndSend("queue", msg);
////        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
//    }
//
//
//    public void send01(Object msg) {
//        log.info("发送red" + msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
//    }
//
//    public void send02(Object msg) {
//        log.info("发送red" + msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
//    }
//
//
//    public void send03(Object msg) {
//        log.info("发送消息(QUEUE01接收)：" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
//    }
//
//
//    public void send04(Object msg) {
//        log.info("发送消息(QUEUE02接收)：" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "green.queue.green.message", msg);
//    }
//
//
//    public void send05(String msg) {
//        log.info("发送消息(QUEUE01和QUEUE02接收)：" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color", "red");
//        properties.setHeader("speed", "fast");
//        Message message = new Message(msg.getBytes(), properties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }
//
//    public void send06(String msg) {
//        log.info("发送消息(QUEUE01接收)：" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color", "red");
//        properties.setHeader("speed", "normal");
//        Message message = new Message(msg.getBytes(), properties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }


}
