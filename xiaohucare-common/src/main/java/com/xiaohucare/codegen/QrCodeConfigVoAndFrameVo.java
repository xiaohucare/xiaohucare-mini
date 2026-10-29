package com.xiaohucare.codegen;

import java.io.Serializable;

import com.xiaohucare.codegen.qrcode.QrcodeConfig;

public class QrCodeConfigVoAndFrameVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String qrPreviewContent;
	private QrFrameVo qrFrameVo;
	private QrcodeConfig qrcodeConfig;
	
	public QrCodeConfigVoAndFrameVo() {	}
	
	public QrCodeConfigVoAndFrameVo(QrFrameVo qrFrameVo, QrcodeConfig qrcodeConfig,String qrPreviewContent) {
		super();
		this.qrFrameVo = qrFrameVo;
		this.qrcodeConfig = qrcodeConfig;
		this.qrPreviewContent=qrPreviewContent;
	}
	
	
	public QrFrameVo getQrFrameVo() {
		return qrFrameVo;
	}
	public void setQrFrameVo(QrFrameVo qrFrameVo) {
		this.qrFrameVo = qrFrameVo;
	}
	public QrcodeConfig getQrcodeConfig() {
		return qrcodeConfig;
	}
	public void setQrcodeConfig(QrcodeConfig qrcodeConfig) {
		this.qrcodeConfig = qrcodeConfig;
	}

	public String getQrPreviewContent() {
		return qrPreviewContent;
	}

	public void setQrPreviewContent(String qrPreviewContent) {
		this.qrPreviewContent = qrPreviewContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
