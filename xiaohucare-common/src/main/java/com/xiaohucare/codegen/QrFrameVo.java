package com.xiaohucare.codegen;

import java.io.Serializable;

public class QrFrameVo implements Serializable {
	private static final long serialVersionUID = 1L;
	int frameWidth = 400;
	int frameHeight = 400;
	String frameImage = "";
	String frameColor = "#ffffff";
	// 二维码位置x qr_frame_qr_x
	private Integer qrFrameQrX;
	// 二维码位置y
	private Integer qrFrameQrY;
	public QrFrameVo() {
	}
	public int getFrameWidth() {
		return frameWidth;
	}
	public QrFrameVo(int frameWidth, int frameHeight, String frameImage, String frameColor, Integer qrFrameQrX,
			Integer qrFrameQrY) {
		super();
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameImage = frameImage;
		this.frameColor = frameColor;
		this.qrFrameQrX = qrFrameQrX;
		this.qrFrameQrY = qrFrameQrY;
	}
	public QrFrameVo(int frameWidth, int frameHeight, String frameImage, String frameColor) {
		super();
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameImage = frameImage;
		this.frameColor = frameColor;
	}

	public Integer getQrFrameQrX() {
		return qrFrameQrX;
	}
	public void setQrFrameQrX(Integer qrFrameQrX) {
		this.qrFrameQrX = qrFrameQrX;
	}
	public Integer getQrFrameQrY() {
		return qrFrameQrY;
	}
	public void setQrFrameQrY(Integer qrFrameQrY) {
		this.qrFrameQrY = qrFrameQrY;
	}
	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public String getFrameColor() {
		return frameColor;
	}

	public void setFrameColor(String frameColor) {
		this.frameColor = frameColor;
	}

	public String getFrameImage() {
		return frameImage;
	}

	public void setFrameImage(String frameImage) {
		this.frameImage = frameImage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
