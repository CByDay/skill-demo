package cn.example.seckilldemo.controller.zhujie;

import cn.example.seckilldemo.zigingyizhujie.MyAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户集合
 * @Author:
 * @Date: 2023-08-20
 */

@Data
public class UserListParam {

    @Replace(source = "_",target = " ")
    private String userId;

    @NotEmpty(message = "不能为空")
    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 功能描述: 校验手机号
     *
     * @Date: 2023/8/20
     * @param null:
     * @return null
     **/
    @MyAnnotation
    private String nickname;

    public UserListParam() {
    }

    public UserListParam(String userId) {
        this.userId = userId;
    }
}
