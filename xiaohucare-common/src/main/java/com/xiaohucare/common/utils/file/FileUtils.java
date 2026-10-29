package com.xiaohucare.common.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.xiaohucare.common.config.XiaohucareConfig;
import com.xiaohucare.common.constant.Constants;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.common.utils.uuid.IdUtils;

/**
 * 文件处理工具类
 * 
 * @author xiaohucare
 */
public class FileUtils
{
	
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    public static String getConfigBasePath() {
		URL url = null;
		url = FileUtils.class.getClassLoader().getResource("");
		String path = url.getPath();
		return path;
	}
    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            IOUtils.close(os);
            IOUtils.close(fis);
        }
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeImportBytes(byte[] data) throws IOException
    {
        return writeBytes(data, XiaohucareConfig.getImportPath());
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @param uploadDir 目标文件
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeBytes(byte[] data, String uploadDir) throws IOException
    {
        FileOutputStream fos = null;
        String pathName = "";
        try
        {
            String extension = getFileExtendName(data);
            pathName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
            File file = FileUploadUtils.getAbsoluteFile(uploadDir, pathName);
            fos = new FileOutputStream(file);
            fos.write(data);
        }
        finally
        {
            IOUtils.close(fos);
        }
        return FileUploadUtils.getPathFileName(uploadDir, pathName);
    }

    /**
     * 移除路径中的请求前缀片段
     * 
     * @param filePath 文件路径
     * @return 移除后的文件路径
     */
    public static String stripPrefix(String filePath)
    {
        return StringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     * 
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource)
    {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource)))
        {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response 响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 获取图像后缀
     * 
     * @param photoByte 图像数据
     * @return 后缀名
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "gif";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "jpg";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "bmp";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "png";
        }
        return strFileExtendName;
    }

    /**
     * 获取文件名称 /profile/upload/2022-04-16/example.png -- example.png
     * 
     * @param fileName 路径名称
     * @return 没有文件路径的名称
     */
    public static String getName(String fileName)
    {
        if (fileName == null)
        {
            return null;
        }
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int index = Math.max(lastUnixPos, lastWindowsPos);
        return fileName.substring(index + 1);
    }

    /**
     * 获取不带后缀文件名称 /profile/upload/2022-04-16/example.png -- example
     * 
     * @param fileName 路径名称
     * @return 没有文件路径和后缀的名称
     */
    public static String getNameNotSuffix(String fileName)
    {
        if (fileName == null)
        {
            return null;
        }
        String baseName = FilenameUtils.getBaseName(fileName);
        return baseName;
    }
    
    /**
	 * 获取扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePathName
	 * @return
	 */
	public static String getFileName(String filePathName) {
		if ((filePathName != null) && (filePathName.length() > 0)) {
			int dot = filePathName.lastIndexOf('/');
			if ((dot > -1) && (dot < (filePathName.length() - 1))) {
				return filePathName.substring(dot + 1);
			}
		}
		return filePathName;
	}
	/**
	 * 获取文件路径
	 * 
	 * @param filePathName
	 * @return
	 */
	public static String getFilePath(String filePathName) {
		if ((filePathName != null) && (filePathName.length() > 0)) {
			int dot = filePathName.lastIndexOf('/');
			if ((dot > -1) && (dot < (filePathName.length() - 1))) {
				return filePathName.substring(0, dot);
			}
		}
		return filePathName;
	}
	public static String toGetDealFileUrl(String urlAndFileName) {
		if(urlAndFileName==null) {
			return "";
		}
		String[] names=urlAndFileName.split("\\|");
		return names[0];
	}
	

	public static byte[] getImageDataAsImagePath(String fileAndPath) {
		// set data
		byte[] data = null;
		FileInputStream input = null;
		File tmpFile = new File(fileAndPath);

		try {
			long fileSize = tmpFile.length();
			data = new byte[(int) fileSize];
			input = new FileInputStream(tmpFile);
			input.read(data);
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 
	 * @param fileName
	 */
	public static List<String> readFileByLines(String fileName) {
		File file = new File(fileName);
		return readFileByLines(file);
	}
	public static List<String> readFileByLines(File file) {
		List<String> list = new ArrayList<String>();
		try {
			 list = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);// 指定编码为UTF-8
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return list;
//		List<String> list = new ArrayList<String>();
//		System.out.println(file.getName());
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new FileReader(file));
//			String tempString = null;
//			int line = 1;
//			while ((tempString = reader.readLine()) != null) {
//				line++;
//				list.add(tempString);
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
//		return list;
	}

	/**
	 * 
	 * @param fileName
	 */
	public static String readFileToStr(String fileName) {
		StringBuffer returnStr = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		int line = 1;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				returnStr.append(tempString.trim());
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return returnStr.toString();
	}

	public static String readFileToLineStr(String fileName) {
		File file = new File(fileName);
		return readFileToLineStr(file);
	}
	public static String readFileToLineStr(File file) {
		StringBuffer returnStr = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				returnStr.append(tempString.trim());
				returnStr.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return returnStr.toString();
	}

	public static List<String> readFileByLinesAsInputStream(InputStream in, String charset) {
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, charset));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				System.out.println("line " + line + ": " + tempString);
				line++;

				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return list;
	}

	public static List<String> readFileByLinesAsInputStream(String fileName) {
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				// System.out.println("line " + line + ": " + tempString);
				line++;
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return list;
	}

	public static InputStream getInputStreamAsFilePath(String fileName) {
		InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
		return in;
	}

	public static File getFileAsFilePath(String fileName) {
		File file = new File(getConfigBasePath() + "/" + fileName);
		return file;
	}
}
