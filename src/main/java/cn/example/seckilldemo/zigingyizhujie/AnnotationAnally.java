package cn.example.seckilldemo.zigingyizhujie;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Description: 注解验证器
 * 自定义验证器需要实现 ConstraintValidator<验证注解,验证的数据类型> 接口,
 * 其中有两个参数,第一个是验证注解,第二个是需要验证的数据类型
 */
public class AnnotationAnally implements ConstraintValidator<MyAnnotation, String> {

    //验证手机号码
    private static final Pattern PATTERN = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");

    /**
     * initialize() 可以在验证开始前调用注解里的方法，从而获取到一些注解里的参数
     **/
    @Override
    public void initialize(MyAnnotation constraint) {
        System.out.println("开始进行参数验证了...");
    }

    /**
     * isValid() 就是判断是否合法的地方
     **/
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.hasLength(value)) {
            return PATTERN.matcher(value).matches();
        }
        return false;
    }
}

