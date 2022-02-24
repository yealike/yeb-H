package com.example.server.mapper;

import com.example.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有操作员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(@Param("id") Integer id,@Param("keywords") String keywords);
}
