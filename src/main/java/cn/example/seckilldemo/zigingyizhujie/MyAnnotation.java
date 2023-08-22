package cn.example.seckilldemo.zigingyizhujie;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Description: 自定义注解
 * @Author:
 * @Date: 2023-06-10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Documented
/**
 * @Constraint(validatedBy = XX.class) 用来表示被贴的这个注解是一个验证注解,
 * 参数中指定了验证器，如下我指定了 AnnotationAnally 这个验证器
 **/
@Constraint(validatedBy = AnnotationAnally.class)
public @interface  MyAnnotation {

    /**
     * 校验不通过时的报错信息
     *
     * @return 校验不通过时的报错信息
     */
    String message() default "输入的号码不符合格式，请重新输入!";

    /**
     * 将validator进行分类，不同的类group中会执行不同的validator操作
     *
     * @return validator的分类类型
     */
    Class<?>[] groups() default {};

    /**
     * 主要是针对bean，很少使用
     *
     * @return 负载
     */
    Class<? extends Payload>[] payload() default {};
}

