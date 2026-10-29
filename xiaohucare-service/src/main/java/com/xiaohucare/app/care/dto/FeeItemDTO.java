
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.FeeItemEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 收费类目表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-03-21 11:03:33
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class FeeItemDTO extends FeeItemEntity {
	private static final long serialVersionUID = 1L;

}
