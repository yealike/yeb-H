package com.example.server.controller;

import com.example.server.pojo.Admin;
import com.example.server.pojo.AdminLoginParam;
import com.example.server.pojo.RespBean;
import com.example.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 专门用来登录的
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回Token")
    @PostMapping("login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(
                adminLoginParam.getUsername(),
                adminLoginParam.getPassword(),
                adminLoginParam.getCode(),
                request);
    }
    //获取当前用户的信息以及退出功能
    /*
    当前登录对象设置到SpringSecurity全局对象中去
    Principal principal这个对象获取当前登录用户
     */
    @ApiOperation(value = "获取当前用户登录信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if (null==principal){
            return null;
        }
        String username = principal.getName();//获取当前用户的用户名
        //根据用户名获取完整的用户对象
        Admin admin = adminService.getAdminByUsername(username);
        //出于安全考虑，不能返回用户的密码，即使是加密的也不能返回，返回给前端也没啥用，也不能给普通人看
        //因为得到的是一个完整的Admin对象，需要主动抹去密码
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    //前端处理退出的逻辑，后端返回一个退出成功的接口
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功！");
    }
}
