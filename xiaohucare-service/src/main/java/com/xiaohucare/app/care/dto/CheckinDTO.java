
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.CheckinEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 到店登记表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class CheckinDTO extends CheckinEntity {
	private static final long serialVersionUID = 1L;

}
