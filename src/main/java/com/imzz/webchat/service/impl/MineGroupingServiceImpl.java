package com.imzz.webchat.service.impl;

import com.imzz.webchat.entity.MineGrouping;
import com.imzz.webchat.mapper.MineGroupingMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.service.MineGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-29
 */
@Service
public class MineGroupingServiceImpl extends ServiceImpl<MineGroupingMapper, MineGrouping> implements MineGroupingService {

    @Autowired
    private MineGroupingMapper mineGroupingMapper;

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    @Override
    public List<MineGrouping> getMineGrouping(String mineId) {
        return mineGroupingMapper.getMineGrouping(mineId);
    }

    @Override
    public List <String> getMineId(String grounpingId) {
        return mineGroupingMapper.getMineId(grounpingId);
    }
}
