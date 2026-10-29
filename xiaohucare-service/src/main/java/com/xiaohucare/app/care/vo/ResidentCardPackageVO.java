
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.ResidentCardPackageEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 会员卡套餐表 视图实体类
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "ResidentCardPackageVO对象")
public class ResidentCardPackageVO extends ResidentCardPackageEntity {
	private static final long serialVersionUID = 1L;
}
