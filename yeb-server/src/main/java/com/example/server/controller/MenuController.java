package com.example.server.controller;


import com.example.server.pojo.Menu;
import com.example.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    /**
     * 这时候并没有传入id 是因为用户一旦登录之后，所有信息 后端已经获取，不需要前端传入
     * SpringSecurity有一个全局对象，一旦登录之后 后端就可以根据全局对象获取用户信息
     * @return
     */
    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }

}
