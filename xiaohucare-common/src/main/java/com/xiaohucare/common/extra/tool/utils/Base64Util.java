package com.xiaohucare.common.extra.tool.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.commons.codec.Charsets;
import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Base64工具
 *
 * @author L.cm
 */
@Slf4j
public class Base64Util extends org.springframework.util.Base64Utils {

	public static String  byteToString(byte[] content) {
		return new String(Base64.getEncoder().encode(content));
	}
	public static byte[]  strToByte(String content) {
		return Base64.getDecoder().decode(content);
	}
	/**
	 * 编码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encode(String value) {
		return Base64Util.encode(value, Charsets.UTF_8);
	}

	/**
	 * 编码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encode(String value, java.nio.charset.Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encode(val), charset);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value) {
		return Base64Util.encodeUrlSafe(value, Charsets.UTF_8);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value, java.nio.charset.Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encodeUrlSafe(val), charset);
	}

	/**
	 * 解码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decode(String value) {
		return Base64Util.decode(value, Charsets.UTF_8);
	}

	/**
	 * 解码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decode(String value, java.nio.charset.Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decode(val);
		return new String(decodedValue, charset);
	}

	/**
	 * 解码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value) {
		return Base64Util.decodeUrlSafe(value, Charsets.UTF_8);
	}
	/**
	 * 解码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value, java.nio.charset.Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decodeUrlSafe(val);
		return new String(decodedValue, charset);
	}
	
	
	/**
     * 将Base64字符串解码并保存为文件
     * @param base64Data Base64编码的字符串
     * @param outputFilePath 输出文件路径
     * @return 成功返回true，失败返回false
     */
    public static boolean convertBase64ToFile(String base64Data, String outputFilePath) {
        try {
            // 检查并清理Base64数据（移除可能的数据URI前缀）
            String cleanedBase64 = cleanBase64Data(base64Data);
            // 解码Base64字符串
            byte[] decodedBytes = Base64.getDecoder().decode(cleanedBase64);
            // 确保输出目录存在
            File outputFile = new File(outputFilePath);
//            if(!outputFile.getParentFile().exists()) {
//            	 outputFile.getParentFile().mkdirs();
//            }
            // 将字节写入文件
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(decodedBytes);
            }
            System.out.println("文件已成功保存到: " + outputFilePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("文件写入失败: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Base64解码失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 清理Base64数据，移除可能的数据URI前缀
     * @param base64Data 原始Base64数据
     * @return 清理后的纯Base64数据
     */
    private static String cleanBase64Data(String base64Data) {
        // 处理常见的数据URI格式
        if (base64Data.contains(",")) {
            return base64Data.split(",")[1];
        }
        // 移除可能的换行符和空格
        return base64Data.replaceAll("\\s+", "");
    }
    
    /**
     * 根据文件扩展名自动确定MIME类型并生成数据URI
     * @param filePath 文件路径
     * @return 数据URI字符串
     */
    public static String createDataUriFromFile(String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            String base64Data = Base64.getEncoder().encodeToString(fileContent);
            // 根据文件扩展名确定MIME类型
            String mimeType = determineMimeType(filePath);
            
            return "data:" + mimeType + ";base64," + base64Data;
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
            return null;
        }
    }
    /**
     * 根据文件扩展名确定MIME类型
     * @param filePath 文件路径
     * @return MIME类型字符串
     */
    private static String determineMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "pdf": return "application/pdf";
            case "jpg": case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "zip": return "application/zip";
            case "txt": return "text/plain";
            default: return "application/octet-stream";
        }
    }
    
    /**
     * 将Base64字符串还原为ZIP文件
     *
     * @param base64String   Base64编码的字符串
     * @param outputFilePath 输出的ZIP文件路径
     */
    public static boolean base64ToZip(String base64String, String outputFilePath) {
    	 if (base64String == null || base64String.isEmpty()) {
             System.err.println("Base64字符串不能为空");
             return false;
         }
         if (outputFilePath == null || outputFilePath.isEmpty()) {
             System.err.println("输出文件路径不能为空");
             return false;
         }
        try {
            byte[] bytes = Base64Utils.decodeFromString(base64String);
            File file = new File(outputFilePath);
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes, true);
            return true;
        } catch (IOException e) {
        	log.error("base64ToZip error", e);
        }
        return false;
    }
    /**
     * 将Base64字符串转换为文件
     * 
     * @param base64String Base64编码的字符串
     * @param outputFilePath 输出文件路径（包含文件名和扩展名）
     * @return 转换成功返回true，否则返回false
     */
    public static boolean convert(String base64String, String outputFilePath) {
        if (base64String == null || base64String.isEmpty()) {
            System.err.println("Base64字符串不能为空");
            return false;
        }
        if (outputFilePath == null || outputFilePath.isEmpty()) {
            System.err.println("输出文件路径不能为空");
            return false;
        }
        try {
            // 移除可能存在的Base64前缀（如"data:image/png;base64,"）
            String base64Data = removeBase64Prefix(base64String);
            // 解码Base64字符串为字节数组
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            // 创建输出文件目录（如果不存在）
            File outputFile = new File(outputFilePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            // 将字节数组写入文件
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(decodedBytes);
            }
            System.out.println("文件转换成功: " + outputFile.getAbsolutePath());
            return true;
            
        } catch (IllegalArgumentException e) {
            System.err.println("Base64解码失败，输入不是有效的Base64字符串: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("文件写入失败: " + e.getMessage());
        }
        return false;
    }
    /**
     * Base64字符串转InputStream
     */
    public static InputStream toInputStream(String base64Data) throws IOException {
        try {
            String cleanedBase64 = cleanBase64Data(base64Data);
            byte[] decodedBytes = Base64.getDecoder().decode(cleanedBase64);
            return new ByteArrayInputStream(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new IOException("无效的Base64字符串", e);
        }
    }
    /**
     * 移除Base64字符串中可能存在的前缀
     * 例如：data:image/png;base64, 或 data:application/pdf;base64,
     */
    private static String removeBase64Prefix(String base64String) {
        if (base64String.startsWith("data:")) {
            int commaIndex = base64String.indexOf(',');
            if (commaIndex != -1) {
                return base64String.substring(commaIndex + 1);
            }
        }
        return base64String;
    }
    
}
