
package com.xiaohucare.framework.system.domain.vo;
import com.xiaohucare.framework.system.domain.SysOss;

import lombok.Data;
import lombok.EqualsAndHashCode;

 /**
 * oss管理 视图实体类
 * @author xiaohucare
 * @date 2026-05-18 12:35:39
 */
 
@Data
@EqualsAndHashCode(callSuper=true)
public class SysOssVO extends SysOss {
	private static final long serialVersionUID = 1L;
	
	private String previewUrl;
}
