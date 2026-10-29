package com.xiaohucare.framework.core.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 分页工具类
 *
 * @author xiaohucare
 * @date 2016年11月4日 下午12:59:00
 */
@Data
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
  	private long totalCount;
  	//每页记录数
  	private long pageSize;
  	//总页数
  	private long totalPage;
  	//当前页数
  	private long currentPage;
  	//列表数据
  	private List<?> list;
    //扩展
    private Object filterCategory;
    
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtils(List<?> list, long totalCount, long pageSize, long currentPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPage = (long)Math.ceil((double)totalCount/pageSize);
	}
    

}
