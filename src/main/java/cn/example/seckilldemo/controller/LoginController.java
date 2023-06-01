package cn.example.seckilldemo.controller;

import cn.example.seckilldemo.service.ITUserService;
import cn.example.seckilldemo.utils.RespBean;
import cn.example.seckilldemo.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author: LC
 * @date 2022/3/2 10:11 上午
 * @ClassName: LoginController
 */

@Controller
@RequestMapping("/login")
@Slf4j
@Api(value = "登录", tags = "登录")
public class LoginController {


    @Autowired
    private ITUserService tUserService;


    /**
     * 跳转登录页面
     *
     * @param
     * @return java.lang.String
     * @author LC
     * @operation add
     * @date 10:13 上午 2022/3/2
     **/
    @ApiOperation("跳转登录页面")
    @GetMapping(value = "/toLogin")
    public String toLogin() {
        return "login";
    }


    @ApiOperation("登录接口")
    @PostMapping(value = "/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("{}", loginVo);
        return tUserService.doLongin(loginVo, request, response);
    }
}
