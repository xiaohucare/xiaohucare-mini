package com.xiaohucare.app.care.controller;

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
import com.xiaohucare.framework.core.api.utils.ApiThreadContext;
import com.xiaohucare.framework.core.api.vo.ApiHeaderVo;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.oss.utils.DynamicOss;
import com.xiaohucare.framework.oss.utils.DynamicOss.Config;
import com.xiaohucare.framework.oss.utils.OssUtils;
import com.xiaohucare.framework.system.domain.SysOss;
import com.xiaohucare.framework.system.service.SysOssService;

import cn.hutool.core.lang.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
@Tag(name = "通用文件上传接口")
@Slf4j
@RestController
@RequestMapping("/intra/care/oss")
public class StoreOssController extends BaseController{
	@Autowired
	private SysOssService sysOssService;
	@Operation(summary = "文件带路径上传")
	@ResponseBody
	@PostMapping("/upload/{osspath}")
	public R<Map<String,Object>> upload(@RequestParam("file") MultipartFile file, @PathVariable("osspath") String osspath)
			throws Exception {
		if (file.isEmpty()) {
			return R.error("文件不能为空");
		}
		
		String fileName = file.getOriginalFilename();
		String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
		String pathAndfileName = "api/oss/"+osspath+"/" + UUID.fastUUID() + "." + extensionName;
		ApiHeaderVo apiHeaderVo=ApiThreadContext.getApiHeader();
		String url =null;
		if(apiHeaderVo!=null&&apiHeaderVo.getOssType()!=null&&apiHeaderVo.getOssType()==1) {
			  // 1. 构建配置
	        Config config = Config.builder()
	                .endpoint(apiHeaderVo.getOssEndpoint())
	                .accessKeyId(apiHeaderVo.getOssAccessKeyId())
	                .accessKeySecret(apiHeaderVo.getOssAccessKeySecret())
	                .bucketName(apiHeaderVo.getOssBucket())
	                .prefix(apiHeaderVo.getOssPrefix())
	                .domain(apiHeaderVo.getOssDomain())
	                .https(false)
	                .build();
	        DynamicOss dynamicOss = new DynamicOss(config);
			url = dynamicOss.upload(file, pathAndfileName);
		}else {
			url = OssUtils.uploadFile(file, pathAndfileName);
		}
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
			ApiHeaderVo apiHeaderVo=ApiThreadContext.getApiHeader();
			String previewUrl =null;
			if(apiHeaderVo!=null&&apiHeaderVo.getOssType()!=null&&apiHeaderVo.getOssType()==1) {
				  // 1. 构建配置
		        Config config = Config.builder()
		                .endpoint(apiHeaderVo.getOssEndpoint())
		                .accessKeyId(apiHeaderVo.getOssAccessKeyId())
		                .accessKeySecret(apiHeaderVo.getOssAccessKeySecret())
		                .bucketName(apiHeaderVo.getOssBucket())
		                .prefix(apiHeaderVo.getOssPrefix())
		                .domain(apiHeaderVo.getOssDomain())
		                .https(false)
		                .build();
		        DynamicOss dynamicOss = new DynamicOss(config);
		        previewUrl = dynamicOss.getFileUrl(sysOss.getUrl());
			}else {
				previewUrl = OssUtils.getFileUrl(sysOss.getUrl());
			}
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
			ApiHeaderVo apiHeaderVo=ApiThreadContext.getApiHeader();
			 DynamicOss dynamicOss =null;
			if(apiHeaderVo!=null&&apiHeaderVo.getOssType()!=null&&apiHeaderVo.getOssType()==1) {
				  // 1. 构建配置
		        Config config = Config.builder()
		                .endpoint(apiHeaderVo.getOssEndpoint())
		                .accessKeyId(apiHeaderVo.getOssAccessKeyId())
		                .accessKeySecret(apiHeaderVo.getOssAccessKeySecret())
		                .bucketName(apiHeaderVo.getOssBucket())
		                .prefix(apiHeaderVo.getOssPrefix())
		                .domain(apiHeaderVo.getOssDomain())
		                .https(false)
		                .build();
		        dynamicOss = new DynamicOss(config);
			}else {
			}
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
						if(apiHeaderVo!=null&&apiHeaderVo.getOssType()!=null&&apiHeaderVo.getOssType()==1&&dynamicOss!=null) {
							previewUrl = dynamicOss.getFileUrl(sysOss.getUrl());
						}else {
							previewUrl = OssUtils.getFileUrl(sysOss.getUrl());
						}
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