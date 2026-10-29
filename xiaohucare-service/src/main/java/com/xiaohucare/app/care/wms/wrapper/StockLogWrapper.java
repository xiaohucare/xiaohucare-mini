
package com.xiaohucare.app.care.wms.wrapper;
import com.xiaohucare.app.care.wms.entity.StockLogEntity;
import com.xiaohucare.app.care.wms.vo.StockLogVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 库存变动记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-12 21:19:36
 */
public class StockLogWrapper extends BaseEntityWrapper<StockLogEntity, StockLogVO>  {
    public static StockLogWrapper build() {
        return new StockLogWrapper();
    }
	@Override
	public StockLogVO entityVO(StockLogEntity stockLog) {
		if(stockLog==null) {
			return null;
		}
		StockLogVO stockLogVO = BeanUtil.copy(stockLog, StockLogVO.class);
		return stockLogVO;
	}

}
