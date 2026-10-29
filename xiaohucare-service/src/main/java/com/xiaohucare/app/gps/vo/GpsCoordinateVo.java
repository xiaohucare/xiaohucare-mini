package com.xiaohucare.app.gps.vo;

import com.xiaohucare.framework.annotation.Excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GpsCoordinateVo {
	/**
	 * 中心点纬度
	 */
	@Schema(description = "纬度")
	@Excel(name = "纬度")
	private Double lat;
	/**
	 * 中心点经度
	 */
	@Schema(description = "经度")
	@Excel(name = "经度")
	private Double lng;
}
