
package com.xiaohucare.app.care.wms.vo;
import com.xiaohucare.app.care.wms.entity.CustomerEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 客户表（商品购买客户） 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "CustomerVO对象")
public class CustomerVO extends CustomerEntity {
	private static final long serialVersionUID = 1L;
}
