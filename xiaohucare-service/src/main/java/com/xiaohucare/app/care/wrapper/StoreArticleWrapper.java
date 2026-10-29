
package com.xiaohucare.app.care.wrapper;
import com.xiaohucare.app.care.entity.StoreArticleEntity;
import com.xiaohucare.app.care.vo.StoreArticleVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 门店文章报道表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-17 22:48:48
 */
public class StoreArticleWrapper extends BaseEntityWrapper<StoreArticleEntity, StoreArticleVO>  {
    public static StoreArticleWrapper build() {
        return new StoreArticleWrapper();
    }
	@Override
	public StoreArticleVO entityVO(StoreArticleEntity storeArticle) {
		if(storeArticle==null) {
			return null;
		}
		StoreArticleVO storeArticleVO = BeanUtil.copy(storeArticle, StoreArticleVO.class);
		return storeArticleVO;
	}

}
