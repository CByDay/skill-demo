package cn.example.seckilldemo.service;

import cn.example.seckilldemo.entity.TGoods;
import cn.example.seckilldemo.entity.TOrder;
import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.vo.GoodsVo;
import cn.example.seckilldemo.vo.OrderDeatilVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 服务类
 *
 * @author LiChao
 * @since 2022-03-03
 */
public interface ITOrderService extends IService<TOrder> {

    /**
     * 秒杀
     *
     * @param user    用户对象
     * @param goodsVo 商品对象
     * @return com.example.seckilldemo.entity.TOrder
     * @author LC
     * @operation add
     * @date 1:44 下午 2022/3/4
     **/
    TOrder secKill(TUser user, GoodsVo goodsVo);

    /**
     * 订单详情方法
     *
     * @param orderId
     * @return com.example.seckilldemo.vo.OrderDeatilVo
     * @author LC
     * @operation add
     * @date 10:21 下午 2022/3/6
     **/
    OrderDeatilVo detail(Long orderId);

    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @return java.lang.String
     * @author LiChao
     * @operation add
     * @date 2:59 下午 2022/3/9
     **/
    String createPath(TUser user, Long goodsId);

    /**
     * 校验秒杀地址
     *
     * @param user
     * @param goodsId
     * @param path
     * @return boolean
     * @author LiChao
     * @operation add
     * @date 2:59 下午 2022/3/9
     **/
    boolean checkPath(TUser user, Long goodsId, String path);

    /**
     * 校验验证码
     *
     * @param tuser
     * @param goodsId
     * @param captcha
     * @return boolean
     * @author LiChao
     * @operation add
     * @date 3:52 下午 2022/3/9
     **/
    boolean checkCaptcha(TUser tuser, Long goodsId, String captcha);

    /**
     * 功能描述: 商品下单
     *
     * @param tuser:
     * @param goodsId:
     * @return boolean
     * @Date: 2023/5/31
     **/
    boolean payGoods(TUser tuser, TGoods tGoods);
}
