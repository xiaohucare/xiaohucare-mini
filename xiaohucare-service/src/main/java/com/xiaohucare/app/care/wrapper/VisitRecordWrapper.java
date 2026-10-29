
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.VisitRecordEntity;
import com.xiaohucare.app.care.vo.VisitRecordVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;
  /**
 * 来访/外出记录表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2025-11-23 22:33:31
 */
public class VisitRecordWrapper extends BaseEntityWrapper<VisitRecordEntity, VisitRecordVO>  {
    public static VisitRecordWrapper build() {
        return new VisitRecordWrapper();
    }
	@Override
	public VisitRecordVO entityVO(VisitRecordEntity visitRecord) {
		if(visitRecord==null) {
			return null;
		}
		VisitRecordVO visitRecordVO = BeanUtil.copy(visitRecord, VisitRecordVO.class);
		// 派生外出状态：实际返回时间为空 => 1 外出中，否则 => 0 已返回
		visitRecordVO.setStatus(StringUtils.isBlank(visitRecord.getActualReturnTime()) ? 1 : 0);
		return visitRecordVO;
	}

}
