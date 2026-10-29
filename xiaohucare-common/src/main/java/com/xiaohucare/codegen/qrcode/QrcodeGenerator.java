
package com.xiaohucare.codegen.qrcode;

import java.awt.image.BufferedImage;

import com.xiaohucare.codegen.Generator;

public interface QrcodeGenerator extends Generator {
	QrcodeConfig getQrcodeConfig();
	QrcodeGenerator generate(String content, String logoPath);
	QrcodeGenerator setBackgroundImage(String path);
	BufferedImage getImage(boolean clear);
	@Override
	default BufferedImage getImage() {
		return getImage(true);
	}

}
