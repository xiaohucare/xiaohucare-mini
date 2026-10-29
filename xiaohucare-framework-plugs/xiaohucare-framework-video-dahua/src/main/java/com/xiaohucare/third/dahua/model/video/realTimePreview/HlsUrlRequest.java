package com.xiaohucare.third.dahua.model.video.realTimePreview;

import lombok.Data;

/**
 * 封装实时拉流请求
 */
@Data
public class HlsUrlRequest {

  private Data data;

  @lombok.Data
  public static class Data{

    private String channelId;

    private String streamType;

    private String type;

  }

}
