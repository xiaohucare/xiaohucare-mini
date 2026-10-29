package com.xiaohucare.app.care.vo;

import java.util.Date;

import lombok.Data;
@Data
public class StorePetApplyOrderVo {
	 /**
     * 宠物编号
     */
    private String petNo;
    /**
     * 预约类型
     */
    private Integer bookType;
    /**
     * 预约人
     */
    private String bookUser;
    /**
     * 预约电话
     */
    private String bookPhone;
    /**
     * 预约备注
     */
    private String bookRemark;
    /**
     * 预订查看时间
     */
    private Date seeDate;
    /**
     * 查看备注
     */
    private String seeRemark;
}
