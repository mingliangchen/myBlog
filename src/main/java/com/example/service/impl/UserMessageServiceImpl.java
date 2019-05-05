package com.example.service.impl;

import com.example.entity.UserMessage;
import com.example.mapper.UserMessageMapper;
import com.example.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mingliang.chen@hand-china.com
 * @since 2019-04-23
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
