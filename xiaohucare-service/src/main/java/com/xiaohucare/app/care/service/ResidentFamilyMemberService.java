package com.xiaohucare.app.care.service;
import java.util.List;

import com.xiaohucare.app.care.entity.ResidentFamilyMemberEntity;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 长者家属表
 *
 * @author xiaohucare
 * @date 2026-10-21
 */
public interface ResidentFamilyMemberService extends BaseService<ResidentFamilyMemberEntity> {
    /**
     * 按长者ID查询家属列表
     */
    List<ResidentFamilyMemberEntity> listByResidentId(Long residentId);
}
