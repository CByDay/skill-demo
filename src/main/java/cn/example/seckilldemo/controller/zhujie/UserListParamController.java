package cn.example.seckilldemo.controller.zhujie;

import cn.example.seckilldemo.controller.zhujie.woritelog.WriteLoginfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description: 控制层
 * @Author:
 * @Date: 2023-08-20
 */

@RestController
@RequestMapping("userList")
@Slf4j
@Validated
public class UserListParamController {

    @WriteLoginfo
    @PostMapping("/list")
    public UserListParam userList(@RequestBody @Valid UserListParam userListParam){
        log.info(userListParam.toString());
        return userListParam;
    }
}
