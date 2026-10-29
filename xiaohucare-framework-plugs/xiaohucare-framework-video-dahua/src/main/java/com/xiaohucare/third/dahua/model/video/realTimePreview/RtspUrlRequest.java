package com.xiaohucare.third.dahua.model.video.realTimePreview;

import lombok.Data;

/**
 *实时拉流request
 */
@Data
public class RtspUrlRequest {

  private Data data;

  @lombok.Data
  public static class Data{

    private String channelId;

    private String dataType;

    private String streamType;
  }
}
