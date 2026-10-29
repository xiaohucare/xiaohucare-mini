package com.xiaohucare.app.care.vo;
import com.xiaohucare.app.care.entity.BedEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 康养床位表 视图实体类
 * @author xiaohucare
 * @date 2026-10-21
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Schema(description = "BedVO对象")
public class BedVO extends BedEntity {
        private static final long serialVersionUID = 1L;
}
