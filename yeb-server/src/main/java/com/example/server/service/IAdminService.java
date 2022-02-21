package com.example.server.service;

import com.example.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.pojo.Menu;
import com.example.server.pojo.RespBean;
import com.example.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password,String code,HttpServletRequest request);

    /**
     * 根据用户名获取完整的用户
     * @param username
     * @return
     */
    Admin getAdminByUsername(String username);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
