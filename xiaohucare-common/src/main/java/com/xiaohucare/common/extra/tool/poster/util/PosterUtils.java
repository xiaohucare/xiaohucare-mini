package com.xiaohucare.common.extra.tool.poster.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 图片工具类
 */
@Component
public class PosterUtils {
    // 程序当前运行目录
    private static final String current = System.getProperty("user.dir");
    /**
     * 下载路径
     */
    private static String downloadPath = current + "/downloads/";
    /**
     * 模板路径
     */
    private static String templatesPath = current + "/templates/";
    /**
     * 模板路径
     */
    private static String fontsPath = current + "/fonts/";
    /**
     * 获取资源路径
     * @param resourceName
     * @return String
     * @throws IOException
     */
    public static String getResourcePath() throws IOException {
        URL url = PosterUtils.class.getClassLoader().getResource("");
        if (url != null) {
            return url.getPath();
        }
        return null;
    }
    
    /**
     * 获取海报模板资源路径
     * 支持开发和打包后两种环境
     * @param resourceName 资源名称（如：template/share_good.json）
     * @return 资源文件路径
     * @throws IOException
     */
    public static String getResourcePath(String resourceName) throws IOException {
        // 优先从 classpath 中获取（支持 JAR 打包环境）
        try {
            String resourcePath = "poster/" + resourceName;
            URL url = PosterUtils.class.getClassLoader().getResource(resourcePath);
            if (url != null) {
                // 处理 file: 和 jar:file: 两种协议
                String path = url.getPath();
                // URL 解码（处理空格等特殊字符）
                path = java.net.URLDecoder.decode(path, "UTF-8");
                return path;
            }
        } catch (Exception e) {
            // 如果 classpath 获取失败，继续尝试文件系统
        }
        
        // 备用方案：从用户自定义目录中获取
        String tempPath = templatesPath + resourceName;
        File file = new File(tempPath);
        if (file.exists()) {
            return tempPath;
        }
        
        // 最后备用：从当前工作目录查找
        String currentPath = System.getProperty("user.dir") + "/poster/" + resourceName;
        file = new File(currentPath);
        if (file.exists()) {
            return currentPath;
        }
        
        // 实在找不到就抛异常
        throw new IOException("资源文件未找到: " + resourceName);
    }
    /**
     * 获取图片
     * @param url
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage getImage(String url) throws IOException {
        if (url.contains("://")) {
            return getImageFromUrl(url);
        }
        // 如果是Base64 url
        if (url.contains("data:image")) {
            byte[] bytes = Base64.decodeBase64(url.split(",")[1].getBytes());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(byteArrayInputStream);
        }
        BufferedImage imageFile = getTemplateImage(url);
        if (imageFile != null) {
            return imageFile;
        }
        throw new IOException("Can't get input stream from URL!");
    }
    /**
     * 从模板中获取图片
     * @param imageName
     * @return imageFile
     * @throws IOException
     */
    public static BufferedImage getTemplateImage(String imageName) throws IOException {
        // 从用户自定义的目录中找
        String tempImage=templatesPath+ imageName;
        File imageFile = new File(tempImage);
        if (imageFile.exists()) {
            return ImageIO.read(imageFile);
        }
        // 找不到的话从默认模板中找
        imageFile = new File(PosterUtils.getResourcePath("templates/" + imageName));
        if (imageFile.exists()) {
            return ImageIO.read(imageFile);
        }
        // 实在找不到就抛异常
        throw new IOException("file not found!");
    }
    /**
     * 通过 URL 获取图片并缓存到本地文件夹中
     * @param url
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage getImageFromUrl(String url) throws IOException {
        return getImageFromUrl(url, false);
    }
    /**
     * 获取下载过的文件
     * @param url
     * @return File
     */
    public static File getDownloadedFile(String url) {
        String tempDownloadPath=downloadPath+ DigestUtils.md5DigestAsHex(url.getBytes()) + ".png";
        File imageFile = new File(tempDownloadPath);
        if (imageFile.exists()) {
            return imageFile;
        }
        return null;
    }
    public static String getDownloadedFilePath(String url) {
        String tempDownloadPath=downloadPath+ DigestUtils.md5DigestAsHex(url.getBytes()) + ".png";
        return tempDownloadPath;
    }
    public static BufferedImage getImageFromUrl(String url, Boolean update) throws IOException {
        File file =getDownloadedFile(url);
        if (file != null) {
            if (update) {
                file.delete();
            } else {
                return ImageIO.read(file);
            }
        }
        System.out.println(url);
        BufferedImage image = ImageIO.read(new URL(url));
        // 存起来
        File imageFile = new File(getDownloadedFilePath(url));
        System.out.println(imageFile.getAbsolutePath());
        imageFile.mkdirs();
        ImageIO.write(image, "PNG", imageFile);
        return image;
    }
    public static File getFontFile(String font) throws IOException {
        if(!font.endsWith(".ttf")) {
            font=font+ ".ttf";
        }
       // 从用户自定义的目录中找
        String localFont=fontsPath+font;
        System.out.println(localFont);
        File fontFile = new File(localFont);
        if (fontFile.exists()) {
            return fontFile;
        }
        // 找不到的话从默认字体库中找
        fontFile = new File(PosterUtils.getResourcePath("fonts/" + font));
        System.out.println(fontFile.getAbsolutePath());
        if (fontFile.exists()) {
            return fontFile;
        }
        // 实在找不到就抛异常
        throw new IOException(font + " font not found!");
    }
    /**
     * 图片设置圆角
     * @param srcImage
     * @param radius
     * @param border
     * @param padding
     * @return
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if (border != 0) {
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
        }
        gs.dispose();
        return image;
    }
    

    /**
     * 图片设置圆角
     * @param srcImage
     * @return BufferedImage
     */
    public static BufferedImage setRadius(BufferedImage srcImage) {
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 6;
        return setRadius(srcImage, radius, 2, 5);
    }
    /**
     * 图片切圆角
     *
     * @param srcImage
     * @param radius
     * @return BufferedImage
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }
}
