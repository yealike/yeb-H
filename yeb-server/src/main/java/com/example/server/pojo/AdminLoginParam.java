package com.example.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登录实体类，仅用于处理前端登录的请求参数
 * 原始的Admin含有太多参数，不方便处理
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AdminLogin对象",description = "")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名，必填",required = true)
    private String username;
    @ApiModelProperty(value = "密码，必填",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;

}
