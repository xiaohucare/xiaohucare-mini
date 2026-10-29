package com.xiaohucare.app.care.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaohucare.app.care.entity.ResidentEntity;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.service.ResidentArchiveSyncService;
import com.xiaohucare.app.care.service.ResidentService;
import com.xiaohucare.common.utils.StringUtils;

/**
 * 长者档案入住摘要同步服务实现
 * <p>
 * 说明：全局 update-strategy=not_null，set(null) + updateById 无法清空字段，
 * 因此清空房间/床位/离店时间等必须走 LambdaUpdateWrapper 显式 SET。
 * </p>
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
@Service
public class ResidentArchiveSyncServiceImpl implements ResidentArchiveSyncService {

    @Autowired
    private ResidentService residentService;

    @Override
    public void markCheckedIn(Long residentId, CheckinEntity checkin) {
        if (residentId == null || checkin == null) {
            return;
        }
        Date now = new Date();
        LambdaUpdateWrapper<ResidentEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(ResidentEntity::getId, residentId);
        uw.set(ResidentEntity::getCheckInStatus, 1); // 在住
        uw.set(ResidentEntity::getSettlementStatus, 0); // 未结算
        uw.set(ResidentEntity::getCheckOutTime, null);
        uw.set(ResidentEntity::getCheckOutReason, null);
        uw.set(ResidentEntity::getCheckInTime, now);
        if (checkin.getCheckinDate() != null) {
            uw.set(ResidentEntity::getAdmissionDate, checkin.getCheckinDate());
        }
        // 房间/床位：ID 必同步；名称快照仅在登记单有值时才覆盖，避免误清空既有快照
        uw.set(ResidentEntity::getRoomId, checkin.getRoomId());
        uw.set(ResidentEntity::getBedId, checkin.getBedId());
        if (StringUtils.isNotBlank(checkin.getRoomName())) {
            uw.set(ResidentEntity::getRoomNo, checkin.getRoomName());
        }
        if (StringUtils.isNotBlank(checkin.getBedNo())) {
            uw.set(ResidentEntity::getBedNo, checkin.getBedNo());
        }
        uw.set(ResidentEntity::getUpdateAt, now);
        residentService.update(uw);
    }

    @Override
    public void markCheckedOut(Long residentId, Date checkoutDate) {
        if (residentId == null) {
            return;
        }
        Date now = new Date();
        LambdaUpdateWrapper<ResidentEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(ResidentEntity::getId, residentId);
        uw.set(ResidentEntity::getCheckInStatus, 2); // 已退住
        uw.set(ResidentEntity::getSettlementStatus, 0); // 待结算（结算完成再置 1）
        uw.set(ResidentEntity::getCheckOutTime, checkoutDate != null ? checkoutDate : now);
        uw.set(ResidentEntity::getRoomId, null);
        uw.set(ResidentEntity::getBedId, null);
        uw.set(ResidentEntity::getRoomNo, null);
        uw.set(ResidentEntity::getBedNo, null);
        uw.set(ResidentEntity::getUpdateAt, now);
        residentService.update(uw);
    }

    @Override
    public void markSettled(Long residentId, boolean settled) {
        if (residentId == null) {
            return;
        }
        LambdaUpdateWrapper<ResidentEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(ResidentEntity::getId, residentId);
        uw.set(ResidentEntity::getSettlementStatus, settled ? 1 : 0);
        uw.set(ResidentEntity::getUpdateAt, new Date());
        residentService.update(uw);
    }
}
