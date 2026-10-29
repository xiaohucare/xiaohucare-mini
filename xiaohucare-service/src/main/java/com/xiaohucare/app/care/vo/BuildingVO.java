package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.BuildingEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 康养楼栋表 视图实体类
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "BuildingVO对象")
public class BuildingVO extends BuildingEntity {
        private static final long serialVersionUID = 1L;
}
