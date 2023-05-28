package cn.example.seckilldemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: MQ&秒杀
 * @Author:
 * @Date: 2023-05-28
 */
@Configuration
public class RabbitMQSeckillConfig {

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    /**
     *功能描述:
     *      seckill.# 此为自定义
     *@Date 2023-05-28
     *@Param * @param
     *@Return org.springframework.amqp.core.Binding
    */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
