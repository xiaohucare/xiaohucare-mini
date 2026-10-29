package com.xiaohucare.third.dahua.model.ipms.infoQuery;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-06-07 10:16
 * @Description: 车辆进出数据返回结果
 */
@Data
public class InAndOutRecordPageResponse extends IccResponse {
    private Data data;
    @lombok.Data
    public static class Data{
        /** 当前页 */
        private Integer currentPage;
        /** 每页条数 */
        private Integer pageSize;
        /** 总页数 */
        private Integer totalPage;
        /** 总条数 */
        private Integer totalRows;
        /** 分页数据 */
        private List<PageData> pageData;
        @lombok.Data
        public static class PageData {
            /** 车牌号 */
            private String carNum;
            /** 车辆停车状态，目前该字段已弃用 */
            private Integer carStatus;
            /** 车辆类型（0-外部车，其他为内部车） */
            private Integer carType;
            /** 车辆类型展示字段 */
            private String carTypeStr;
            /** 入场图片存放路径 */
            private String enterImg;
            /** 入场卡口相机通道编码 */
            private String enterItcDevChnid;
            /** 入场口相机通道名称 */
            private String enterItcDevChnname;
            /** 入场方式:0-刷卡 1-自动识别 */
            private String enterMode;
            /** 入场车牌图片存放路径 */
            private String enterNumImg;
            /** 入场道闸通道编码 */
            private String enterSluiceDevChnid;
            /** 入场道闸通道名称 */
            private String enterSluiceDevChnname;
            /** 入场时间 */
            private String enterTime;
            /** 入场时间字符串 */
            private String enterTimeStr;
            /** 入场车道号 */
            private String enterWayCode;
            /** 是否ETC用户(0:否 1：是) */
            private Integer etcType;
            /** 出场车牌号 */
            private String exitCarNum;
            /** 出场图片存放路径 */
            private String exitImg;
            /** 出场卡口相机通道编码 */
            private String exitItcDevChnid;
            /** 出场卡口相机通道名称 */
            private String exitItcDevChnname;
            /** 出场车牌图片，参考停车子系统方式 */
            private String exitNumImg;
            /** 出场道闸通道编码 */
            private String exitSluiceDevChnid;
            /** 出场道闸通道名称 */
            private String exitSluiceDevChnname;
            /** 出场时间 */
            private Long exitTime;
            /** 出场时间字符串 */
            private String exitTimeStr;
            /** 入场车道号 */
            private String exitWayCode;
            /** 数据库id */
            private String id;
            /** 入场抓拍图片，大图，参考停车子系统方式 */
            private String originalPicPathEnter;
            /** 出场抓拍图片，大图，参考停车子系统方式 */
            private String originalPicPathExit;
            /** 用户类别：0-临时用户；1-储值用户；2-月卡用户；3-长期用户 */
            private String ownerType;
            /** 停车场名称 */
            private String parkingLot;
            /** 停车场编码 */
            private String parkingLotCode;
            /** 停车收费金额 */
            private Float payedMoney;
            /** 入场抓拍图片小图，参考停车子系统方式 */
            private String realCapturePicPathEnter;
            /** 出场抓拍图片小图，参考停车子系统方式 */
            private String realCapturePicPathExit;
            /** 车标 */
            private String parkingCarLogo;
            /** 车身颜色,参考停车场-车身颜色 */
            private String parkingCarColor;

        }
    }
}
