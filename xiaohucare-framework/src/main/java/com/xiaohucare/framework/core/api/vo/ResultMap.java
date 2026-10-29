package com.xiaohucare.framework.core.api.vo;

import java.util.HashMap;
import java.util.Map;

public class ResultMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	public ResultMap() {
	        put("code", Response.OK);
	    }
	public static ResultMap error() {
		return error(Response.BUSINESS_ERROR, "未知异常，请联系管理员");
	}

	public static ResultMap error(String message) {
		return error(Response.BUSINESS_ERROR, message);
	}
	
	public static ResultMap error(int code, String message) {
		ResultMap r = new ResultMap();
		r.put("code", code);
		r.put("message", message);
		return r;
	}
	public  boolean isOk() {
		Integer code =(Integer) get("code");
		return code==Response.OK;
	}
	public static ResultMap ok() {
		ResultMap r = new ResultMap();
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap ok(String data) {
		ResultMap r = new ResultMap();
		r.put("data", data);
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap success() {
		ResultMap r = new ResultMap();
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap data(Object data) {
		ResultMap r = new ResultMap();
		r.put("data", data);
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap page(Object data) {
		ResultMap r = new ResultMap();
		r.put("page", data);
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap status(boolean flag) {
		ResultMap r = new ResultMap();
		r.put("data", flag);
		r.put("code", Response.OK);
		return r;
	}
	
	
	public static ResultMap success(String message) {
		ResultMap r = new ResultMap();
		r.put("code", Response.OK);
		r.put("message", message);
		return r;
	}
	public static ResultMap success(Object data) {
		ResultMap r = new ResultMap();
		r.put("data", data);
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap success(int code,Object data) {
		ResultMap r = new ResultMap();
		r.put("data", data);
		r.put("code",code);
		return r;
	}
	public static ResultMap success(int code,String message,Object data) {
		ResultMap r = new ResultMap();
		r.put("data", data);
		r.put("code",code);
		r.put("message", message);
		return r;
	}
	public static ResultMap successData(Map<String, Object> map) {
		ResultMap r = new ResultMap();
		r.put("data", map);
		r.put("code", Response.OK);
		return r;
	}
	public static ResultMap success(Map<String, Object> map) {
		ResultMap r = new ResultMap();
		r.putAll(map);
		return r;
	}
	public ResultMap put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	@SuppressWarnings("unchecked")
	public ResultMap putData(String key, Object value) {
		Map<String, Object> map=null;
		if(this.containsKey("data")) {
			map=(Map<String, Object>) this.get("data");
		}else {
			map=new HashMap<String, Object>();
		}
		map.put(key, value);
		super.put("data", map);
		return this;
	}
	
	
	
}
