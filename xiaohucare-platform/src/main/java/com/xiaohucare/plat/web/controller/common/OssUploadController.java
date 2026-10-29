package com.xiaohucare.plat.web.controller.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.common.extra.tool.utils.Base64Util;
import com.xiaohucare.framework.core.api.utils.ApiThreadContext;
import com.xiaohucare.framework.core.api.vo.ApiHeaderVo;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.oss.utils.OssUtils;
import com.xiaohucare.framework.system.domain.SysOss;
import com.xiaohucare.framework.system.service.SysOssService;

import cn.hutool.core.lang.UUID;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
@Tag(name = "通用文件上传接口")
@Slf4j
@RestController
@RequestMapping("/common/oss")
public class OssUploadController extends BaseController{
	@Autowired
	private SysOssService sysOssService;

	 @Operation(summary = "文件上传")
	@PostMapping("/upload")
	public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return R.error("文件不能为空");
		}
		// 上传文件
		String fileName = file.getOriginalFilename();
		String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
		String pathAndfileName = "api/oss/" + UUID.fastUUID() + "." + extensionName;
		String url = OssUtils.uploadFile(file, pathAndfileName);
		Long fileSize = file.getSize();
		log.info("--------url---------" + url);
		SysOss ossEntity = new SysOss();
		ossEntity.setUrl(url);
		ossEntity.setCreateAt(new Date());
		ossEntity.setFileSize(fileSize);
		ossEntity.setName(fileName);
		ossEntity.setOssId(pathAndfileName);
		try {
			ossEntity.setCreateBy(this.getUserId());
		} catch (Exception e) {
		} 
		ossEntity.setOssType(OssUtils.getStorageType());
		ossEntity.setPrefix(OssUtils.getPrefix());
	   sysOssService.save(ossEntity);
		return R.data(url);
	}
	 @Operation(summary = "文件带路径上传")
	@ResponseBody
	@PostMapping("/toupload/{osspath}")
	public R<Map<String,Object>> toupload(@RequestParam("file") MultipartFile file, @PathVariable("osspath") String osspath)
			throws Exception {
		if (file.isEmpty()) {
			return R.error("文件不能为空");
		}
		
		String fileName = file.getOriginalFilename();
		String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
		String pathAndfileName = "api/oss/"+osspath+"/" + UUID.fastUUID() + "." + extensionName;
		String url = OssUtils.uploadFile(file, pathAndfileName);
		Long fileSize = file.getSize();
		log.info("--------url---------" + url);
		SysOss ossEntity = new SysOss();
		ossEntity.setUrl(url);
		ossEntity.setCreateAt(new Date());
		ossEntity.setFileSize(fileSize);
		ossEntity.setName(fileName);
		ossEntity.setOssId(pathAndfileName);
		try {
			ossEntity.setCreateBy(this.getUserId());
		} catch (Exception e) {
		} 
		ossEntity.setRemark(OssUtils.getStorageType());
		ossEntity.setOssType(OssUtils.getStorageType());
		ossEntity.setPrefix(OssUtils.getPrefix());
		sysOssService.save(ossEntity);
		Map<String,Object> r = new HashMap<String, Object>();
		r.put("url", url);
		r.put("fileName", fileName);
		r.put("extensionName", extensionName);
		r.put("fileSize", fileSize);
		return R.data(r);
	}
	 @Operation(summary = "文件base64上传")
	@PostMapping("/uploadbase64")
	public R<String> uploadbase64(@RequestParam("base64img") String base64img) throws Exception {
		 if (base64img == null) {
				return R.error("数据不能为空");
			}
			String prefix = "jpg";
			String imgBase64 = base64img;
			if (base64img.contains(",")) {
				String data[] = base64img.split(",");
				String fileName = data[0];
				prefix = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf(";"));
				imgBase64 = data[data.length - 1];
			}
			// 上传文件
			String pathAndfileName = UUID.fastUUID() + "." + prefix;
			InputStream inputStream = new ByteArrayInputStream(Base64Util.strToByte(imgBase64));
			String url = OssUtils.uploadFile(inputStream, pathAndfileName, "image/jpeg");
			Long fileSize =0L;
			log.info("--------url---------" + url);
			SysOss ossEntity = new SysOss();
			ossEntity.setUrl(url);
			ossEntity.setCreateAt(new Date());
			ossEntity.setFileSize(fileSize);
			ossEntity.setName(pathAndfileName);
			ossEntity.setOssId(pathAndfileName);
			try {
				ossEntity.setCreateBy(this.getUserId());
			} catch (Exception e) {
			} 
			ossEntity.setRemark(OssUtils.getStorageType());
			ossEntity.setOssType(OssUtils.getStorageType());
			ossEntity.setPrefix(OssUtils.getPrefix());
			sysOssService.save(ossEntity);
		return R.data(url);
	}

	@Operation(summary = "获取文件预览URL")
	@PostMapping("/preview/{ossId}")
	public R<String> getPreviewUrl(@PathVariable("ossId") String ossId) {
		if (ossId == null || ossId.isEmpty()) {
			return R.error("文件ID不能为空");
		}
		try {
			// 查询OSS记录
			QueryWrapper<SysOss> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("oss_id", ossId);
			SysOss sysOss = sysOssService.getOne(queryWrapper);
			if (sysOss == null) {
				return R.error("文件不存在");
			}
			// 获取文件访问URL
			String previewUrl = OssUtils.getFileUrl(sysOss.getUrl());
			log.info("--------previewUrl---------" + previewUrl);
				String previewUrl2 = OssUtils.getFileUrl(ossId);
			log.info("--------previewUrl2---------" + previewUrl2);
			return R.data(previewUrl);
		} catch (Exception e) {
			log.error("获取文件预览URL失败: " + e.getMessage(), e);
			return R.error("获取预览URL失败: " + e.getMessage());
		}
	}

	@Operation(summary = "批量获取文件预览URL")
	@PostMapping("/preview/batch")
	public R<Map<String, String>> getBatchPreviewUrls(@RequestParam("ossIds") String ossIds) {
		if (ossIds == null || ossIds.isEmpty()) {
			return R.error("文件ID列表不能为空");
		}
		try {
			Map<String, String> result = new HashMap<>();
			String[] ossIdArray = ossIds.split(",");
			for (String ossId : ossIdArray) {
				ossId = ossId.trim();
				if (!ossId.isEmpty()) {
					QueryWrapper<SysOss> queryWrapper = new QueryWrapper<>();
					queryWrapper.eq("oss_id", ossId);
					SysOss sysOss = sysOssService.getOne(queryWrapper);
					if (sysOss != null) {
						String previewUrl = OssUtils.getFileUrl(sysOss.getUrl());
						result.put(ossId, previewUrl);
					}
				}
			}
			log.info("--------batch previewUrls---------" + result.size());
			return R.data(result);
		} catch (Exception e) {
			log.error("批量获取文件预览URL失败: " + e.getMessage(), e);
			return R.error("批量获取预览URL失败: " + e.getMessage());
		}
	}

}