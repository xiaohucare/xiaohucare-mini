package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.FloorPlanEntity;
import com.xiaohucare.app.care.vo.FloorPlanVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 康养楼层表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-21
 */
public class FloorPlanWrapper extends BaseEntityWrapper<FloorPlanEntity, FloorPlanVO> {
    public static FloorPlanWrapper build() {
        return new FloorPlanWrapper();
    }
        @Override
        public FloorPlanVO entityVO(FloorPlanEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, FloorPlanVO.class);
        }

}
