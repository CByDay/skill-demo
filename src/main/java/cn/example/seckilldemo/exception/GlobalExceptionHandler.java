package cn.example.seckilldemo.exception;

import cn.example.seckilldemo.quartz.CustomException;
import cn.example.seckilldemo.quartz.Result;
import cn.example.seckilldemo.quartz.ResultCode;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.enums.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理类
 *
 * @author: LC
 * @date 2022/3/2 5:33 下午
 * @ClassName: GlobalExceptionHandler
 */

/*
* @RestControllerAdvice是一个组合注解，由@ControllerAdvice、@ResponseBody组成
* */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
        Spring的 @ExceptionHandler可以用来统一处理方法抛出的异常
        @ExceptionHandler(参数) 参数是某个异常类的class，代表这个方法专门处理该类异常，比如：ExceptionHandler(NumberFormatException.class)
     */
    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException exception = (GlobalException) e;
            return RespBean.error(exception.getRespBeanEnum());
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常：" + bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        log.info("异常信息" + e);
        return RespBean.error(RespBeanEnum.ERROR);
    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(CustomException customException){
        log.error("运行时异常--------->:", customException);
        return Result.createError(customException.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(MethodArgumentNotValidException e){
        log.error("校验异常--------->:", e);
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        return Result.createError(ResultCode.ILLEGAL_ARGUMENT.getCode(),objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常--------->", e);
        return Result.createError(e.getMessage());
    }
}
