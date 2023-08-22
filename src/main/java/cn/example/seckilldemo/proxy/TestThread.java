package cn.example.seckilldemo.proxy;

/**
 * @Description:
 * @Author:
 * @Date: 2023-06-21
 */
public class TestThread {

    public static void main(String[] args) {
        //实例化当前对象
        HttpRequest request = new HttpRequestImpl();

//        request.setAttr("key","张三");
        ThreadByProxy.set(request);

        //这句话传入的代理对象只是让他可以执行到代理层代码，而不是真实的执行对象。真实的执行对象放在线程本地变量
        HttpRequest proxy = (HttpRequest) JdkProxy.getProxy(request);

        System.out.println(proxy.getAttr("key")); // 这里输出什么


        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequest reallyObj = new HttpRequestImpl();
                reallyObj.setAttr("key", "李四");
                ThreadByProxy.set(reallyObj);//实际执行变量

                HttpRequest proxy = (HttpRequest) JdkProxy.getProxy(request);//这是是伪的

                System.out.println(proxy.getAttr("key"));   // 这里又输出什么


            }
        }).start();


    }
}
