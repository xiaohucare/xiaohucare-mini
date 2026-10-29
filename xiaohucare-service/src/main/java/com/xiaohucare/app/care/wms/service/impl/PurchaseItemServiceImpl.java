package com.xiaohucare.app.care.wms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;
import com.xiaohucare.app.care.wms.mapper.PurchaseItemMapper;
import com.xiaohucare.app.care.wms.service.PurchaseItemService;
import com.xiaohucare.app.care.wms.vo.PurchaseItemVO;
import com.xiaohucare.app.care.wms.wrapper.PurchaseItemWrapper;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;




@Service
public class PurchaseItemServiceImpl extends BaseServiceImpl<PurchaseItemMapper, PurchaseItemEntity> implements PurchaseItemService {
	@Autowired
	private PurchaseItemMapper purchaseItemMapper;
	@Override
	public List<PurchaseItemVO> listByOrderId(Long orderId){
		return purchaseItemMapper.listByOrderIdWithSkuInfo(orderId);
	}
	@Override
	public List<PurchaseItemVO> getByOrderId(Long orderId){
		QueryWrapper<PurchaseItemEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("order_id", orderId);
        List<PurchaseItemEntity> list = this.list(queryWrapper);
		return PurchaseItemWrapper.build().listVO(list);
	}
	
	
}