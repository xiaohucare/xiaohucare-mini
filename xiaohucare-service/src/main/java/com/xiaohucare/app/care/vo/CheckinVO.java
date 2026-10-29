
package com.xiaohucare.app.care.vo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.vo.ResidentCardVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 到店登记表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "CheckinVO对象")
public class CheckinVO extends CheckinEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 已废弃：登记合并为一人一单后本字段仅回填当前登记自身（单元素），为未发版的小程序保留读取兼容
	 */
	@Deprecated
	private List<CheckinVO> checkinResidentVOList;

	/**
	 * 本单个性化费用项（原挂在入住长者明细上，现直接挂登记单）
	 */
	@Schema(description = "费用项列表")
	private List<CheckinFeeItemVO> feeItemList;

	/**
	 * 入住近照（列表形式，便于回显）
	 */
	@Schema(description = "入住近照列表")
	private List<String> checkinPhotoList;
	/**
	 * 物品照片（列表形式）
	 */
	@Schema(description = "物品照片列表")
	private List<String> broughtPhotoList;
	/**
	 * 退住时长者近照（列表形式）
	 */
	@Schema(description = "退住近照列表")
	private List<String> checkoutPhotoList;

	ResidentCardVO cardVO;
	 
	 // 结算相关临时字段（不存储到checkin表，仅用于接口传参）
	 @Schema(description = "康养费用")
	 private BigDecimal bedFee;
	 @Schema(description = "增值服务费用")
	 private BigDecimal serviceFee;
	 @Schema(description = "餐饮费用")
	 private BigDecimal mealFee;
	 @Schema(description = "医疗/照护费用")
	 private BigDecimal careFee;
	 @Schema(description = "其他费用")
	 private BigDecimal otherFee;
	 @Schema(description = "费用小计")
	 private BigDecimal subtotal;
	 @Schema(description = "总费用")
	 private BigDecimal totalAmount;

	/**
	 * 兼容未发版小程序：将当前登记的浅拷贝回填到 checkinResidentVOList（单元素），
	 * 并置空拷贝的自身列表以断开引用，避免 Jackson 序列化无限递归。
	 */
	public static void fillCompatResidentList(CheckinVO vo) {
		if (vo == null) {
			return;
		}
		CheckinVO copy = new CheckinVO();
		BeanUtils.copyProperties(vo, copy);
		copy.setCheckinResidentVOList(null);
		List<CheckinVO> compat = new ArrayList<>();
		compat.add(copy);
		vo.setCheckinResidentVOList(compat);
	}
}
