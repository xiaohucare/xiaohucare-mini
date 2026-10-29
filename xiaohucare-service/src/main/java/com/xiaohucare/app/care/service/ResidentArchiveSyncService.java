package com.xiaohucare.app.care.service;

import java.util.Date;

import com.xiaohucare.app.care.entity.CheckinEntity;

/**
 * 长者档案入住摘要同步服务
 * <p>
 * 康养全链路（登记确认 → 退住交接 → 结算）中，e_resident 上的入住摘要字段
 * （checkInStatus/roomId/bedId/roomNo/bedNo/admissionDate/checkInTime/checkOutTime/settlementStatus）
 * 需要随主流程同步维护，避免各控制器各自散写导致档案状态与登记单不一致。
 * </p>
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
public interface ResidentArchiveSyncService {

    /**
     * 押金确认、正式入住：档案置为在住，并快照房间/床位与入住时间。
     *
     * @param residentId 长者档案ID
     * @param checkin    已确认的登记单（提供房间/床位/入住日期快照）
     */
    void markCheckedIn(Long residentId, CheckinEntity checkin);

    /**
     * 完成退住交接：档案置为已退住，清空房间/床位，记录离店时间，结算状态重置为未结算。
     *
     * @param residentId   长者档案ID
     * @param checkoutDate 实际离店时间（为空取当前时间）
     */
    void markCheckedOut(Long residentId, Date checkoutDate);

    /**
     * 结算完成/回滚：更新档案结算状态。
     *
     * @param residentId 长者档案ID
     * @param settled    true-已结算 false-未结算
     */
    void markSettled(Long residentId, boolean settled);
}
