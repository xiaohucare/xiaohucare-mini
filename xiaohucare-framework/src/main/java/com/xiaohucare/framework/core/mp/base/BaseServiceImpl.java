/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaohucare.framework.core.mp.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohucare.common.extra.utils.CollectionUtil;
import com.xiaohucare.framework.core.Constant;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author xiaohucare
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

	@Override
	public boolean save(T entity) {
		entity.setIsDelete(Constant.DB_NOT_DELETED);
		return super.save(entity);
	}
	@Override
	public boolean updateById(T entity) {
		return super.updateById(entity);
	}

	@Override
	public boolean deleteLogic(List<Long> ids) {
		if(CollectionUtil.isEmpty(ids)) {
			return false;
		}
		return super.removeByIds(ids);
	}
	@Override
	public List<T> getByIds(Collection<? extends Serializable> idList){
		return baseMapper.selectBatchIds(idList);
	}
	@Override
	public List<T> list(T entity){
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(entity);
		return baseMapper.selectList(qw);
	}
  
}
