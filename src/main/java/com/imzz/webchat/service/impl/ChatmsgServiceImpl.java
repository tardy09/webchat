package com.imzz.webchat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.entity.Chatmsg;
import com.imzz.webchat.mapper.ChatmsgMapper;
import com.imzz.webchat.service.ChatmsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@Transactional
@Service
public class ChatmsgServiceImpl extends ServiceImpl<ChatmsgMapper, Chatmsg> implements ChatmsgService {

    @Autowired
    private ChatmsgMapper chatmsgMapper;

    @Override
    public int batchUPdateMsgSigned(List<String> list) {
        return chatmsgMapper.batchUPdateMsgSigned(list);
    }
}
