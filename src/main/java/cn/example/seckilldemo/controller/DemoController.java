package cn.example.seckilldemo.controller;


import cn.example.seckilldemo.entity.DemoData;
import cn.example.seckilldemo.excels.DemoDataListener;
import cn.example.seckilldemo.service.IDemoDataService;
import cn.example.seckilldemo.zigingyizhujie.TestCallable;
import cn.example.seckilldemo.zigingyizhujie.TestNoCallable;
import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 测试类
 *
 * @author: LC
 * @date 2022/3/1 4:22 下午
 * @ClassName: DemoController
 */
@Controller
@RequestMapping("demo")
@Api(value = "demo", tags = "demo测试类")
public class DemoController {

    /**
     * 测试页面
     *
     * @param model
     * @return java.lang.String
     * @author LC
     * @operation add
     * @date 4:25 下午 2022/3/1
     **/
    @GetMapping(value = "/hello")
    public String hello(Model model) {
        model.addAttribute("msg", "Hello Thymeleaf");
        return "hello";
    }


    @Autowired
    private TestCallable testCallable;

    @Autowired
    private TestNoCallable testNoCallable;

    /**
     * 功能描述: 串行查询数据
     *
     * @return List<Object>
     * @Date: 2023/6/16
     **/

    @RequestMapping(value = "/testTreads1/{cardId}")
    @ResponseBody
    public List<Object> testTreads1(@PathVariable String cardId) throws Exception {
        List<Object> list = testNoCallable.test1(cardId);
        return list;
    }

    /**
     * 功能描述: 并行查询数据
     *
     * @return List<Object>
     * @Date: 2023/6/16
     **/

    @RequestMapping(value = "/testTreads2/{cardId}")
    @ResponseBody
    public List<Object> testTreads2(@PathVariable String cardId) throws Exception {
        List<Object> list = testCallable.test2(cardId);
        return list;
    }

    @Autowired
    private IDemoDataService demoDataService;

    /**
     * 上传Excel，代码参考自官方文档
     */
    @PostMapping("/upload")
    @ResponseBody
    public void upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(),
                        DemoData.class,
                        new DemoDataListener(demoDataService))
                .sheet().doRead();

    }

    public static String path = "D:\\upload2\\";

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        EasyExcel.write(path+"测试持久层导出.xlsx", DemoData.class)
                .sheet("模板")
                .doWrite(demoDataService.list());
    }

}
