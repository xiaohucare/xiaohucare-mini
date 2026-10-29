
package com.xiaohucare.app.care.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 退住交接提交对象（一人一单，id 即 e_care_checkin.id）
 * 
 * @author xiaohucare
 * @date 2026-10-24
 */

@Data
@Schema(description = "CheckoutVO对象")
public class CheckoutVO {

	/**
	 * 入住登记ID（e_care_checkin.id）
	 */
	@Schema(description = "入住登记ID")
	@Excel(name = "入住登记ID")
	private Long id;
	/**
	 * 退住交接单号
	 */
	@Schema(description = "交接单号")
	@Excel(name = "交接单号")
	private String checkoutNo;
	/**
	 * 实际退住日期
	 */
	@Schema(description = "实际退住日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "实际退住日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date checkoutDate;
	/**
	 * 实际住养天数
	 */
	@Schema(description = "实际住养天数")
	@Excel(name = "实际住养天数")
	private Integer actualDays;
	/**
	 * 身体状况变化结果（0-正常 1-有变化）
	 */
	@Schema(description = "身体状况变化结果（0-正常 1-有变化）")
	@Excel(name = "身体状况变化结果")
	private Integer healthChangeDesc;
	/**
	 * 身体状况变化说明
	 */
	@Schema(description = "变化说明")
	@Excel(name = "变化说明")
	private String appearanceChanges;
	/**
	 * 个人卫生状态（0-整洁 1-一般 2-需清洁）
	 */
	@Schema(description = "个人卫生状态（0-整洁 1-一般 2-需清洁）")
	@Excel(name = "个人卫生状态")
	private Integer careSummary;
	/**
	 * 离店照片URL（逗号分隔）
	 */
	@Schema(description = "离店照片URL（逗号分隔）")
	@Excel(name = "离店照片URL")
	private String checkoutPhotos;
	/**
	 * 归还物品（逗号分隔）
	 */
	@Schema(description = "归还物品（逗号分隔）")
	@Excel(name = "归还物品")
	private String returnedItems;
	/**
	 * 物品状态（0-完好 1-有损耗 2-有损坏）
	 */
	@Schema(description = "物品状态（0-完好 1-有损耗 2-有损坏）")
	@Excel(name = "物品状态")
	private Integer itemsStatus;
	/**
	 * 物品交接确认（0-未确认 1-已确认）
	 */
	@Schema(description = "物品交接确认（0-未确认 1-已确认）")
	@Excel(name = "物品交接确认")
	private Integer itemsConfirmed;
	/**
	 * 物品备注
	 */
	@Schema(description = "物品备注")
	@Excel(name = "物品备注")
	private String itemsRemark;
	/**
	 * 在住适应情况
	 */
	@Schema(description = "在住适应情况")
	@Excel(name = "在住适应情况")
	private String performance;
	/**
	 * 健康提醒
	 */
	@Schema(description = "健康提醒")
	@Excel(name = "健康提醒")
	private String healthReminder;
	/**
	 * 后续照料建议
	 */
	@Schema(description = "后续照料建议")
	@Excel(name = "后续照料建议")
	private String suggestions;
	/**
	 * 满意度评分（1-5）
	 */
	@Schema(description = "满意度评分（1-5）")
	@Excel(name = "满意度评分")
	private Integer customerSatisfaction;
	/**
	 * 长者/家属反馈
	 */
	@Schema(description = "长者/家属反馈")
	@Excel(name = "长者/家属反馈")
	private String customerFeedback;
	/**
	 * 本单费用项（新增/修改/删除一次提交）
	 */
	@Schema(description = "费用项列表")
	private List<CheckinFeeItemVO> feeItemList;
}
