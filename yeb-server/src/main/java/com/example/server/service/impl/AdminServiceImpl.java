package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.AdminUtils;
import com.example.server.config.security.component.JwtTokenUtil;
import com.example.server.mapper.AdminRoleMapper;
import com.example.server.mapper.RoleMapper;
import com.example.server.pojo.Admin;
import com.example.server.mapper.AdminMapper;
import com.example.server.pojo.AdminRole;
import com.example.server.pojo.RespBean;
import com.example.server.pojo.Role;
import com.example.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@Service

public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    //登录
    @Autowired
    private UserDetailsService userDetailsService;//安全框架，加密解密
    //数据库存的是加密后的密码，SpringSecurity提供一个加密的工具
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {

        String captcha = (String) request.getSession().getAttribute("captcha");
        System.out.println("测试验证码：" + captcha);
        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            //验证码是空的或者验证码匹配不上
            return RespBean.error("验证码输入错误，请重新输入");
        }

        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || passwordEncoder.matches(password, userDetails.getPassword())) {
            //如果匹配失败，就说明用户名或密码错误
            return RespBean.error("用户名或密码不正确");
        }
        //判断它是否被禁用
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员！");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //如果判断都成立说明登录成功
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);//给前端用的，让他放在请求头里面

        return RespBean.success("登录成功", tokenMap);

    }

    /**
     * 根据用户名获取完整的用户对象，
     * 需要去数据查询，提前注入AdminMapper
     *
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username)
                .eq("enabled", true));
    }

    /**
     * 根据用户id查询角色列表
     *
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * 不能查当前登录的操作员
     * 传参的时候除了传keywords 还要传当前登录用户的id
     *
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    /**
     * 更新操作员角色
     *
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));

        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if (rids.length == result) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPass(String oldPass, String pass, Integer adminId) {
        //旧密码要和admin的密码进行比较
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            //如果用户更新密码成功，需要将用户踢下线让用户重新登录
            if (1==result){
                return RespBean.success("更新成功！");
            }
        }
        return RespBean.error("更新失败！");
    }


}
