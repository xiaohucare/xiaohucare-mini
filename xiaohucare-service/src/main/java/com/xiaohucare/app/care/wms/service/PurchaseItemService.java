package com.xiaohucare.app.care.wms.service;
import java.util.List;

import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;
import com.xiaohucare.app.care.wms.vo.PurchaseItemVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 进货明细表
 *
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public interface PurchaseItemService extends BaseService<PurchaseItemEntity> {

	List<PurchaseItemVO> getByOrderId(Long orderId);

	List<PurchaseItemVO> listByOrderId(Long orderId);
}

