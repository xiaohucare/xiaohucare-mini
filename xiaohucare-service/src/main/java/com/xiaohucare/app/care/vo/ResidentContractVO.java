package com.xiaohucare.app.care.vo;

import com.xiaohucare.app.care.entity.ResidentContractEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同记录 视图实体类
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "ResidentContractVO对象")
public class ResidentContractVO extends ResidentContractEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * PDF 预览地址（前端统一读取 pdfUrl，缺省时回落到 signPdfurl）
	 */
	@Schema(description = "PDF预览地址")
	private String pdfUrl;
}
