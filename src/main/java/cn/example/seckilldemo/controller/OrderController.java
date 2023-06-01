package cn.example.seckilldemo.controller;

import cn.example.seckilldemo.entity.TGoods;
import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.enums.RespBeanEnum;
import cn.example.seckilldemo.mapper.TOrderMapper;
import cn.example.seckilldemo.service.ITOrderService;
import cn.example.seckilldemo.service.impl.TGoodsServiceImpl;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.vo.GoodsVo;
import cn.example.seckilldemo.vo.OrderDeatilVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author:
 * @Date: 2023-05-28
 */
@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private ITOrderService itOrderService;

    @Autowired
    private TGoodsServiceImpl tGoodsService;

    @Autowired
    private TOrderMapper orderMapper;


    /**
     * 功能描述: 订单详情
     *
     * @param orderId
     * @Date 2023-05-28
     * @Param * @param tUser
     * @Return cn.example.seckilldemo.utils.RespBean
     */
    @ApiOperation("订单")
    @GetMapping(value = "/detail")
    @ResponseBody
    public RespBean detail(TUser tUser, Long orderId) {
        if (tUser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDeatilVo orderDeatilVo = itOrderService.detail(orderId);
        return RespBean.success(orderDeatilVo);
    }


    /**
     * 功能描述: 下单买东西
     *
     * @param user:
     * @param goodsId:
     * @return RespBean
     * @Date: 2023/5/31
     **/
    @ResponseBody
    @PostMapping(value = "/payGoods")
    public RespBean payGoods(TUser user, @RequestParam("goodsId") long goodsId) {
        log.info("goodsId：" + goodsId);
        TGoods tGoods = tGoodsService.findGoodsVobyGoodsId(goodsId);
        itOrderService.payGoods(user, tGoods);
        return RespBean.success();
    }

    @ApiOperation("商品列表")
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    public String toList(Model model, TUser user, HttpServletRequest request, HttpServletResponse response) {
        IPage<TOrder> page = new Page<>(1, 2);//参数一：当前页，参数二：每页记录数
        //这里想加分页条件的可以如方法三自己构造条件构造器
        IPage<TOrder> tOrderIPage = orderMapper.selectPage(page, null);
        model.addAttribute("user", user);
        model.addAttribute("orderList", tOrderIPage.getRecords());
        return "orderList";
    }

    @ApiOperation("订单列表结账")
    @ResponseBody
    @PostMapping(value = "/payMoney")
    public RespBean payMoney(TUser user, @RequestParam("orderId") Long orderId) {
        UpdateWrapper<TOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", orderId).set("status", 1).set("pay_date", new Date());
        int i = orderMapper.update(null, updateWrapper);
        return RespBean.success(i);
    }
}
