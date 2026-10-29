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
 * 企业公报
 * 
 * @author xiaohucare
 * @date 2026-04-15 19:01:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("e_care_paper")
public class StorePaperEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 发布日期
	 */
	@Schema(description = "发布日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "发布日期", width = 30, dateFormat = "yyyy-MM-dd")
	private Date publishDate;
	/**
	 * 优先级
	 */
	@Schema(description = "优先级")
	@Excel(name = "优先级")
	private Integer priority;
	/**
	 * 浏览总数
	 */
	@Schema(description = "浏览总数")
	@Excel(name = "浏览总数")
	private Integer views;
	/**
	 * 下载总数
	 */
	@Schema(description = "下载总数")
	@Excel(name = "下载总数")
	private Integer downloads;
	/**
	 * 评论总数
	 */
	@Schema(description = "评论总数")
	@Excel(name = "评论总数")
	private Integer comments;
	/**
	 * 顶
	 */
	@Schema(description = "顶")
	@Excel(name = "顶")
	private Integer diggs;
	/**
	 * 得分
	 */
	@Schema(description = "得分")
	@Excel(name = "得分")
	private Integer score;
	/**
	 * 0 普通新闻 1 焦点图 2 推荐新闻
	 */
	@Schema(description = "0 普通新闻 1 焦点图 2 推荐新闻 ")
	@Excel(name = "0 普通新闻 1 焦点图 2 推荐新闻 ")
	private Integer infoType;
	/**
	 * 1 热门信息
	 */
	@Schema(description = "1 热门信息 ")
	@Excel(name = "1 热门信息 ")
	private Integer infoHot;
	/**
	 * 审核状态
	 */
	@Schema(description = "审核状态")
	@Excel(name = "审核状态")
	private Integer auditState;
	/**
	 * 内容类型 0 默认 图文 1 存图片新闻
	 */
	@Schema(description = "内容类型 0 默认 图文 1 存图片新闻")
	@Excel(name = "内容类型 0 默认 图文 1 存图片新闻")
	private Integer contentType;
	/**
	 * 主标题
	 */
	@Schema(description = "主标题")
	@Excel(name = "主标题")
	private String title;
	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	@Excel(name = "副标题")
	private String subtitle;
	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	@Excel(name = "副标题")
	private String tags;
	/**
	 * 信息模板
	 */
	@Schema(description = "信息模板")
	@Excel(name = "信息模板")
	private String infoTemplate;
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	@Excel(name = "描述")
	private String description;
	/**
	 * 来源名称
	 */
	@Schema(description = "来源名称")
	@Excel(name = "来源名称")
	private String source;
	/**
	 * 来源url
	 */
	@Schema(description = "来源url")
	@Excel(name = "来源url")
	private String sourceUrl;
	/**
	 * 作者
	 */
	@Schema(description = "作者")
	@Excel(name = "作者")
	private String author;
	/**
	 * 小图
	 */
	@Schema(description = "小图")
	@Excel(name = "小图")
	private String smallImage;
	/**
	 * 视频url
	 */
	@Schema(description = "视频url")
	@Excel(name = "视频url")
	private String video;
	/**
	 * 视频名称
	 */
	@Schema(description = "视频名称")
	@Excel(name = "视频名称")
	private String videoName;
	/**
	 * 文件url
	 */
	@Schema(description = "文件url")
	@Excel(name = "文件url")
	private String file;
	/**
	 * 文件名称
	 */
	@Schema(description = "文件名称")
	@Excel(name = "文件名称")
	private String fileName;
	/**
	 * 文档长度
	 */
	@Schema(description = "文档长度")
	@Excel(name = "文档长度")
	private Long fileLength;
	/**
	 * 是否允许评论
	 */
	@Schema(description = "是否允许评论")
	@Excel(name = "是否允许评论")
	private String isAllowComment;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date updateAt;

}