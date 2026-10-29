package com.xiaohucare.app.care.wms.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.wms.entity.PurchaseItemEntity;
import com.xiaohucare.app.care.wms.vo.PurchaseItemVO;
/**
 * 进货明细表
 * 
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
@Mapper
public interface PurchaseItemMapper extends BaseMapper<PurchaseItemEntity> {
	  /**
     * 根据订单ID查询明细（连表查询SKU信息）
     */
    List<PurchaseItemVO> listByOrderIdWithSkuInfo(@Param("orderId") Long orderId);
}
