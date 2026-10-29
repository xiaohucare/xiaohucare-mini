package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.BuildingEntity;
import com.xiaohucare.app.care.vo.BuildingVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 康养楼栋表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-21
 */
public class BuildingWrapper extends BaseEntityWrapper<BuildingEntity, BuildingVO> {
    public static BuildingWrapper build() {
        return new BuildingWrapper();
    }
        @Override
        public BuildingVO entityVO(BuildingEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, BuildingVO.class);
        }

}
