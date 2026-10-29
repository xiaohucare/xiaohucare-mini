package com.xiaohucare.third.dahua.model.attendance.infoQuery;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 * @author xiaohucare
 * @Date:2024-04-28 14:06
 * @Description: 打卡记录查询请求参数
 */
@Data
public class GetRecordPageRequest {
    /** 当前页 */
    private Integer pageNum;
    /** 每页记录数 */
    private Integer pageSize;
    /** 开始时间，格式: yyyy-MM-dd HH:mm:ss */
    private String actionTimeAfter;
    /** 结束时间，格式: yyyy-MM-dd HH:mm:ss */
    private String actionTimeBefore;
    /** 卡号 */
    private String cardNo;
    /** 考勤类型(0:刷卡考勤；1:人像考勤；2:指纹考勤) */
    private String attendanceType;
    /** 人员id集合 */
    private List<Long> personIds;
    /** 模糊查询参数（人员姓名） */
    private String personName;
    /** 模糊查询参数（人员编号） */
    private String personCode;
    /** 排序字段 */
    private String sort;
    /** DESC-降序, ASC-升序 */
    private String sortType;
}
