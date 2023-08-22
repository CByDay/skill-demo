package cn.example.seckilldemo.controller.zhujie.woritelog;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author:
 * @Date: 2023-08-20
 */
@Component
@Slf4j
@Aspect
public class WriteLoginfoAspect {

    @Pointcut("@annotation(cn.example.seckilldemo.controller.zhujie.woritelog.WriteLoginfo)")
    public void writeLoginfoAspect() {

    }

    @Before("writeLoginfoAspect()")
    public void beforWriteLoginfoAspect(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String methodName = joinPoint.getSignature().getName();
        log.info("========================Method: " + methodName + "() begin ========================");

        /** 执行时间 */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        log.info("Time          : " + time);
        log.info("IP          : " + getIpAddr(request));
        log.info("Url          : " + request.getRequestURI());
        log.info("HTTP Method          : " + request.getMethod());
        log.info("Class Method          : " + joinPoint.getSignature().getDeclaringTypeName()+"."+methodName);
        log.info("Request Args          : " + JSON.toJSONString(joinPoint.getArgs()));
        log.info("==============================================================================");
    }

    @After("writeLoginfoAspect()")
    public void afterWriteLoginfoAspect(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("========================Method: " + methodName + "() end ========================");
    }

    //ip获取公共方法
    public String getIp() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest ip = ((ServletRequestAttributes) requestAttributes).getRequest();
        String clientIP = ServletUtil.getClientIP(ip);
        return  clientIP;
    }

    public String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;

        //嵌入jsp网页中，然后用<%String ip=getIpAddr(request);%>得到访问者ip
        //ip就是访问者的ip，你保存到数据库或者缓存中，就由你自己了
    }

}
