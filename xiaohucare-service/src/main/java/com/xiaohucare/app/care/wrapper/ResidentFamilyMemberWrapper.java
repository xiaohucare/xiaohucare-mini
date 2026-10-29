package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.ResidentFamilyMemberEntity;
import com.xiaohucare.app.care.vo.ResidentFamilyMemberVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

/**
 * 长者家属表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-10-21
 */
public class ResidentFamilyMemberWrapper extends BaseEntityWrapper<ResidentFamilyMemberEntity, ResidentFamilyMemberVO> {
    public static ResidentFamilyMemberWrapper build() {
        return new ResidentFamilyMemberWrapper();
    }
        @Override
        public ResidentFamilyMemberVO entityVO(ResidentFamilyMemberEntity entity) {
                if(entity==null) {
                        return null;
                }
                return BeanUtil.copy(entity, ResidentFamilyMemberVO.class);
        }

}
