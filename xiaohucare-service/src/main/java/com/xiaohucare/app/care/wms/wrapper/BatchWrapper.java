
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.BatchEntity;
import com.xiaohucare.app.care.wms.vo.BatchVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 库存批次表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 22:04:06
 */
public class BatchWrapper extends BaseEntityWrapper<BatchEntity, BatchVO>  {
    public static BatchWrapper build() {
        return new BatchWrapper();
    }
	@Override
	public BatchVO entityVO(BatchEntity batch) {
		if(batch==null) {
			return null;
		}
		BatchVO batchVO = BeanUtil.copy(batch, BatchVO.class);
		return batchVO;
	}

}
