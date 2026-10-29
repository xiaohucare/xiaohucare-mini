package com.xiaohucare.codegen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.xiaohucare.codegen.qrcode.QrcodeConfig;
import com.xiaohucare.codegen.qrcode.ThreadQrcode;

public class QrGen {
	private static final ThreadQrcode QRCODE = new ThreadQrcode();
	private final QrcodeConfig qrcodeConfig;
	public QrGen() {
		this(new QrcodeConfig());
	}
	public QrGen(QrcodeConfig qrcodeConfig) {
		super();
		this.qrcodeConfig = qrcodeConfig;
	}
	public QrcodeConfig getQrcodeConfig() {
		return this.qrcodeConfig;
	}
	
	public QrGen generate(String content) {
		QRCODE.setImage(QrGenUtil.genQrcode(qrcodeConfig, content,null));
		return this;
	}
	
	public QrGen generate(String content, String logoPath) {
		QRCODE.setImage(QrGenUtil.genQrcode(qrcodeConfig, content, logoPath));
		return this;
	}
	public QrGen generateAndFrame(String content, String logoPath,QrFrameVo qrFrameVo) throws IOException {
		QRCODE.setImage(QrGenUtil.genQrcodeAndFrame(qrcodeConfig, content, logoPath,qrFrameVo));
		return this;
	}
	
	public boolean toFile(String pathname) throws IOException {
		try {
			return ImageIO.write(QRCODE.getImage(), Codectx.IMAGE_TYPE, new File(pathname));
		} finally {
			clear();
		}
	}
	public boolean toStream(OutputStream output) throws IOException {
		try {
			return ImageIO.write(QRCODE.getImage(), Codectx.IMAGE_TYPE, output);
		} finally {
			clear();
		}
	}
	public BufferedImage getImage(boolean clear) {
		try {
			return QRCODE.getImage();
		} finally {
			if (clear) {
				clear();
			}
		}
	}
	public void clear() {
		QRCODE.remove();
	}
	

}
