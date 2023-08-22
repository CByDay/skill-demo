package cn.example.seckilldemo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description:
 * @Author:
 * @Date: 2023-06-21
 */
public class JdkProxy {


    public static Object getProxy(Object target) {

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("execute--------before");
                //原理在这
                Object invoke = method.invoke(ThreadByProxy.get(), args);
                System.out.println("execute--------after");
                return invoke;
            }
        });
    }

}
