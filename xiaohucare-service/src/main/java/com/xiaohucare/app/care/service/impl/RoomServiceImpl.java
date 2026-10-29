package com.xiaohucare.app.care.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaohucare.app.care.constants.SPACE_STATUS_ENUM;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.mapper.RoomMapper;
import com.xiaohucare.app.care.service.RoomService;
import com.xiaohucare.app.care.vo.RoomUsageVO;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

@Service
public class RoomServiceImpl extends BaseServiceImpl<RoomMapper, RoomEntity> implements RoomService {

    @Override
    public Map<Integer, RoomUsageVO> statRoomUsage(Long buildingId, Long floorId) {
        LambdaQueryWrapper<RoomEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RoomEntity::getStatus, RoomEntity::getRoomName,
            RoomEntity::getOccupiedCount, RoomEntity::getBedCount);
        queryWrapper.eq(buildingId != null, RoomEntity::getBuildingId, buildingId);
        queryWrapper.eq(floorId != null, RoomEntity::getFloorId, floorId);
        List<RoomEntity> roomList = list(queryWrapper);
        Map<Integer, RoomUsageVO> roomUsage = new HashMap<>();
        for (RoomEntity room : roomList) {
            Integer status = room.getStatus();
            if (roomUsage.containsKey(status)) {
                RoomUsageVO roomUsageVO = roomUsage.get(status);
                roomUsageVO.setOccupiedCount(roomUsageVO.getOccupiedCount() + (room.getOccupiedCount() != null ? room.getOccupiedCount() : 0));
                roomUsageVO.setTotalCount(roomUsageVO.getTotalCount() + (room.getBedCount() != null ? room.getBedCount() : 0));
                roomUsage.put(status, roomUsageVO);
            } else {
                RoomUsageVO roomUsageVO = new RoomUsageVO();
                roomUsageVO.setOccupiedCount(room.getOccupiedCount() != null ? room.getOccupiedCount() : 0);
                roomUsageVO.setTotalCount(room.getBedCount() != null ? room.getBedCount() : 0);
                roomUsageVO.setStatusName(SPACE_STATUS_ENUM.fromCode(status != null ? status : 0).getMessage());
                roomUsageVO.setStatus(status);
                roomUsage.put(status, roomUsageVO);
            }
        }
        return roomUsage;
    }
}
