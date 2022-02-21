package com.example.server.service.impl;

import com.example.server.pojo.Admin;
import com.example.server.pojo.Menu;
import com.example.server.mapper.MenuMapper;
import com.example.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户id查询菜单列表
     *
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //从redis中获取菜单数据
        List<Menu> menus = (List<Menu>) valueOperations.get("menu" + adminId);
        if (CollectionUtils.isEmpty(menus)) {
            //检查menus是否真实存在，如果没有就去数据库里面查
            menus = menuMapper.getMenusByAdminId(adminId);
            //将数据存入redis中
            valueOperations.set("menu_"+adminId,menus);
        }

        return menus;
    }

    /**
     * 根据角色获取菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenuWithRole() {
        return menuMapper.getMenusWithRole();
    }
}
