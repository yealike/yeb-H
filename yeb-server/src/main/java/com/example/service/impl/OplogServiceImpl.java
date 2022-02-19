package com.example.service.impl;

import com.example.pojo.Oplog;
import com.example.mapper.OplogMapper;
import com.example.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 叶汉
 * @since 2022-02-19
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
