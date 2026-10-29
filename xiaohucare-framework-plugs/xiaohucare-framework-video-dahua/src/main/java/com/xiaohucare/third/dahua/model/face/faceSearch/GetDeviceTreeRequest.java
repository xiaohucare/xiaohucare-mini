package com.xiaohucare.third.dahua.model.face.faceSearch;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-04-15 15:15
 * @Description: 请求参数
 */
@Data
public class GetDeviceTreeRequest {
    /** 设备类型,查询设备编码：001;1,62 查询通道编码：001;00_1;1 */
    private String type;
    /** 组织编码 */
    private String id="001";
    /** 上级组织/设备节点是否有权限 */
    private String checkStat;
    /** 当前页 */
    private String curCount;
    /** 搜索关键字 */
    private String searchKey;
    /** 通道能力集 */
    private String capacity;
    /** 默认为0,表示本域 */
    private Integer isDomain=0;

    /**
     * 获取URL后缀
     *
     * @return URL后缀
     * @throws BusinessException 业务异常
     */
    public String getUrlSuffix() {
        StringBuilder urlSuffix = new StringBuilder();
        urlSuffix.append("?");
        if (!StringUtils.isEmpty(type)) {
            urlSuffix.append("type=" + type);
        }
        if (!StringUtils.isEmpty(checkStat)) {
            urlSuffix.append("&id=" + id);
        }
        if (!StringUtils.isEmpty(checkStat)) {
            urlSuffix.append("&checkStat" + checkStat);
        }
        if (!StringUtils.isEmpty(curCount)) {
            urlSuffix.append("&curCount=" + curCount);
        }
        if (!StringUtils.isEmpty(searchKey)) {
            urlSuffix.append("&searchKey=" + searchKey);
        }
        if (!StringUtils.isEmpty(capacity)) {
            urlSuffix.append("&capacity=" + capacity);
        }
        if (isDomain != null) {
            urlSuffix.append("&isDomain=" + isDomain);
        }
        return urlSuffix.toString();
    }
}
