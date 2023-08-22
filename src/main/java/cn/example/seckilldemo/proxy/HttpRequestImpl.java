package cn.example.seckilldemo.proxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:
 * @Date: 2023-06-21
 */
public class HttpRequestImpl implements HttpRequest{
  private Map<String,Object> data=  new LinkedHashMap<String,Object>();

    @Override
    public void setAttr(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public Object getAttr(String key) {
        return data.get(key);
    }
}
