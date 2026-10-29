
package com.xiaohucare.framework.core.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.extra.utils.Func;
import com.xiaohucare.common.extra.utils.Kv;
import com.xiaohucare.common.extra.utils.MapRemoveNullUtil;
import com.xiaohucare.common.extra.utils.StringUtil;
import com.xiaohucare.common.extra.utils.xss.SQLFilter;
import com.xiaohucare.framework.core.Constant;
import com.xiaohucare.framework.core.mp.support.Query;
import com.xiaohucare.framework.core.mp.support.SqlKeyword;

import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询参数
 */
@Schema(description = "查询条件")
public class VQueryKit {
	public static <T> IPage<T> getPage(Map<String, Object> params) {
		return getPage(params, null, false);
	}
	public static  Map<String, Object>  getQueryByMap(Object paramsT) {
		 Map<String, Object> mapParams = cn.hutool.core.bean.BeanUtil.beanToMap(paramsT);
		MapRemoveNullUtil.removeNullEntry(mapParams);
		return mapParams;
	}
	/**
	 * 转化成mybatis plus中的Page
	 *
	 * @param query 查询条件
	 * @return IPage
	 */
	public static <T> IPage<T> getPage(Query query) {
		Page<T> page = new Page<>(Func.toInt(query.getCurPage(), 1), Func.toInt(query.getPageSize(), 10));
		String[] ascArr = Func.toStrArray(query.getAscs());
		for (String asc : ascArr) {
			page.addOrder(OrderItem.asc(StringUtil.cleanIdentifier(asc)));
		}
		String[] descArr = Func.toStrArray(query.getDescs());
		for (String desc : descArr) {
			page.addOrder(OrderItem.desc(StringUtil.cleanIdentifier(desc)));
		}
		return page;
	}
	public static <T> IPage<T> getPage(int curPage,int pageSize) {
		Page<T> page = new Page<>(Func.toInt(curPage, 1), Func.toInt(pageSize, 10));
		return page;
	}
	public static  Boolean isPage(Map<String, Object> params) {
		long curPage = -1;
		long limit = -10;
		if (params.get(Constant.PAGE) != null) {
			curPage = Long.parseLong( params.get(Constant.PAGE).toString());
		}else if (params.get(Constant.CURPAGE) != null) {
			curPage = Long.parseLong(params.get(Constant.CURPAGE).toString());
		}
		if (params.get(Constant.LIMIT) != null) {
			limit = Long.parseLong(params.get(Constant.LIMIT).toString());
		}else if (params.get(Constant.PAGESIZE) != null) {
			limit = Long.parseLong(params.get(Constant.PAGESIZE).toString());
		}
		if(curPage<0||limit<0) {
			return false;
		}
		return true;
	}
	public static  String getPageLimit(Map<String, Object> params) {
		long curPage = -1;
		long limit = -10;
		
		if (params.get(Constant.PAGE) != null) {
			curPage = Long.parseLong(params.get(Constant.PAGE).toString());
		}else if (params.get(Constant.CURPAGE) != null) {
			curPage = Long.parseLong(params.get(Constant.CURPAGE).toString());
		}
		if (params.get(Constant.LIMIT) != null) {
			limit = Long.parseLong(params.get(Constant.LIMIT).toString());
		}else if (params.get(Constant.PAGESIZE) != null) {
			limit = Long.parseLong( params.get(Constant.PAGESIZE).toString());
		}
		if(curPage<0||limit<0) {
			return null;
		}
		return " limit "+curPage+","+limit;
	}
	public static OrderItem  getOrder(Map<String, Object> params) {
		String orderField = SQLFilter.sqlInject((String) params.get(Constant.ORDER_FIELD));
		String order = (String) params.get(Constant.ORDER);
		if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
			if (Constant.ASC.equalsIgnoreCase(order)) {
				return OrderItem.asc(orderField);
			} else {
				return OrderItem.desc(orderField);
			}
		}
		return null;
	}
	public static <T> IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
		// 分页参数
		long curPage = 1;
		long limit = 10;
		if (params.get(Constant.PAGE) != null) {
			curPage = Long.parseLong(params.get(Constant.PAGE).toString());
		}else if (params.get(Constant.CURPAGE) != null) {
			curPage = Long.parseLong(params.get(Constant.CURPAGE).toString());
		}else if (params.get(Constant.PAGENUM) != null) {
			curPage = Long.parseLong(params.get(Constant.PAGENUM).toString());
		}
		if (params.get(Constant.LIMIT) != null) {
			limit = Long.parseLong(params.get(Constant.LIMIT).toString());
		}else if (params.get(Constant.PAGESIZE) != null) {
			limit = Long.parseLong(params.get(Constant.PAGESIZE).toString());
		}
		// 排序字段
		// 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
		String orderField = SQLFilter.sqlInject((String) params.get(Constant.ORDER_FIELD));
		String order = (String) params.get(Constant.ORDER);
		return getPage(curPage, limit, orderField, order, defaultOrderField, isAsc);
	}
	public static <T> QueryWrapper<T> getQueryWrapperByMap(Map<String, Object> params,List<String> excludeList,Class<T> clazz) {
		 Map<String, Object> query = new HashMap<>();
  	      query.putAll(params);
		  excludeList.forEach(item->{
			query.remove(item);
		});
		QueryWrapper<T> qw = new QueryWrapper<>();
		MapRemoveNullUtil.removeNullEntry(query);
		qw.setEntity(JSONUtil.toBean(JSONUtil.toJsonStr(query),clazz));
		OrderItem orderItem= VQueryKit.getOrder(query);
		if(orderItem!=null)qw.orderBy(true, orderItem.isAsc(), orderItem.getColumn());
		return qw;
	}
	public static  boolean isPageNum(String key) {
		if (Constant.CURPAGE.equalsIgnoreCase(key)) {
			return true;
		}else if (Constant.PAGE.equalsIgnoreCase(key)) {
			return true;
		}else if (Constant.LIMIT.equalsIgnoreCase(key)) {
			return true;
		}else if (Constant.PAGESIZE.equalsIgnoreCase(key)) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query 查询条件
	 * @param clazz 实体类
	 * @param <T>   类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
		Kv exclude = Kv.init()
			.set("current", "current").set("size", "size").set("ascs", "ascs").set("descs", "descs")
			.set(Constant.CURPAGE, Constant.CURPAGE)
			.set(Constant.PAGESIZE, Constant.PAGESIZE)
			.set(Constant.PAGE, Constant.PAGE)
			.set(Constant.LIMIT, Constant.LIMIT)
			.set(Constant.ORDER, Constant.ORDER)
			.set(Constant.ORDER_FIELD, Constant.ORDER_FIELD)
			;
		return getQueryWrapper(query, exclude, clazz);
	}
	public static <T> QueryWrapper<T> getQueryWrapperByMap(Map<String, Object> params,Class<T> clazz) {
		QueryWrapper<T> qw = new QueryWrapper<>();
		 MapRemoveNullUtil.removeNullEntry(params);
		qw.setEntity(JSONUtil.toBean(JSONUtil.toJsonStr(params),clazz));
		OrderItem orderItem= VQueryKit.getOrder(params);
		if(orderItem!=null)qw.orderBy(true, orderItem.isAsc(), orderItem.getColumn());
		return qw;
	}
	/**
	 * 获取mybatis plus中的QueryWrapper
	 * @param query   查询条件
	 * @param exclude 排除的查询条件
	 * @param clazz   实体类
	 * @param <T>     类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
		exclude.forEach((k, v) -> query.remove(k));
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		SqlKeyword.buildCondition(query, qw);
		
		return qw;
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 * @param entity 实体
	 * @param <T>    类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
		return new QueryWrapper<>(entity);
	}
	
	

	public static <T> IPage<T> getPage(Long curPage, Long limit, String orderField, String order) {
		return getPage(curPage, limit, orderField, order, null, false);
	}

	public static <T> IPage<T> getPage(Long curPage, Long limit, String orderField, String order,
			String defaultOrderField, boolean isAsc) {
		if (curPage == null || curPage == 0)
			curPage = 1L;
		if (limit == null || limit == 0)
			limit = 10L;

		// 分页对象
		Page<T> page = new Page<>(curPage, limit);
//        //分页参数
//        params.put(Constant.PAGE, page);
		// 前端字段排序
		if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
			if (Constant.ASC.equalsIgnoreCase(order)) {
				return page.addOrder(OrderItem.asc(orderField));
			} else {
				return page.addOrder(OrderItem.desc(orderField));
			}
		}
		if (defaultOrderField != null) {
			// 默认排序
			if (isAsc) {
				page.addOrder(OrderItem.asc(defaultOrderField));
			} else {
				page.addOrder(OrderItem.desc(defaultOrderField));
			}
		}
		return page;
	}
	
	public static String getUnderColumn(String humpName) {
		return StringUtil.humpToUnderline(humpName);
	}
	
}
