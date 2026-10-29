package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.VisitorEntity;
import com.xiaohucare.app.care.vo.VisitorVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 来访者表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-22
 */
public class VisitorWrapper extends BaseEntityWrapper<VisitorEntity, VisitorVO> {
    public static VisitorWrapper build() {
        return new VisitorWrapper();
    }
        @Override
        public VisitorVO entityVO(VisitorEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, VisitorVO.class);
        }

}
