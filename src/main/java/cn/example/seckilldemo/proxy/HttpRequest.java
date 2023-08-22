package cn.example.seckilldemo.proxy;

/**
 * @Description: 模拟 httpservletrequest
 * @Author:
 * @Date: 2023-06-21
 */
public interface HttpRequest {


    void  setAttr(String key,Object value);

    Object getAttr(String key);
}
