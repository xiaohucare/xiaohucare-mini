
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.StorePaperEntity;
import com.xiaohucare.app.care.vo.StorePaperVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 企业公报 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-15 19:01:57
 */
public class StorePaperWrapper extends BaseEntityWrapper<StorePaperEntity, StorePaperVO>  {
    public static StorePaperWrapper build() {
        return new StorePaperWrapper();
    }
	@Override
	public StorePaperVO entityVO(StorePaperEntity storePaper) {
		if(storePaper==null) {
			return null;
		}
		StorePaperVO storePaperVO = BeanUtil.copy(storePaper, StorePaperVO.class);
		return storePaperVO;
	}

}
