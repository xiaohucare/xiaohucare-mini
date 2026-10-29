
package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.RoomEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * 康养空间表 视图实体类
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "RoomVO对象")
public class RoomVO extends RoomEntity {
	private static final long serialVersionUID = 1L;
}
