package com.xiaohucare.third.dahua.model.ipms.infoQuery;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-06-06 17:20
 * @Description: 统计车辆进出总数请求参数
 */
@Data
@Slf4j
public class InAndOutRecordCountRequest {
    /** 入口通道id，多个时用半角逗号隔开 */
    private String entranceChannelIds;
    /** 出口通道id，多个时用半角逗号隔开 */
    private String exitusChannelIds;
    /** 车辆类型 （1-内部车，0-外部车，字段不送则查询全部） */
    private Integer carType;
    /** 部门ID */
    private Long departmentId;
    /** 是否ETC用户(0:否 1：是) */
    private Integer etcType;
    /** 车主编码 */
    private String ownerCode;
    /** 出口总数 */
    private Integer exitusNum;
    /** 车主名称模糊查询 */
    private String ownerLikeStr;
    /** 所属场区编码(多个时半角逗号分隔) */
    private String parkingLotCodes;
    /** 进场车牌 */
    private String enterCarNumLikeStr;
    /** 出场车牌 */
    private String exitCarNumLikeStr;
    /** 进场时间起，格式：yyyy-MM-dd HH:mm:ss */
    private String enterTimeStrLeft;
    /** 进场时间止，格式：yyyy-MM-dd HH:mm:ss */
    private String enterTimeStrRight;
    /** 出场时间起，格式：yyyy-MM-dd HH:mm:ss */
    private String exitTimeStrLeft;
    /** 出场时间止，格式：yyyy-MM-dd HH:mm:ss */
    private String exitTimeStrRight;

    /**
     * 将参数连接到url末尾
     * @return
     */
    public String getUrlSuffix() {
        StringBuilder urlSuffix = new StringBuilder();
        urlSuffix.append("?");
        if(!StringUtils.isEmpty(entranceChannelIds)){
            urlSuffix.append("&entranceChannelIds="+entranceChannelIds);
        }
        if(!StringUtils.isEmpty(exitusChannelIds)){
            urlSuffix.append("&exitusChannelIds="+exitusChannelIds);
        }
        if(carType != null ){
            urlSuffix.append("&carType="+carType);
        }
        if(departmentId != null ){
            urlSuffix.append("&departmentId="+departmentId);
        }
        if(etcType != null ){
            urlSuffix.append("&etcType="+etcType);
        }
        if(!StringUtils.isEmpty(ownerCode)){
            urlSuffix.append("&ownerCode="+ownerCode);
        }
        if(exitusNum != null ){
            urlSuffix.append("&exitusNum="+exitusNum);
        }
        if(!StringUtils.isEmpty(ownerLikeStr)){
            urlSuffix.append("&ownerLikeStr="+ownerLikeStr);
        }
        if(!StringUtils.isEmpty(parkingLotCodes)){
            urlSuffix.append("&parkingLotCodes="+parkingLotCodes);
        }
        if(!StringUtils.isEmpty(enterCarNumLikeStr)){
            urlSuffix.append("&enterCarNumLikeStr="+enterCarNumLikeStr);
        }
        if(!StringUtils.isEmpty(exitCarNumLikeStr)){
            urlSuffix.append("&exitCarNumLikeStr="+exitCarNumLikeStr);
        }
        if(!StringUtils.isEmpty(enterTimeStrLeft)) {
            urlSuffix.append("&enterTimeStrLeft=" + enterTimeStrLeft);
        }
        if(!StringUtils.isEmpty(enterTimeStrRight)){
            urlSuffix.append("&enterTimeStrRight="+enterTimeStrRight);
        }
        if(!StringUtils.isEmpty(exitTimeStrLeft)){
            urlSuffix.append("&exitTimeStrLeft="+exitTimeStrLeft);
        }
        if(!StringUtils.isEmpty(exitTimeStrRight)){
            urlSuffix.append("&exitTimeStrRight="+exitTimeStrRight);
        }
//        try {
//            if(!StringUtils.isEmpty(enterTimeStrLeft)) {
//                urlSuffix.append("&enterTimeStrLeft=" + URLEncoder.encode(enterTimeStrLeft, "utf-8"));
//            }
//            if(!StringUtils.isEmpty(enterTimeStrRight)){
//                urlSuffix.append("&enterTimeStrRight="+URLEncoder.encode(enterTimeStrRight,"utf-8"));
//            }
//            if(!StringUtils.isEmpty(exitTimeStrLeft)){
//                urlSuffix.append("&exitTimeStrLeft="+URLEncoder.encode(exitTimeStrLeft,"utf-8"));
//            }
//            if(!StringUtils.isEmpty(exitTimeStrRight)){
//                urlSuffix.append("&exitTimeStrRight="+URLEncoder.encode(exitTimeStrRight,"utf-8"));
//            }
//        } catch (UnsupportedEncodingException e) {
//            log.error(e.getMessage());
//        }
        if(urlSuffix.length()>1){
            //去掉最开始?后面的&
            urlSuffix.deleteCharAt(1);
            return urlSuffix.toString();
        }else{
            return null;
        }
    }
}
