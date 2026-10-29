package com.xiaohucare.codegen;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.Version;
import com.xiaohucare.codegen.qrcode.LogoConfig;
import com.xiaohucare.codegen.qrcode.MultiFormatQreyesRenderer;
import com.xiaohucare.codegen.qrcode.QRCodeBitMatrix;
import com.xiaohucare.codegen.qrcode.QrcodeConfig;
import com.xiaohucare.codegen.qrcode.QrcodeWriter;
import com.xiaohucare.codegen.qrcode.QreyesFormat;
import com.xiaohucare.codegen.qrcode.QreyesPosition;
import com.xiaohucare.codegen.qrcode.QreyesRenderStrategy;
import com.xiaohucare.codegen.utils.FileUtils;
import com.xiaohucare.common.extra.tool.utils.ImageThumbnailatorUtil;

public class QrGenUtil extends AbstractGenerator {
	/**
	 * 
	 * @param qrcodeConfig
	 * @param content
	 * @param logoImageUrl
	 * @param qrFrameVo
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage genQrcodeAndFrame(QrcodeConfig qrcodeConfig, final String content,String logoImageUrl, 
			QrFrameVo qrFrameVo) throws IOException {
		BufferedImage qrcode = genQrcode(qrcodeConfig, content,logoImageUrl);
		if(qrFrameVo==null||qrFrameVo.getFrameImage()==null||qrFrameVo.getFrameImage()=="")return qrcode;
		BufferedImage frameBufferedImage = addFrame(qrcode, qrFrameVo);
		return frameBufferedImage;
	}
	/**
	 * 
	 * @param config
	 * @param content
	 * @param logoImageUrl
	 * @return
	 */
	public static BufferedImage genQrcode(QrcodeConfig config, final String content, String logoImageUrl) {
		try {
			QRCodeBitMatrix qrCodeData = new QrcodeWriter().encodeX(content, BarcodeFormat.QR_CODE, config.getWidth(),config.getHeight(), config.getHints());
			BufferedImage qrimage = toQrBufferedImage(qrCodeData, config);
			qrimage = ImageThumbnailatorUtil.ZoomPicByWidth(qrimage, config.getWidth());
			/**
			 * render image margin
			 */
			qrimage = setRadius(qrimage, config.getBorderRadius(), config.getBorderSize(), config.getBorderColor(),
					config.getBorderStyle(), config.getBorderDashGranularity(), config.getMargin());
			/**
			 * insert logo in the middle of the image
			 */
			if (logoImageUrl != null && logoImageUrl.length() > 0) {
				BufferedImage logoImage = FileUtils.getImageByUrl(logoImageUrl);
				addLogo(qrimage, logoImage, config.getLogoConfig());
			}
			return qrimage;
		} catch (Exception e) {
			throw new QrcodeGenerateException(e);
		}
	}

	/**
	 * 添加中间LOGO 图片
	 * 
	 * @param image
	 * @param logoImage
	 * @param config
	 * @throws IOException
	 */
	public static void addLogo(final BufferedImage qrimage, final BufferedImage logoImage, final LogoConfig logoConfig)
			throws IOException {
		BufferedImage srcImage = qrimage;
		if (logoImage == null) {
			return;
		}
		// 3、画笔对象
		Graphics2D graphics = srcImage.createGraphics();
		/**
		 * get logo width & height
		 */
		final int ratio = logoConfig.getRatio();
		int ratioWidthOfCodeImage = srcImage.getWidth() / ratio;
		int ratioHeightOfCodeImage = srcImage.getHeight() / ratio;
		int width = logoImage.getWidth() > ratioWidthOfCodeImage ? ratioWidthOfCodeImage : logoImage.getWidth();
		int height = logoImage.getHeight() > ratioHeightOfCodeImage ? ratioHeightOfCodeImage : logoImage.getHeight();
		/**
		 * get logo panel position
		 */
		int padding = logoConfig.getPadding() * 2;
		int margin = logoConfig.getMargin() * 2;
		int w = width + padding + margin, h = height + padding + margin;
		int positionX = (srcImage.getWidth() - w) / 2;
		int positionY = (srcImage.getHeight() - h) / 2;
		
		boolean logoBg=logoConfig.isBackgroundColorIsTransparent();
		Shape shape =null;
		/**
		 * render panel, 1px offset is added to ensure center position
		 */
		shape = new RoundRectangle2D.Float(positionX, positionY, (float) (w + 1), (float) (h + 1),
				logoConfig.getPanelArcWidth(), logoConfig.getPanelArcHeight());
		if (!logoBg) {
			graphics.setColor(getColor(logoConfig.getPanelColor()));
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.fill(shape);
		}
		/**
		 * get logo position
		 */
		positionX += logoConfig.getMargin();
		positionY += logoConfig.getMargin();
		/**
		 * render logo background
		 */
		if (!logoBg) {
			shape = new RoundRectangle2D.Float(positionX, positionY, width + padding, height + padding,
					logoConfig.getArcWidth(), logoConfig.getArcHeight());
			graphics.setColor(getColor(logoConfig.getBackgroundColor()));
			graphics.fill(shape);
		} 
		
		positionX += logoConfig.getPadding();
		positionY += logoConfig.getPadding();
		// 透明度
		float alpha = 0.5f;
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

		graphics.drawImage(logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), positionX, positionY, null);
		if(!logoBg) {
			/**
			 * border
			 */
			graphics.setStroke(new BasicStroke(logoConfig.getBorderSize()));
			graphics.setColor(getColor(logoConfig.getBorderColor()));
			/**
			 * anti-aliasing
			 */
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if(shape!=null)graphics.draw(shape);
		}
		
		/**
		 * flush
		 */
		graphics.dispose();
		logoImage.flush();
		srcImage.flush();
	}

	/**
	 * 添加外框
	 * @param qrimage
	 * @param qrFrameVo
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage addFrame(final BufferedImage qrimage, QrFrameVo qrFrameVo) throws IOException {
		if (qrFrameVo == null) {
			return qrimage;
		}
		BufferedImage frameImage = null;
		if (qrFrameVo.getFrameImage() != null && qrFrameVo.getFrameImage().length() > 0) {
			   frameImage = FileUtils.getImageByUrl(qrFrameVo.getFrameImage());
		}else {
			return qrimage;
		}
		return addFrame(qrimage, qrFrameVo.getFrameWidth(), qrFrameVo.getFrameHeight(), frameImage,
				qrFrameVo.getFrameColor(),qrFrameVo.getQrFrameQrX(),qrFrameVo.getQrFrameQrY());
	}
	/**
	 * 添加外框
	 * @param qrimage
	 * @param frameWidth
	 * @param frameHeight
	 * @param frameImage
	 * @param frameColor
	 * @return
	 */
	public static BufferedImage addFrame(final BufferedImage qrimage, final int frameWidth, final int frameHeight,
			final BufferedImage frameImage, final String frameColor,Integer qrFrameQrX, Integer qrFrameQrY) {
		// 初始化图片
		BufferedImage frameimage = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gd = frameimage.createGraphics();
		if (frameColor != null) {
			gd.setColor(getColor(frameColor)); // 设置画笔颜色
			gd.fillRect(0, 0, frameWidth, frameHeight); // 画填充矩形
		}
		// 画图
		if (frameImage != null)
			gd.drawImage(frameImage.getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH), 0, 0, null);
		int qrw = qrimage.getWidth();
		int qrh = qrimage.getHeight();
		int qrx = (frameWidth - qrw) / 2;
		int qry = (frameHeight - qrh) / 2;
		if (qrx < 0)
			qrx = 0;
		if (qry < 0)
			qry = 0;
		if(qrFrameQrX!=null&&qrFrameQrX>-1) {
			qrx=qrFrameQrX;
		}
		if(qrFrameQrY!=null&&qrFrameQrY>-1) {
			qry=qrFrameQrY;
		}
		gd.drawImage(qrimage, qrx, qry, null);
		gd.dispose();
		qrimage.flush();
		if (frameImage != null)
			frameImage.flush();
		return frameimage;
	}
	/**
	 * 画 二维码
	 * 
	 * @param matrix
	 * @param config
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage toQrBufferedImage(final QRCodeBitMatrix matrix, final QrcodeConfig config)
			throws IOException {

		BitMatrix bitMatrix = matrix.getBitMatrix();
		Version version = matrix.getQrcode().getVersion();
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		/**
		 * Formula for calculating number of modules on each side: (Version - 1) * 4 +
		 * 21.
		 */
		int modules = (version.getVersionNumber() - 1) * 4 + 21;
		int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();

		QreyesPosition position = new QreyesPosition(modules, topLeftOnBit);

		int moduleHeight = position.getModuleHeight(height);
		int moduleWidth = position.getModuleWidth(width);
		/**
		 * Calculating codeEyes position.
		 */
		int leftStartX = topLeftOnBit[0] + moduleWidth * QreyesRenderStrategy.POINT_BORDER.getStart();
		int leftEndX = topLeftOnBit[0] + moduleWidth * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int topStartY = topLeftOnBit[1] + moduleHeight * QreyesRenderStrategy.POINT_BORDER.getStart();
		int topEndY = topLeftOnBit[1] + moduleHeight * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int rightStartX = topLeftOnBit[0] + moduleWidth * (modules - QreyesRenderStrategy.POINT_BORDER.getEnd());
		int rightEndX = width - topLeftOnBit[0] - moduleWidth * QreyesRenderStrategy.POINT_BORDER.getStart();
		int bottomStartY = height - topLeftOnBit[1] - moduleHeight * QreyesRenderStrategy.POINT_BORDER.getEnd();
		int bottomEndY = height - topLeftOnBit[1] - moduleHeight * QreyesRenderStrategy.POINT_BORDER.getStart();

		/**
		 * Build image.
		 */
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// custom color.
		int masterColor = getColor(config.getMasterColor()).getRGB();
		int slaveColor = getColor(config.getSlaveColor()).getRGB();
		BufferedImage backgroundImage = null;
		BufferedImage masterColorImage = null;
		/**
		 * insert bg in the middle of the image
		 */
		if (config.getBackgroundImage() != null && config.getBackgroundImage().length() > 0) {
			backgroundImage = FileUtils.getImageByUrl(config.getBackgroundImage());
			backgroundImage = ImageThumbnailatorUtil.ZoomPicByWidth(backgroundImage, width);
		}

		if (config.getMasterColorImage() != null && config.getMasterColorImage().length() > 0) {
			masterColorImage = FileUtils.getImageByUrl(config.getMasterColorImage());
			masterColorImage = ImageThumbnailatorUtil.ZoomPicByWidth(masterColorImage, width);
		}
		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {
				// top left
				if (x >= leftStartX && x < leftEndX && y >= topStartY && y < topEndY) {
				}
				// top right
				else if (x >= rightStartX && x < rightEndX && y >= topStartY && y < topEndY) {
				}
				// bottom left
				else if (x >= leftStartX && x < leftEndX && y >= bottomStartY && y < bottomEndY) {
				}
				// non codeEyes region
				else {
					if (backgroundImage != null) {
						int orgW = backgroundImage.getWidth();
						int orgH = backgroundImage.getHeight();
						int WF = (width - backgroundImage.getWidth()) / 2;
						int HF = (height - backgroundImage.getHeight()) / 2;
						int colorX = x - WF;
						int colorY = y - HF;

						if (colorX >= 0 && colorX < orgW && colorY >= 0 && colorY < orgH) {
							slaveColor = backgroundImage.getRGB(colorX, colorY);
						}
					}
					if (masterColorImage != null) {
						int orgW = masterColorImage.getWidth();
						int orgH = masterColorImage.getHeight();
						int WF = (width - masterColorImage.getWidth()) / 2;
						int HF = (height - masterColorImage.getHeight()) / 2;
						int colorX = x - WF;
						int colorY = y - HF;
						if (colorX >= 0 && colorX < orgW && colorY >= 0 && colorY < orgH) {
							masterColor = masterColorImage.getRGB(colorX, colorY);
						} else {
							masterColor = getColor(config.getMasterColor()).getRGB();
						}
					}
					image.setRGB(x, y, bitMatrix.get(x, y) ? masterColor : slaveColor);
				}
			}
		}
		position.setPosition(leftStartX, leftEndX, topStartY, topEndY, rightStartX, rightEndX, bottomStartY, bottomEndY);
		Color border = getColor(config.getCodeEyesBorderColor());
		Color point = getColor(config.getCodeEyesPointColor());
		QreyesFormat format = config.getCodeEyesFormat();
		new MultiFormatQreyesRenderer().render(image, format, position, new Color(slaveColor), border, point);
		return image;
	}

	public static boolean toFile(BufferedImage frameimage, String pahtname) throws IOException {
		return ImageIO.write(frameimage, Codectx.IMAGE_TYPE, new File(pahtname));
	}

	public boolean toStream(BufferedImage frameimage, OutputStream output) throws IOException {
		return ImageIO.write(frameimage, Codectx.IMAGE_TYPE, output);
	}
}
