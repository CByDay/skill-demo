package cn.example.seckilldemo.controller;

import cn.example.seckilldemo.config.AccessLimit;
import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.entity.TSeckillOrder;
import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.exception.GlobalException;
//import cn.example.seckilldemo.rabbitmq.MQSender;
import cn.example.seckilldemo.mapper.TUserMapper;
import cn.example.seckilldemo.rabbitmq.MQSender;
import cn.example.seckilldemo.service.ITGoodsService;
import cn.example.seckilldemo.service.ITOrderService;
import cn.example.seckilldemo.service.ITSeckillOrderService;
import cn.example.seckilldemo.utils.CookieUtil;
import cn.example.seckilldemo.utils.JsonUtil;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.enums.RespBeanEnum;
import cn.example.seckilldemo.vo.GoodsVo;
import cn.example.seckilldemo.vo.SeckillMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀
 *
 * @author: LC
 * @date 2022/3/4 11:34 上午
 * @ClassName: SeKillController
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
@Api(value = "秒杀", tags = "秒杀")
public class SeKillController implements InitializingBean {

    @Autowired
    private ITGoodsService itGoodsService;
    @Autowired
    private ITSeckillOrderService itSeckillOrderService;
    @Autowired
    private ITOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();


    @ApiOperation("获取验证码")
    @GetMapping(value = "/captcha")
    public void verifyCode(TUser tUser, Long goodsId, HttpServletResponse response) {
        if (tUser == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置请求头为输出图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + tUser.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

    /**
     * 系统初始化，把商品库存数量加载到Redis
     *
     * @param
     * @return void
     * @author LiChao
     * @operation add
     * @date 6:29 下午 2022/3/8
     **/
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = itGoodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            // 用来检查库存
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }

    /**
     * @param user
     * @param goodsId
     * @Description 立即秒杀
     * @Date 2023-05-26
     * @Param * @param model
     * @Return java.lang.String
     */
//    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(TUser user, Long goodsId,@PathVariable String path) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //优化后代码
        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user, goodsId, path);
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        // 通过内存标记减少对reids 的访问
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 预减库存
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock < 0) {
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);   // 防止为负数（
            RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);





        /* 旧版扣库存
        GoodsVo goodsVo = itGoodsService.findGoodsVobyGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 判断是否重复抢购
//        TSeckillOrder seckillOrder = itSeckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        // 从reids 获取
        TSeckillOrder seckillOrder = (TSeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        TOrder order = orderService.secKill(user, goodsVo);
        return RespBean.success(order);
         */

    }

    /**
     * 获取秒杀地址
     *
     * @param tuser
     * @param goodsId
     * @param captcha
     * @return com.example.seckilldemo.vo.RespBean
     * @author LiChao
     * @operation add
     * @date 4:04 下午 2022/3/9
     **/
    @ApiOperation("获取秒杀地址")
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @GetMapping(value = "/path")
    @ResponseBody
    public RespBean getPath(TUser tuser, Long goodsId, String captcha, HttpServletRequest request) {
        if (tuser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 限制访问次数，5秒内访问5次
        String uri = request.getRequestURI(); // 请求地址
        captcha = "0";
        Integer count = (Integer) valueOperations.get(uri + ":" + tuser.getId());
        if (count == null) { // 为空说明时第一次请求
            valueOperations.set(uri + ":" + tuser.getId(), 1, 5, TimeUnit.SECONDS);
        } else if (count < 5) { // increment 递增
            valueOperations.increment(uri + ":" + tuser.getId());
        } else {
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
        }


        boolean check = orderService.checkCaptcha(tuser, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(tuser, goodsId);
        return RespBean.success(str);
    }

    /**
     * 获取秒杀结果
     *
     * @param tUser
     * @param goodsId
     * @return orderId 成功 ；-1 秒杀失败 ；0 排队中
     * @author LiChao
     * @operation add
     * @date 7:04 下午 2022/3/8
     **/
    @ApiOperation("获取秒杀结果")
    @GetMapping("getResult")
    @ResponseBody
    public RespBean getResult(TUser tUser, Long goodsId) {
        if (tUser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = itSeckillOrderService.getResult(tUser, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * @param user
     * @param goodsId
     * @Description 立即秒杀2 废弃
     * @Date 2023-05-26
     * @Param * @param model
     * @Return java.lang.String
     */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, TUser user, Long goodsId, HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = itGoodsService.findGoodsVobyGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        TSeckillOrder seckillOrder = itSeckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        TOrder order = orderService.secKill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }
}
