
package com.xiaohucare.app.care.wms.dto;

import com.xiaohucare.app.care.wms.entity.CustomerEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 客户表（商品购买客户） 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class CustomerDTO extends CustomerEntity {
	private static final long serialVersionUID = 1L;

}
