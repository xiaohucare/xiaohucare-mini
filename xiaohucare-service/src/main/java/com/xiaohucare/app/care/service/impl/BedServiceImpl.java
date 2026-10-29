package com.xiaohucare.app.care.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaohucare.app.care.entity.BedEntity;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.mapper.BedMapper;
import com.xiaohucare.app.care.mapper.CheckinMapper;
import com.xiaohucare.app.care.mapper.RoomMapper;
import com.xiaohucare.app.care.service.BedService;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

@Service
public class BedServiceImpl extends BaseServiceImpl<BedMapper, BedEntity> implements BedService {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private CheckinMapper checkinMapper;

    @Override
    public List<BedEntity> listFreeBeds(Long buildingId, Long floorId, Long roomId, Integer bedType) {
        LambdaQueryWrapper<BedEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(buildingId != null, BedEntity::getBuildingId, buildingId);
        queryWrapper.eq(floorId != null, BedEntity::getFloorId, floorId);
        queryWrapper.eq(roomId != null, BedEntity::getRoomId, roomId);
        queryWrapper.eq(bedType != null, BedEntity::getBedType, bedType);
        queryWrapper.eq(BedEntity::getStatus, 1); // 空闲
        queryWrapper.orderByAsc(BedEntity::getSortOrder, BedEntity::getId);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean occupyBed(Long bedId, Long residentId, String residentName, Long checkinId) {
        BedEntity bed = getById(bedId);
        if (bed == null || (bed.getStatus() != null && bed.getStatus() == 2)) {
            return false;
        }
        bed.setStatus(2); // 占用
        bed.setCurResidentId(residentId);
        bed.setCurResidentName(residentName);
        bed.setCheckinId(checkinId);
        bed.setCheckinTime(new Date());
        bed.setUseNum(bed.getUseNum() != null ? bed.getUseNum() + 1 : 1);
        updateById(bed);

        RoomEntity room = roomMapper.selectById(bed.getRoomId());
        if (room != null) {
            int occupied = (room.getOccupiedCount() != null ? room.getOccupiedCount() : 0) + 1;
            room.setOccupiedCount(occupied);
            if (room.getBedCount() != null && occupied >= room.getBedCount()) {
                room.setStatus(3); // 已满房
            } else {
                room.setStatus(2); // 已预订/部分入住
            }
            roomMapper.updateById(room);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean releaseBed(Long bedId) {
        BedEntity bed = getById(bedId);
        if (bed == null) {
            return false;
        }
        // 一人一单后同床仍可能挂其他在住的登记单，条件释放：有 active 单则床位保持占用并指向剩余单
        QueryWrapper<CheckinEntity> activeQuery = new QueryWrapper<>();
        activeQuery.eq("bed_id", bedId);
        activeQuery.eq("is_delete", 0);
        activeQuery.in("checkin_status", 0, 1, 2);
        activeQuery.and(w -> w.isNull("checkout_status").or().lt("checkout_status", 2));
        activeQuery.orderByDesc("id");
        List<CheckinEntity> remaining = checkinMapper.selectList(activeQuery);
        if (remaining != null && !remaining.isEmpty()) {
            CheckinEntity next = remaining.get(0);
            bed.setStatus(2); // 保持占用
            bed.setCurResidentId(next.getResidentId());
            bed.setCurResidentName(next.getResidentName());
            bed.setCheckinId(next.getId());
            bed.setUpdateAt(new Date());
            updateById(bed);
            // 床位仍被占用，房间入住床位数不变
            return true;
        }
        // 确无人在住：真正置空闲
        // 全局 update-strategy=not_null，set null + updateById 无法清空入住人，需用 UpdateWrapper 显式置空
        LambdaUpdateWrapper<BedEntity> bedUpdate = new LambdaUpdateWrapper<>();
        bedUpdate.eq(BedEntity::getId, bedId);
        bedUpdate.set(BedEntity::getStatus, 1); // 空闲
        bedUpdate.set(BedEntity::getCurResidentId, null);
        bedUpdate.set(BedEntity::getCurResidentName, null);
        bedUpdate.set(BedEntity::getCheckinId, null);
        bedUpdate.set(BedEntity::getCheckinTime, null);
        update(bedUpdate);

        RoomEntity room = roomMapper.selectById(bed.getRoomId());
        if (room != null) {
            int occupied = (room.getOccupiedCount() != null && room.getOccupiedCount() > 0) ? room.getOccupiedCount() - 1 : 0;
            room.setOccupiedCount(occupied);
            if (occupied == 0 && room.getStatus() != null && (room.getStatus() == 2 || room.getStatus() == 3)) {
                room.setStatus(1); // 空闲
            }
            roomMapper.updateById(room);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeBed(Long oldBedId, Long newBedId, Long residentId, String residentName, Long checkinId) {
        BedEntity oldBed = getById(oldBedId);
        if (oldBed == null) {
            return false;
        }
        releaseBed(oldBedId);
        return occupyBed(newBedId, residentId, residentName, checkinId);
    }
}
