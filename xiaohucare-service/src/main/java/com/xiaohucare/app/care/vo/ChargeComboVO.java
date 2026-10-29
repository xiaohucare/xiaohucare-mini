
package com.xiaohucare.app.care.vo;
import java.util.List;

import com.xiaohucare.app.care.entity.ChargeComboEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 收费套餐表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ChargeComboVO对象")
public class ChargeComboVO extends ChargeComboEntity {
	private static final long serialVersionUID = 1L;
	
	List<FeeItemVO> feeItemVoList;
	List<String> suitableForList;
	List<String> environmentImageList;
	
}
