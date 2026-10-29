package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-16 13:59
 * @Description: 根据任务获取搜索结果请求参数
 */
@Data
public class GetTaskResultRequest {
    /** 搜索库类型 1抓拍库 2人像库 */
    private Integer searchGroupType;
    /** 页码 */
    private Integer pageNum;
    /** 每页大小 */
    private Integer pageSize;
    /** 排序方式1-按相似度从高到底,2-按相似度从底到高,3-按时间正序,4-按时间倒序 备注:当searchGroupType=2,只有按相似度排序。 */
    private Integer ordered;
    /** 搜索结果的流水ID */
    private String sessionId;
}
