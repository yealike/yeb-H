package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.config.security.JwtTokenUtil;
import com.example.server.pojo.Admin;
import com.example.server.mapper.AdminMapper;
import com.example.server.pojo.RespBean;
import com.example.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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


    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public RespBean login(String username, String password,String code, HttpServletRequest request) {

        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
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
                userDetails,null,userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //如果判断都成立说明登录成功
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);//给前端用的，让他放在请求头里面

        return RespBean.success("登录成功",tokenMap);

    }

    /**
     * 根据用户名获取完整的用户对象，
     * 需要去数据查询，提前注入AdminMapper
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username)
                .eq("enabled",true));
    }
}
