package com.xiaohucare.app.care.service;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.vo.RoomUsageVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 康养房间表
 *
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public interface RoomService extends BaseService<RoomEntity> {
    /**
     * 统计当前房间/床位的使用情况（按房间状态分组）
     */
    java.util.Map<Integer, RoomUsageVO> statRoomUsage(Long buildingId, Long floorId);
}
