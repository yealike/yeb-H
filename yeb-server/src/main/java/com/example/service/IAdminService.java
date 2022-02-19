package com.example.service;

import com.example.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, HttpServletRequest request);

    /**
     * 根据用户名获取完整的用户
     * @param username
     * @return
     */
    Admin getAdminByUsername(String username);
}
