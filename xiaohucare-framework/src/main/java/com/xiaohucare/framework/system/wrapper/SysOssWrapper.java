
package com.xiaohucare.framework.system.wrapper;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
import com.xiaohucare.framework.system.domain.SysOss;
import com.xiaohucare.framework.system.domain.vo.SysOssVO;

  /**
 * oss管理 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-05-18 12:35:39
 */
public class SysOssWrapper extends BaseEntityWrapper<SysOss, SysOssVO>  {
    public static SysOssWrapper build() {
        return new SysOssWrapper();
    }
	@Override
	public SysOssVO entityVO(SysOss sysOss) {
		if(sysOss==null) {
			return null;
		}
		SysOssVO sysOssVO = BeanUtil.copy(sysOss, SysOssVO.class);
		return sysOssVO;
	}

}
