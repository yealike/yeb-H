package com.example.server.mapper;

import com.example.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id查询角色列表
     *
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
