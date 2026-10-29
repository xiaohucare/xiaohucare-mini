package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.FloorPlanEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 康养楼层表 视图实体类
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "FloorPlanVO对象")
public class FloorPlanVO extends FloorPlanEntity {
        private static final long serialVersionUID = 1L;
}
