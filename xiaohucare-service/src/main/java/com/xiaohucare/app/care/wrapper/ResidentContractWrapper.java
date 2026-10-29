package com.xiaohucare.app.care.wrapper;

import com.xiaohucare.app.care.entity.ResidentContractEntity;
import com.xiaohucare.app.care.vo.ResidentContractVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

import cn.hutool.core.util.StrUtil;

/**
 * 合同记录 包装类,返回视图层所需的字段
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
public class ResidentContractWrapper extends BaseEntityWrapper<ResidentContractEntity, ResidentContractVO> {

	public static ResidentContractWrapper build() {
		return new ResidentContractWrapper();
	}

	@Override
	public ResidentContractVO entityVO(ResidentContractEntity contract) {
		if (contract == null) {
			return null;
		}
		ResidentContractVO vo = BeanUtil.copy(contract, ResidentContractVO.class);
		// 前端统一用 pdfUrl 预览，缺省回落到签署合同地址
		if (vo != null && StrUtil.isBlank(vo.getPdfUrl())) {
			vo.setPdfUrl(vo.getSignPdfurl());
		}
		return vo;
	}
}
