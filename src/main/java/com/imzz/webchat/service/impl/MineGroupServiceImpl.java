package com.imzz.webchat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.entity.MineGroup;
import com.imzz.webchat.mapper.MineGroupMapper;
import com.imzz.webchat.service.MineGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@Service
public class MineGroupServiceImpl extends ServiceImpl<MineGroupMapper, MineGroup> implements MineGroupService {

    @Autowired
    private MineGroupMapper mineGroupMapper;

    @Override
    public List<MineGroup> mineGroups(String mineId) {
        return mineGroupMapper.mineGroups(mineId);
    }
}
