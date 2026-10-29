
package com.xiaohucare.app.care.wms.vo;
import java.util.List;

import com.xiaohucare.app.care.wms.entity.SalesOrderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 出货单表 视图实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "SalesOrderVO对象")
public class SalesOrderVO extends SalesOrderEntity {
	private static final long serialVersionUID = 1L;
	 List<SalesItemVO> salesItemVOList; 
}
