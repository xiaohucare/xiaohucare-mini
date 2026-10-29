
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.RoomEntity;
import com.xiaohucare.app.care.vo.RoomVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 康养空间表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
public class RoomWrapper extends BaseEntityWrapper<RoomEntity, RoomVO>  {
    public static RoomWrapper build() {
        return new RoomWrapper();
    }
	@Override
	public RoomVO entityVO(RoomEntity space) {
		if(space==null) {
			return null;
		}
		RoomVO spaceVO = BeanUtil.copy(space, RoomVO.class);
		return spaceVO;
	}

}
