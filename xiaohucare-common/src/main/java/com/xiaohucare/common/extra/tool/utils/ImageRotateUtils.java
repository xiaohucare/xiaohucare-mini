package com.xiaohucare.common.extra.tool.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
/**
 * 图片旋转工具类
 */
public class ImageRotateUtils {
	
    
	 public static String urlStringToBase64Image(String imageUrl) {  
	        try {  
	            ByteArrayInputStream bais = DownloadFileUtils.saveToFiles(imageUrl);
	            BufferedImage bufferedImage = ImageIO.read(bais); 
	            return getBufferedImageBase64(bufferedImage);
	        } catch (IOException e) {  
	       }  
	      return null;
	   }  
	 public static BufferedImage urlStringToBufferedImage(String imageUrl) {  
		 try {  
			 ByteArrayInputStream bais = DownloadFileUtils.saveToFiles(imageUrl);
			 BufferedImage bufferedImage = ImageIO.read(bais); 
			 return bufferedImage;
		 } catch (IOException e) {  
		 }  
		 return null;
	 }  
	 public static BufferedImage base64StringToBufferedImage(String base64String) {  
		 try {  
			 byte[] bytes =Base64.getDecoder().decode(base64String);
			 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);  
			 BufferedImage bufferedImage = ImageIO.read(bais); 
			 return bufferedImage;
		 } catch (IOException e) {  
		 }  
		 return null;
	 }
	 public static File base64StringToImageFile(String base64String) { 
		 BufferedImage bufferedImage = base64StringToBufferedImage(base64String);
		 File file=null;
		try {
			file = File.createTempFile(RandomUtil.getRandom()+"", ".png");// 可以是jpg,png,gif格式 
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		 try {
			 ImageIO.write(bufferedImage, "png", file);// 不管输出什么格式图片，此处不需改动  
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 return file;
	 }
	 public static void base64StringToImage(String base64String,String outPath) { 
		    BufferedImage bufferedImage = base64StringToBufferedImage(base64String);
            File file = new File(outPath);// 可以是jpg,png,gif格式  
            try {
				ImageIO.write(bufferedImage, "png", file);// 不管输出什么格式图片，此处不需改动  
			} catch (IOException e) {
				e.printStackTrace();
		}
	 }
	 public static String getBufferedImageBase64(BufferedImage bufferedImage) {
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         try {
				ImageIO.write(bufferedImage, "png", baos);
			} catch (IOException e) {
				e.printStackTrace();
			}
	         byte[] bytes = baos.toByteArray();
	         // 对字节数组Base64编码
	         return Base64.getEncoder().encodeToString(bytes).trim();
	} 
	 public static  byte[]  getBufferedImageByte(BufferedImage bufferedImage) {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
         try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
         byte[] bytes = baos.toByteArray();
         // 对字节数组Base64编码
         return bytes;
   } 
	 public static String getImageBase64ByFile(File temFile) {
		 if(temFile.isFile()&&temFile.exists()) {
				InputStream in = null;
				byte[] data = null;
				// 读取图片字节数组
				try {
					
					in = new FileInputStream(temFile);
					data = new byte[in.available()];
					in.read(data);
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 对字节数组Base64编码
				return Base64.getEncoder().encodeToString(data);
			}else {
				return null;
			}
	 }
	 public static String getImageBase64(String imgUrl) {
			File temFile=new File(imgUrl);
			return getImageBase64ByFile(temFile);
	}
	
	public static void writeImgByBufferedImage(BufferedImage bufferedImage, String targerPath) {
		OutputStream os;
		try {
			os = new FileOutputStream(targerPath);
			// 生成图片
			ImageIO.write(bufferedImage, "jpg", os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage writeImgToBufferedImage(String srcImgPath) {
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(new File(srcImgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return originalImage;
	}
	
	public static Image writeImgToImage(String srcImgPath) {
		Image originalImage = null;
		try {
			originalImage = ImageIO.read(new File(srcImgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return originalImage;
	}
	
	/**
	 * 对图片进行旋转
	 *
	 * @param src   被旋转图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// 计算旋转后图片的尺寸
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// 进行转换
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, null, null);
		return res;
	}
	
	/**
     * 旋转图片为指定角度
     * 
     * @param bufferedimage
     *            目标图像
     * @param degree
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,final int degree){
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(h, w, type)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2 + (w>h?(w-h)/2:(h-w)/2));
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }


	/**
	 * 计算旋转后的图片
	 *
	 * @param src   被旋转的图片
	 * @param angel 旋转角度
	 * @return 旋转后的图片
	 */
	public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// 如果旋转的角度大于90度做相应的转换
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}
		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new Rectangle(new Dimension(des_width, des_height));
	}
}
