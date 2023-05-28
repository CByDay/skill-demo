package cn.example.seckilldemo.controller;

import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.enums.RespBeanEnum;
import cn.example.seckilldemo.service.ITOrderService;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.vo.OrderDeatilVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author:
 * @Date: 2023-05-28
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ITOrderService itOrderService;


    /**
     *功能描述: 订单详情
     *
     *@Date 2023-05-28
     *@Param * @param tUser
     * @param orderId
     *@Return cn.example.seckilldemo.utils.RespBean
    */
    @ApiOperation("订单")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(TUser tUser, Long orderId) {
        if (tUser == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDeatilVo orderDeatilVo = itOrderService.detail(orderId);
        return RespBean.success(orderDeatilVo);
    }
}
