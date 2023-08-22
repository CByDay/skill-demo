package cn.example.seckilldemo.proxy;

/**
 * @Description: 线程本地类，存储线程的真实代理对象
 * @Author:
 * @Date: 2023-06-21
 */
public class ThreadByProxy {
  private static ThreadLocal<Object> threadLocal=new ThreadLocal<>();

    public static Object get(){
        return threadLocal.get();
    }


    public static void set(Object obj){
        threadLocal.set(obj);
    }

    public void remove(){
        threadLocal.remove();
    }
}
