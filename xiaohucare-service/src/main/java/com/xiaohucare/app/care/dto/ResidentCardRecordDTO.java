
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.ResidentCardRecordEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 会员卡交易流水表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class ResidentCardRecordDTO extends ResidentCardRecordEntity {
	private static final long serialVersionUID = 1L;

}
