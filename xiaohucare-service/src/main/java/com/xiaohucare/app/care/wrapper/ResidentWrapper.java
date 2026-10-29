package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ResidentEntity;
import com.xiaohucare.app.care.vo.ResidentVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 长者档案表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-21
 */
public class ResidentWrapper extends BaseEntityWrapper<ResidentEntity, ResidentVO> {
    public static ResidentWrapper build() {
        return new ResidentWrapper();
    }
        @Override
        public ResidentVO entityVO(ResidentEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, ResidentVO.class);
        }

}
