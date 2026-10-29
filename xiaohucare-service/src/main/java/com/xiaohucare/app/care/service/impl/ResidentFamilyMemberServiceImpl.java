package com.xiaohucare.app.care.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.ResidentFamilyMemberEntity;
import com.xiaohucare.app.care.mapper.ResidentFamilyMemberMapper;
import com.xiaohucare.app.care.service.ResidentFamilyMemberService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

/**
 * 长者家属表
 *
 * @author xiaohucare
 * @date 2026-10-21
 */
@Service
public class ResidentFamilyMemberServiceImpl extends BaseServiceImpl<ResidentFamilyMemberMapper, ResidentFamilyMemberEntity> implements ResidentFamilyMemberService {

    @Override
    public List<ResidentFamilyMemberEntity> listByResidentId(Long residentId) {
        QueryWrapper<ResidentFamilyMemberEntity> qw = new QueryWrapper<>();
        qw.eq("resident_id", residentId).orderByDesc("is_emergency_contact").orderByDesc("id");
        return this.list(qw);
    }
}
