package com.xiaohucare.third.dahua.model.passengerflow.passengerflowConfig;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-05-08 13:56
 * @Description: 分页查询区域配置列表返回参数
 */
@Data
public class RegionConfigPageResponse extends IccResponse {

    private PageVO data;
    @Data
    public static class PageVO{
        /** 当前页 */
        private Integer currentPage;
        /** 是否启用云数据库，false-不启用，true-启用 */
        private Boolean enableCloudDB;
        /** 分页大小 */
        private Integer pageSize;
        /** 总页码 */
        private Integer totalPage;
        /** 总条数 */
        private Integer totalRows;
        /** 分页数据 */
        private List<RegionInfoVO> pageData;

        @Data
        public static class RegionInfoVO{
            /** 报警通道编码 */
            private String alarmChannelCode;
            /** 环比值 */
            private String chainRatio;
            /** 创建时间 */
            private Long createTime;
            /** 进入人数 */
            private Integer enterNumber;
            /** 超过上限报警时长 */
            private String exceedUpperTime;
            /** 主键ID */
            private Integer id;
            /** 少于下限报警时长 */
            private String lessLowerTime;
            /** 下限人数 */
            private Integer lowerPersonCount;
            /** 计划不符报警时长 */
            private String notPlanTime;
            /** 出去人数 */
            private Integer outNumber;
            /** 计划人数 */
            private Integer planPersonCount;
            /** 区域编码 */
            private Integer regionCode;
            /** 区域名称 */
            private String regionName;
            /** 区域类型 */
            private Integer regionType;
            /** 剩余人数 */
            private Integer remainder;
            /** 设备类型 1、门禁 5、通用客流 */
            private Integer devType;
            /** 统计模式 1、进出人数统计 2、区域内人数统计(景区才有) */
            private Integer statisticModel;
            /** 今日客流人数，字段弃用 */
            private Integer todayCount;
            /** 昨日客流人数，字段弃用 */
            private Integer yesterdayCount;
            /** 上限人数 */
            private Integer upPersonCount;
            /** 更新时间 */
            private Long updateTime;
        }
    }
}
