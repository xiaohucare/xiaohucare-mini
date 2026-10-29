package com.xiaohucare.app.care.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;
import com.xiaohucare.framework.core.mp.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门店文章报道表
 * 
 * @author xiaohucare
 * @date 2026-04-17 22:48:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_article")
public class StoreArticleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 文章ID
	 */
	@TableId
	@Schema(description = "文章ID")
	@Excel(name = "文章ID")
	private Long id;
	/**
	 * 文章标题
	 */
	@Schema(description = "文章标题")
	@Excel(name = "文章标题")
	private String title;
	/**
	 * 文章分类：news-门店新闻, activity-活动报道, knowledge-宠物知识, case-客户案例, promotion-宣传文章
	 */
	@Schema(description = "文章分类：news-门店新闻, activity-活动报道, knowledge-宠物知识, case-客户案例, promotion-宣传文章")
	@Excel(name = "文章分类：news-门店新闻, activity-活动报道, knowledge-宠物知识, case-客户案例, promotion-宣传文章")
	private String category;
	/**
	 * 作者
	 */
	@Schema(description = "作者")
	@Excel(name = "作者")
	private String author;
	/**
	 * 封面图URL
	 */
	@Schema(description = "封面图URL")
	@Excel(name = "封面图URL")
	private String coverImage;
	/**
	 * 文章摘要
	 */
	@Schema(description = "文章摘要")
	@Excel(name = "文章摘要")
	private String summary;
	/**
	 * 文章内容（支持Markdown）
	 */
	@Schema(description = "文章内容（支持Markdown）")
	@Excel(name = "文章内容（支持Markdown）")
	private String content;
	/**
	 * 标签（逗号分隔）
	 */
	@Schema(description = "标签（逗号分隔）")
	@Excel(name = "标签（逗号分隔）")
	private String tags;
	/**
	 * 浏览量
	 */
	@Schema(description = "浏览量")
	@Excel(name = "浏览量")
	private Integer viewCount;
	/**
	 * 点赞数
	 */
	@Schema(description = "点赞数")
	@Excel(name = "点赞数")
	private Integer likeCount;
	/**
	 * 评论数
	 */
	@Schema(description = "评论数")
	@Excel(name = "评论数")
	private Integer commentCount;
	/**
	 * 排序（数字越小越靠前）
	 */
	@Schema(description = "排序（数字越小越靠前）")
	@Excel(name = "排序（数字越小越靠前）")
	private Integer sortOrder;
	/**
	 * 是否置顶：0-否，1-是
	 */
	@Schema(description = "是否置顶：0-否，1-是")
	@Excel(name = "是否置顶：0-否，1-是")
	private Integer isTop;
	/**
	 * 状态：0-草稿，1-已发布，2-已下架
	 */
	@Schema(description = "状态：0-草稿，1-已发布，2-已下架")
	@Excel(name = "状态：0-草稿，1-已发布，2-已下架")
	private Integer status;
	/**
	 * 发布时间
	 */
	@Schema(description = "发布时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date publishTime;
	/**
	 * 创建者
	 */
	@Schema(description = "创建者")
	@Excel(name = "创建者")
	private String createBy;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新者
	 */
	@Schema(description = "更新者")
	@Excel(name = "更新者")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	@Excel(name = "备注")
	private String remark;
	/**
	 * SEO标题
	 */
	@Schema(description = "SEO标题")
	@Excel(name = "SEO标题")
	private String seoTitle;
	/**
	 * SEO关键词
	 */
	@Schema(description = "SEO关键词")
	@Excel(name = "SEO关键词")
	private String seoKeywords;
	/**
	 * SEO描述
	 */
	@Schema(description = "SEO描述")
	@Excel(name = "SEO描述")
	private String seoDescription;

}