package com.xiaohucare.app.care.service;
import java.util.List;

import com.xiaohucare.app.care.entity.BedEntity;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 康养床位表
 *
 * @author xiaohucare
 * @date 2026-10-21
 */
public interface BedService extends BaseService<BedEntity> {
    /**
     * 查询空闲床位（可按房间/楼栋/楼层/床位类型过滤）
     */
    List<BedEntity> listFreeBeds(Long buildingId, Long floorId, Long roomId, Integer bedType);

    /**
     * 入住占床：锁定床位并联动房间入住人数与房态
     */
    boolean occupyBed(Long bedId, Long residentId, String residentName, Long checkinId);

    /**
     * 退住放床：释放床位并联动房间入住人数与房态
     */
    boolean releaseBed(Long bedId);

    /**
     * 换床：释放旧床并占用新床（联动两侧房间入住人数）
     */
    boolean changeBed(Long oldBedId, Long newBedId, Long residentId, String residentName, Long checkinId);
}
