package com.xiaohucare.third.dahua.model.video.realTimePreview;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

/**
 * 实时拉流返回
 */
@Data
public class RtspUrlResponse extends IccResponse {

  private RtspData data;

  @Data
  public static class RtspData {

    private String minRate;

    private String protocol;

    private String ip;

    private String port;

    private String stunEnable;

    private String stunPort;

    private String url;

    private String connectType;

    private String session;

    private String token;

    private String trackId;

  }
}
