
package com.xiaohucare.app.care.dto;

import com.xiaohucare.app.care.entity.BedEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 康养床位表 数据传输对象实体类
 * @author xiaohucare
 * @date 2026-10-21
 */
@EqualsAndHashCode(callSuper=true)
@Data
public class BedDTO extends BedEntity {
        private static final long serialVersionUID = 1L;

}
