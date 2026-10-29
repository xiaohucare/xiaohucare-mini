package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.BedEntity;
import com.xiaohucare.app.care.vo.BedVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 康养床位表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-21
 */
public class BedWrapper extends BaseEntityWrapper<BedEntity, BedVO> {
    public static BedWrapper build() {
        return new BedWrapper();
    }
        @Override
        public BedVO entityVO(BedEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, BedVO.class);
        }

}
