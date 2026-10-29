package com.xiaohucare.framework.core.api.vo;
public class Response {
	public static final int OK = 0; 
	public static final int PARTIAL_SUCCESS = 1; 
	public static final int ALL_FAILURE = 2; 
	public static final int SYS_ERROR = 5; //系统错误
	public static final int PARAM_ERROR = 6; //参数错误
	public static final int LOGIN_ERROR = 10; //登录失败
	public static final int MEMBER_NOT_FOLLOW = 11; //标识没有关注微信账号，强制用户必须关注公众号
	public static final int AUTH_ERROR =100; //权限错误
	public static final int DB_ERROR =200; //数据重复
	public static final int NO_LOGIN = 401; //未登录
	public static final int BUSINESS_ERROR =500; //业务错误
}

