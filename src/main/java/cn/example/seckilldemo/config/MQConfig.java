package cn.example.seckilldemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Description: 延时任务（订单超时）
 * @Author:
 * @Date: 2023-05-31
 */
@Configuration
public class MQConfig {

    public static final String EXCHANGE_DELAY_ORDER = "miaosha.order.delay_exchange";//死信交换机名
    public static final String ROUTING_KEY_DELAY_ORDER = "order_delay";//死信路由key
    public static final String QUEUE_DELAY_ORDER = "miaosha.order.delay_queue";//死信队列名
    public static final String QUEUE_ORDER = "miaosha.order.queue";//处理订单超时队列
    public static final String ROUTING_KEY_ORDER = "order";//处理订单超时路由key

    /**
     * 功能描述: 下单
     *
     * @return Binding
     * @Date: 2023/5/31
     **/
    //创建死信队列（自动过期消息队列、延时队列）
    @Bean
    public Queue delayOrderQueue(){
        HashMap<String, Object> params = new HashMap<>();
        //设置死信转发的交换机
        params.put("x-dead-letter-exchange", EXCHANGE_DELAY_ORDER);
        //设置死信在转发时使用的路由key
        params.put("x-dead-letter-routing-key", ROUTING_KEY_ORDER);
        return new Queue(QUEUE_DELAY_ORDER,true,false,false,params);
    }

    //创建死信交换机(普通的交换机)
    @Bean
    public Exchange delayExchange(){
        return new DirectExchange(EXCHANGE_DELAY_ORDER,true, false);
    }

    //死信交换机绑定延迟队列
    @Bean
    public Binding dlxbinding(){
        return BindingBuilder.bind(delayOrderQueue()).to(delayExchange()).with(ROUTING_KEY_DELAY_ORDER).and(null);
    }

}
