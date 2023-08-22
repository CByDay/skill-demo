package cn.example.seckilldemo.service.impl;


import cn.example.seckilldemo.entity.TUser;
import cn.example.seckilldemo.enums.RespBeanEnum;
import cn.example.seckilldemo.exception.GlobalException;
import cn.example.seckilldemo.mapper.TUserMapper;
import cn.example.seckilldemo.service.ITUserService;
import cn.example.seckilldemo.utils.CookieUtil;
import cn.example.seckilldemo.utils.MD5Util;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.utils.UUIDUtil;
import cn.example.seckilldemo.vo.LoginVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author LiChao
 * @since 2022-03-02
 */
@Service
@Primary
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {


    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLongin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //参数校验
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
//        }
        //TODO 因为我懒测试的时候，把手机号码和密码长度校验去掉了，可以打开。页面和实体类我也注释了，记得打开
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        TUser user = tUserMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成Cookie
        String userTicket = UUIDUtil.uuid();
        // 将 用户信息存入 redis
        redisTemplate.opsForValue().set("user:"+userTicket,user);
        CookieUtil.setCookie(request, response, "userTicket", userTicket);
        return RespBean.success(userTicket);

    }

    /**
     * @Description: 根据cookie 获取用户
     * @Author: zhd 
     * @Date: 2023/5/26
     * @param: null
     * @return: null
     **/
    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        // 从redis 中获取 用户对象
        TUser user = (TUser) redisTemplate.opsForValue()
                .get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /**
     *@Description 更新密码
     *@Date 2023-05-27
     *@Param * @param userTicket
     * @param password
     * @param request
     * @param response
     *@Return cn.example.seckilldemo.utils.RespBean
    */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        TUser user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int result = tUserMapper.updateById(user);
        if (1 == result) {
            //删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
