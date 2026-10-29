package com.xiaohucare.app.care.wms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.mapper.SalesItemMapper;
import com.xiaohucare.app.care.wms.service.SalesItemService;
import com.xiaohucare.app.care.wms.vo.SalesItemVO;
import com.xiaohucare.app.care.wms.wrapper.SalesItemWrapper;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class SalesItemServiceImpl extends BaseServiceImpl<SalesItemMapper, SalesItemEntity> implements SalesItemService {
	
	@Autowired
	private SalesItemMapper salesItemMapper;
	
	@Override
	public List<SalesItemVO> listByOrderId(Long orderId){
		return salesItemMapper.listByOrderIdWithSkuInfo(orderId);
	}
	@Override
	public List<SalesItemVO> getByOrderId(Long orderId){
		QueryWrapper<SalesItemEntity> queryWrapper=new QueryWrapper<SalesItemEntity>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("order_id", orderId);
        List<SalesItemEntity> list = this.list(queryWrapper);
		return SalesItemWrapper.build().listVO(list);
	}
}