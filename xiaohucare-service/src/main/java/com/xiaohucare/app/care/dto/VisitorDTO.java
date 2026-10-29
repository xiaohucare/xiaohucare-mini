
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.VisitorEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 来访者表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-10-22
 */
 @EqualsAndHashCode(callSuper=true)
@Data
public class VisitorDTO extends VisitorEntity {
	private static final long serialVersionUID = 1L;

}
