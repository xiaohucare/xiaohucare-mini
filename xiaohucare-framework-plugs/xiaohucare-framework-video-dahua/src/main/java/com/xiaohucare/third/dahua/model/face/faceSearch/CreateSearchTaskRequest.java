package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 13:38
 * @Description: 人像以图搜图创建搜索任务请求参数
 */
@Data
public class CreateSearchTaskRequest {
    /** 搜索库类型 1-抓拍库，2-注册库 */
    private Integer searchGroupType;
    /** 图片数据,base64图片流 */
    private String base64Img;
    /** 最小相似度 正整数：60-100 */
    private Integer threshold;
    /** 智能设备类型	1-编码器类型，例如：IVSS、IVD、MC8000，2-CV */
    private Integer devType;

    /** 抓拍库检索参数 */
    /** 开始时间 格式:yyyy-MM-dd HH:mm:ss */
    private String startTime;
    /** 结束时间 格式:yyyy-MM-dd HH:mm:ss */
    private String endTime;
    /** 设备通道 key:设备编码，数字、字母、@字符；value:通道编码，不允许为空， 数字、字母、# */
    private Map<String, List<String>> deviceChannels;
    /** 1-按相似度从高到底，2-按相似度从底到高，3-按时间正序，4-按时间倒序 */
    private Integer ordered;

    /** 注册库检索参数 */
    /** 设备编码，数字、字母、@字符 */
    private List<String> deviceCodes;
    /** 平台人像库ID */
    private List<String> groupIds;

}
