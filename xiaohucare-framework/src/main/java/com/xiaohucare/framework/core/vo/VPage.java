package com.xiaohucare.framework.core.vo;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Data;

/**
 * 分页工具类
 * 
 */
@Data
public class VPage<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	private long totalCount;
	//每页记录数
	private long pageSize;
	//总页数
	private long totalPage;
	//当前页数
	private long curPage;
	//列表数据
	private List<T> list;
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public VPage(List<T> list, int totalCount, int pageSize, int curPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}
	public VPage(List<T> list, long totalCount, long pageSize, long curPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}
	public VPage(List<T> list) {
		this.list = list;
		this.totalCount = list.size();
		this.pageSize = list.size();
		this.curPage = 1;
		this.totalPage =1;
	}
	public VPage(List<T> list,IPage<T> page) {
		this.list = list;
		if(page!=null) {
			this.totalCount = page.getTotal();
			this.pageSize = page.getSize();
			this.curPage = page.getCurrent();
			this.totalPage =page.getPages();
		}else {
			this.totalCount = list.size();
			this.pageSize = list.size();
			this.curPage = 1;
			this.totalPage =1;
		}
	}
	/**
	 * 分页
	 */
	public VPage(IPage<T> page) {
		this.list = page.getRecords();
		this.totalCount = (int)page.getTotal();
		this.pageSize = (int)page.getSize();
		this.curPage = (int)page.getCurrent();
		this.totalPage = (int)page.getPages();
	}
	public static <T> VPage<T> toVPage(IPage<T> page) {
		return new VPage<T>(page);
	}
	public static <T> VPage<T> create(IPage<T> page) {
		return new VPage<>(page);
	}
	public  List<T> getList() {
		return this.list;
	}
}
