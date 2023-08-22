package cn.example.seckilldemo.controller.zhujie;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author:
 * @Date: 2023-08-20
 */
public class ReplaceParser {

    public Object parse(Object o){
        Class<?> mClass = null;

        Object o1 = o;
        try {
            mClass = o.getClass();
            Field[] fields = mClass.getDeclaredFields();
            for (Field field: fields){
                if(field.isAnnotationPresent(Replace.class)){
                    field.setAccessible(true);
                    /** 获取 当前 field 设置的注解 */
                    Replace trimSpace = field.getAnnotation(Replace.class);
                    String source = trimSpace.source();
                    String target = trimSpace.target();

                    /** 获取当前 field 的值进行处理 */
                    String s = String.valueOf(field.get(o1));
                    String replace = s.replace(target,source);
                    field.set(o1,replace);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return o1;
    }
}
