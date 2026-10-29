
package com.xiaohucare.app.care.wms.dto;

import com.xiaohucare.app.care.wms.entity.BatchEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 库存批次表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class BatchDTO extends BatchEntity {
	private static final long serialVersionUID = 1L;

}
