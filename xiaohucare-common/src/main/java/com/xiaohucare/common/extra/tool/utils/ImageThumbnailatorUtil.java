package com.xiaohucare.common.extra.tool.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 
 * @author xiaohucare
 * 
 */
public class ImageThumbnailatorUtil{

	/**
	 * 指定大小进行缩放
	 *
	 * size(width,height) 若图片横比200小，高比300小，不变
	 * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
	 * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
	 * keepAspectRatio(false) 默认是按照比例缩放的
	 * 不按照比例，指定大小进行缩放
	 * @throws IOException
	 */
	public static void CompressPicBySize(String orgImage,String outImage,int width,int height,boolean isRatio) throws Exception {
		try {
			File file=new File(orgImage);
			if(!file.exists()){
				return;
			}
			
			Thumbnails.of(orgImage).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
		} catch (Exception e) {
		}
	}
 public static void CompressPicBySize(String orgImage,String outImage,int width,int height,boolean isRatio,String wareImage,Position position){
	try {	
	    File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
        if(wareImage!=null&&new File(wareImage).exists()){
        	if(position==null)position=Positions.BOTTOM_RIGHT;
        	Thumbnails.of(orgImage).watermark(position,ImageIO.read(new File(wareImage)), 1f).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
        }else{
        	Thumbnails.of(orgImage).size(width, height).keepAspectRatio(isRatio).toFile(
					outImage);	
        }
		} catch (Exception e) {
	 }		
	}
	
	public static void CompressPicBySize(String orgImage,String outImage,int width,boolean isRatio){
		BufferedImage tempImage;
		try {
			tempImage = ImageIO.read(new File(orgImage));
			int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
				Thumbnails.of(orgImage).scale(scale).toFile(outImage);
			}else
				Thumbnails.of(orgImage).scale(scale).toFile(outImage);
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}
	public static void CompressPicBySize(String orgImage,String outImage,int width,boolean isRatio,String wareImage,Position position){
		try {	
		    File file=new File(orgImage);
			if(!file.exists()){
				return;
			}
			BufferedImage tempImage = ImageIO.read(new File(orgImage));  
			int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
			}
	        if(wareImage!=null&&new File(wareImage).exists()){
	        	if(position==null)position=Positions.BOTTOM_RIGHT;
	        	Thumbnails.of(tempImage).watermark(position,ImageIO.read(new File(wareImage)), 1f).scale(scale).toFile(
						outImage);	
	        }else{
	        	Thumbnails.of(tempImage).scale(scale).toFile(outImage);
	        }
			} catch (Exception e) {
		 }		
   }
	public static void CompressPicUrlBySize(String orgImageUrl,String outImage,int width,boolean isRatio,String wareImage,Position position){
		try {	
			BufferedImage tempImage = ImageIO.read(new URL(orgImageUrl));  
			int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
			}
			Thumbnails.of(tempImage).scale(scale).toFile(outImage);
			} catch (Exception e) {
		 }		
   }
	public static void CompressPicUrlBySize(String orgImageUrl,File outImageFile,int width,boolean isRatio,String wareImage,Position position){
		try {	
			BufferedImage tempImage = ImageIO.read(new URL(orgImageUrl));  
			int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
			}
			Thumbnails.of(tempImage).scale(scale).toFile(outImageFile);
		} catch (Exception e) {
		}		
	}
	
	public static void CropPicBySize(String orgImage,String outImage,int width,int height,String wareImage,Position position) throws IOException {
		
	    File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
		BufferedImage tempImage = ImageIO.read(file);  
		    int orgW = tempImage.getWidth();
	        int orgH = tempImage.getHeight();
	        
	        int hfW=(orgW-width)/2;
	        int hfH=(orgH-height)/2;
	        
	        if(orgH<height){
	        	height=orgH;
	        	hfH=0;
	        }
	        if(orgW<width){
	        	width=orgW;
	        	hfW=0;
	        }
	        
	        if(wareImage!=null&&new File(wareImage).exists()){
	        	if(position==null)position=Positions.BOTTOM_RIGHT;
	        	Thumbnails.of(orgImage).watermark(position,ImageIO.read(new File(wareImage)), 0.5f).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);;		
	        }else{
	        	Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);;		
	        }
				
	}
	public static void CropPicBySize(String orgImage,String outImage,int width,int height) throws IOException {
		File file=new File(orgImage);
		if(!file.exists()){
			return;
		}
		BufferedImage tempImage = ImageIO.read(file);  
	    int orgW = tempImage.getWidth();
        int orgH = tempImage.getHeight();
        
        int hfW=(orgW-width)/2;
        int hfH=(orgH-height)/2;
        
        if(orgH<height){
        	height=orgH;
        	hfH=0;
        }
        if(orgW<width){
        	width=orgW;
        	hfW=0;
        }
        Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).toFile(outImage);
	}
	
	public static BufferedImage CropPicBySize(String orgImage,int width,int height) throws IOException {
	        
		File file=new File(orgImage);
		if(!file.exists()){
			return null;
		}
		BufferedImage tempImage = ImageIO.read(file);
		    int orgW = tempImage.getWidth();
	        int orgH = tempImage.getHeight();
	        
	        if(orgH<height){
	        	height=orgH;
	        }
	        if(orgW<width){
	        	width=orgW;
	        }
	        int imageWidth = tempImage.getWidth();  
			double scale=1;
			if(width<imageWidth){
				scale=(double)width/imageWidth;
				if(scale<=0.1){
					scale=0.1;
				}
				return Thumbnails.of(orgImage).scale(scale).asBufferedImage();
			}else
				return Thumbnails.of(orgImage).scale(scale).asBufferedImage();
//	      return Thumbnails.of(orgImage).sourceRegion(0+hfW, 0+hfH, width+hfW, height+hfH).scale(1).asBufferedImage();
	}
	
	public static BufferedImage CropPicBySize(BufferedImage tempImage,int width,int height) throws IOException {
		if(tempImage==null) {
			return tempImage;
		}
		int orgW = tempImage.getWidth();
		if(orgW<width){
			width=orgW;
		}
		int imageWidth = tempImage.getWidth();  
		double scale=1;
		if(width<imageWidth){
			scale=(double)width/imageWidth;
			if(scale<=0.1){
				scale=0.1;
			}
			return Thumbnails.of(tempImage).scale(scale).asBufferedImage();
		}else
			return Thumbnails.of(tempImage).scale(scale).asBufferedImage();
	}
	
	public static BufferedImage ZoomPicByWidth(BufferedImage tempImage,int width) throws IOException {
		if(tempImage==null) {
			return tempImage;
		}
		int imageWidth = tempImage.getWidth();  
		if(imageWidth<=1) {
			return tempImage;
		}
		double scale=(double)width/imageWidth;
		return Thumbnails.of(tempImage).scale(scale).asBufferedImage();
	}
	
	/**
	 * 截取图像模块
	 * @param tempImage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
  public static BufferedImage CropPicBySize(BufferedImage tempImage,int x,int y,int width,int height) throws IOException {
	      return Thumbnails.of(tempImage).sourceRegion(x, y, width, height).scale(1).asBufferedImage();
	}
	
	/**
	 * 按照比例进行缩放
	 * scale(比例) eg:0.25f
	 * @throws IOException
	 */
	public static void CompressPicByScale(String orgImage,String outImage,double scale) throws IOException {
		Thumbnails.of(orgImage).scale(scale).toFile(outImage);
	}

	/**
	 * 旋转
	 *  rotate(角度),正数：顺时针 负数：逆时针
	 * @throws IOException
	 */
	public static void CompressPicByLocal(String orgImage,String outImage,int rotate) throws IOException {
		Thumbnails.of(orgImage).rotate(rotate).toFile(outImage);
	}

	/**
	 * 水印
	 * watermark(位置(Positions.BOTTOM_RIGHT)，水印图，透明度 0.5f)
	 * Positions.CENTER,
	 * @throws IOException
	 */
	public static void CompressPicByWare(String orgImage,String outImage,String wareImage,Position position) throws Exception {
		File orgImageFile=new File(orgImage);
		if (orgImageFile.length()>1*1024*1024) {
			CompressPicBySize(orgImage, outImage, 800, 1024, true);
		}
		
		if(wareImage!=null&&new File(wareImage).exists()){
			Thumbnails.of(orgImage).watermark(
					position,
					ImageIO.read(new File(wareImage)), 0.5f)
					.outputQuality(0.8f).scale(1).toFile(outImage);
		}else{
			CompressPicBySize(orgImage, outImage, 800, 1024, true);
		}
		
	}
	public static void CompressPicByHeight(String orgImage,String outImage,int height,boolean isRatio){
		BufferedImage tempImage;
		try {
			tempImage = ImageIO.read(new File(orgImage));
			int imageHeight = tempImage.getHeight();  
			double scale=1;
			if(height<imageHeight){
				scale=(double)height/imageHeight;
				if(scale<=0.1){
					scale=0.1;
				}
				Thumbnails.of(orgImage).scale(scale).toFile(outImage);
			}else
				Thumbnails.of(orgImage).scale(scale).toFile(outImage);
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}
	
	/**
	 * 添加文字水印
	 * @param orgImage 原图路径
	 * @param outImage 输出图片路径
	 * @param watermarkText 水印文字
	 * @param fontSize 字体大小
	 * @param multiLine 是否多行显示（true:多行，false:单行）
	 * @param lineSpacing 行间隔（像素）
	 * @param angle 倾斜角度（弧度，0 表示不倾斜）
	 * @param position 水印位置
	 * @param color 文字颜色
	 * @param opacity 透明度（0.0-1.0）
	 * @throws Exception
	 */
	public static void addTextWatermark(String orgImage, String outImage, String watermarkText, 
			int fontSize, boolean multiLine, int lineSpacing, double angle, 
			Position position, Color color, float opacity) throws Exception {
		
		File file = new File(orgImage);
		if (!file.exists()) {
			return;
		}
		BufferedImage originalImage = ImageIO.read(file);
		if (originalImage == null) {
			return;
		}
		BufferedImage watermarkedImage = createCanvas(originalImage, outImage);
		Graphics2D g2d = buildGraphics2D(watermarkedImage, originalImage);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
		drawTextAtPosition(g2d, watermarkedImage, font, watermarkText,
				fontSize, multiLine, lineSpacing, angle, position, color, opacity);
		g2d.dispose();
		saveImage(watermarkedImage, outImage);
	}

	/**
	 * 多位置文字水印：在图片的多个预设位置分别添加相同文字水印
	 * @param orgImage      原图路径
	 * @param outImage      输出图片路径
	 * @param watermarkText 水印文字
	 * @param fontSize      字体大小
	 * @param multiLine     是否多行/竖排（true：无\n则每字符单独一行；有\n则按行分割）
	 * @param lineSpacing   行间距（像素）
	 * @param angle         倾斜角度（弧度，0 表示不倾斜）
	 * @param positions     水印位置列表，传入多个 Positions.xxx
	 * @param color         文字颜色
	 * @param opacity       透明度（0.0-1.0）
	 * @throws Exception
	 */
	public static void addTextWatermark(String orgImage, String outImage, String watermarkText,
			int fontSize, boolean multiLine, int lineSpacing, double angle,
			List<Position> positions, Color color, float opacity) throws Exception {

		File file = new File(orgImage);
		if (!file.exists()) {
			return;
		}
		BufferedImage originalImage = ImageIO.read(file);
		if (originalImage == null) {
			return;
		}
		BufferedImage watermarkedImage = createCanvas(originalImage, outImage);
		Graphics2D g2d = buildGraphics2D(watermarkedImage, originalImage);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
		if (positions != null) {
			for (Position position : positions) {
				drawTextAtPosition(g2d, watermarkedImage, font, watermarkText,
						fontSize, multiLine, lineSpacing, angle, position, color, opacity);
			}
		}
		g2d.dispose();
		saveImage(watermarkedImage, outImage);
	}

	/**
	 * 平铺文字水印：按行列间距在整张图片上平铺水印文字
	 * @param orgImage      原图路径
	 * @param outImage      输出图片路径
	 * @param watermarkText 水印文字
	 * @param fontSize      字体大小
	 * @param angle         倾斜角度（弧度）
	 * @param xSpacing      水平间距（每个水印块之间的水平像素距离）
	 * @param ySpacing      垂直间距（每个水印块之间的垂直像素距离）
	 * @param color         文字颜色
	 * @param opacity       透明度（0.0-1.0）
	 * @throws Exception
	 */
	public static void addTextWatermarkTile(String orgImage, String outImage, String watermarkText,
			int fontSize, double angle, int xSpacing, int ySpacing,
			Color color, float opacity) throws Exception {

		File file = new File(orgImage);
		if (!file.exists()) {
			return;
		}
		BufferedImage originalImage = ImageIO.read(file);
		if (originalImage == null) {
			return;
		}
		int imgW = originalImage.getWidth();
		int imgH = originalImage.getHeight();
		BufferedImage watermarkedImage = createCanvas(originalImage, outImage);
		Graphics2D g2d = buildGraphics2D(watermarkedImage, originalImage);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int textW = fm.stringWidth(watermarkText);
		int textH = fontSize;
		// 水印块宽高 = 文字尺寸 + 间距
		int blockW = textW + xSpacing;
		int blockH = textH + ySpacing;
		g2d.setColor(color);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
		for (int row = 0; row * blockH < imgH + blockH; row++) {
			for (int col = 0; col * blockW < imgW + blockW; col++) {
				int x = col * blockW;
				int y = row * blockH + textH;
				if (angle != 0) {
					int cx = x + textW / 2;
					int cy = y - textH / 2;
					g2d.rotate(angle, cx, cy);
					g2d.drawString(watermarkText, x, y);
					g2d.rotate(-angle, cx, cy);
				} else {
					g2d.drawString(watermarkText, x, y);
				}
			}
		}
		g2d.dispose();
		saveImage(watermarkedImage, outImage);
	}

	// ---- 内部辅助方法 ----

	/**
	 * 根据输出路径判断格式，创建画布并绘制原图
	 */
	private static BufferedImage createCanvas(BufferedImage originalImage, String outImage) {
		String outLower = outImage.toLowerCase();
		boolean isPng = outLower.endsWith(".png");
		int imageType = isPng ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
		return new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), imageType);
	}

	/**
	 * 创建并初始化 Graphics2D，绘制原图底层
	 */
	private static Graphics2D buildGraphics2D(BufferedImage canvas, BufferedImage original) {
		Graphics2D g2d = canvas.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawImage(original, 0, 0, null);
		return g2d;
	}

	/**
	 * 在指定位置绘制文字水印（单位置核心绘制逻辑）
	 */
	private static void drawTextAtPosition(Graphics2D g2d, BufferedImage canvas, Font font,
			String watermarkText, int fontSize, boolean multiLine, int lineSpacing,
			double angle, Position position, Color color, float opacity) {

		int imgW = canvas.getWidth();
		int imgH = canvas.getHeight();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(color);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));

		if (multiLine) {
			// 多行/竖排：有 \n 按行分割，否则每字符单行
			String[] lines;
			if (watermarkText.contains("\n")) {
				lines = watermarkText.split("\n");
			} else {
				lines = watermarkText.codePoints()
						.mapToObj(cp -> new String(Character.toChars(cp)))
						.toArray(String[]::new);
			}
			int lineH = fontSize + lineSpacing;
			int totalHeight = lines.length * fontSize + (lines.length - 1) * lineSpacing;
			int startY;
			if (position == Positions.TOP_LEFT || position == Positions.TOP_RIGHT || position == Positions.TOP_CENTER) {
				startY = fontSize + 20;
			} else if (position == Positions.CENTER) {
				startY = (imgH - totalHeight) / 2 + fontSize;
			} else {
				startY = imgH - 20 - (lines.length - 1) * lineH;
			}
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				int lineW = fm.stringWidth(line);
				int lineX;
				if (position == Positions.BOTTOM_RIGHT || position == Positions.TOP_RIGHT) {
					lineX = imgW - lineW - 20;
				} else if (position == Positions.BOTTOM_LEFT || position == Positions.TOP_LEFT) {
					lineX = 20;
				} else {
					lineX = (imgW - lineW) / 2;
				}
				int lineY = startY + i * lineH;
				int cx = lineX + lineW / 2;
				int cy = lineY - fm.getAscent() / 2;
				if (angle != 0) {
					g2d.rotate(angle, cx, cy);
					g2d.drawString(line, lineX, lineY);
					g2d.rotate(-angle, cx, cy);
				} else {
					g2d.drawString(line, lineX, lineY);
				}
			}
		} else {
			// 单行
			int textW = fm.stringWidth(watermarkText);
			int x, y;
			if (position == Positions.BOTTOM_RIGHT) {
				x = imgW - textW - 20; y = imgH - 20;
			} else if (position == Positions.BOTTOM_LEFT) {
				x = 20; y = imgH - 20;
			} else if (position == Positions.TOP_RIGHT) {
				x = imgW - textW - 20; y = fontSize + 20;
			} else if (position == Positions.TOP_LEFT) {
				x = 20; y = fontSize + 20;
			} else if (position == Positions.CENTER) {
				x = (imgW - textW) / 2; y = (imgH + fontSize) / 2;
			} else {
				x = imgW - textW - 20; y = imgH - 20;
			}
			if (angle != 0) {
				int cx = x + textW / 2;
				int cy = y - fontSize / 2;
				g2d.rotate(angle, cx, cy);
				g2d.drawString(watermarkText, x, y);
				g2d.rotate(-angle, cx, cy);
			} else {
				g2d.drawString(watermarkText, x, y);
			}
		}
	}

	/**
	 * 将 BufferedImage 保存到指定路径，自动识别格式
	 */
	private static void saveImage(BufferedImage image, String outImage) throws IOException {
		String outLower = outImage.toLowerCase();
		String format = "jpeg";
		if (outLower.endsWith(".png")) {
			format = "png";
		} else if (outLower.endsWith(".gif")) {
			format = "gif";
		}
		File outFile = new File(outImage);
		File parentDir = outFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}
		System.out.println("输出文件：" + outFile.getAbsolutePath());
		boolean result = ImageIO.write(image, format, outFile);
		if (!result) {
			throw new IOException("图片写入失败，不支持的格式或图片数据异常：" + outImage);
		}
	}
	
	/**
	 * 添加文字水印（简化版本，使用默认参数）
	 * @param orgImage 原图路径
	 * @param outImage 输出图片路径
	 * @param watermarkText 水印文字
	 * @param fontSize 字体大小
	 * @param multiLine 是否多行显示
	 * @param lineSpacing 行间隔
	 * @param angle 倾斜角度
	 * @param position 水印位置
	 * @throws Exception
	 */
	public static void addTextWatermark(String orgImage, String outImage, String watermarkText, 
			int fontSize, boolean multiLine, int lineSpacing, double angle, 
			Position position) throws Exception {
		addTextWatermark(orgImage, outImage, watermarkText, fontSize, multiLine, 
				lineSpacing, angle, position, Color.WHITE, 0.5f);
	}
	
	/**
	 * 添加文字水印（最简版本，使用默认位置和颜色）
	 * @param orgImage 原图路径
	 * @param outImage 输出图片路径
	 * @param watermarkText 水印文字
	 * @param fontSize 字体大小
	 * @param multiLine 是否多行显示
	 * @param lineSpacing 行间隔
	 * @param angle 倾斜角度
	 * @throws Exception
	 */
	public static void addTextWatermark(String orgImage, String outImage, String watermarkText, 
			int fontSize, boolean multiLine, int lineSpacing, double angle) throws Exception {
		addTextWatermark(orgImage, outImage, watermarkText, fontSize, multiLine, 
				lineSpacing, angle, Positions.BOTTOM_RIGHT, Color.WHITE, 0.5f);
	}
	/**
	 * 裁剪
	 * 图片中心400*400的区域
	 * @throws IOException
	 */
	public void CompressPicByCut(String orgImage,String outImage,Position position,int width,int height) throws IOException {
		Thumbnails.of(orgImage).sourceRegion(position, width,
				height).size(width, height).keepAspectRatio(false).toFile(
						outImage);
		
//		/**
//		 * 图片中心400*400的区域
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(Positions.CENTER, 400,
//				400).size(200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_center.jpg");
//		/**
//		 * 图片右下400*400的区域
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(Positions.BOTTOM_RIGHT,
//				400, 400).size(200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_bootom_right.jpg");
//		/**
//		 * 指定坐标
//		 */
//		Thumbnails.of("images/test.jpg").sourceRegion(600, 500, 400, 400).size(
//				200, 200).keepAspectRatio(false).toFile(
//				"images/image_region_coord.jpg");
	}
	
}
