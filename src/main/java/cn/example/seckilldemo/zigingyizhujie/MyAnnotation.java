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
 * @Constraint(validatedBy = XX.class) 用来表示被贴的这个注解是一个验证注解, 参数中指定了验证器，如下我指定了 AnnotationAnally 这个验证器
 **/
@Constraint(validatedBy = AnnotationAnally.class)
public @interface  MyAnnotation {
    String message() default "参数效验失败!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

