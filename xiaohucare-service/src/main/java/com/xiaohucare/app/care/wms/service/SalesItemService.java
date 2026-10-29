package com.xiaohucare.app.care.wms.service;
import java.util.List;

import com.xiaohucare.app.care.wms.entity.SalesItemEntity;
import com.xiaohucare.app.care.wms.vo.SalesItemVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 出货明细表
 *
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public interface SalesItemService extends BaseService<SalesItemEntity> {

	List<SalesItemVO> listByOrderId(Long orderId);
	List<SalesItemVO> getByOrderId(Long orderId);
}

