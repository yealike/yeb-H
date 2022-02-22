package com.example.server.service;

import com.example.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
