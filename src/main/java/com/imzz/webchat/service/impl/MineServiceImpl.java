package com.imzz.webchat.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.entity.Mine;
import com.imzz.webchat.mapper.MineMapper;
import com.imzz.webchat.service.MineService;
import org.apache.commons.lang3.StringUtils;
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
public class MineServiceImpl extends ServiceImpl<MineMapper, Mine> implements MineService {

    @Autowired
    private MineMapper mineMapper;

    @Override
    public List<Mine> mines(String groupingId) {
        if(StringUtils.isNoneBlank(groupingId)){
            return mineMapper.mines(groupingId);
        }
        return null;
    }

    @Override
    public List <Mine> mineList(Integer pageIndex, Integer pageSize, String id) {
        return mineMapper.mineList((pageIndex - 1) * pageSize,pageSize,id);
    }
}
