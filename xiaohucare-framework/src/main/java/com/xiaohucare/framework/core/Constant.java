
package com.xiaohucare.framework.core;
/**
 * 常量
 *
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    public static final String CURPAGE = "curPage";
    public static final String PAGENUM = "pageNum";
    public static final String PAGESIZE = "pageSize";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";
    /**
	 * 编码
	 */
    public static final String UTF_8 = "UTF-8";

	/**
	 * contentType
	 */
    public static final String CONTENT_TYPE_NAME = "Content-type";

	/**
	 * JSON 资源
	 */
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	/**
	 * 角色前缀
	 */
    public static final String SECURITY_ROLE_PREFIX = "ROLE_";

	/**
	 * 主键字段名
	 */
    public static final String DB_PRIMARY_KEY = "id";

	/**
	 * 业务状态[1:正常]
	 */
    public static final int DB_STATUS_NORMAL = 1;

	/**
	 * 删除状态[0:正常,1:删除]
	 */
    public static final int DB_NOT_DELETED = 0;
    public static final int DB_IS_DELETED = 1;

	/**
	 * 用户锁定状态
	 */
    public static final int DB_ADMIN_NON_LOCKED = 0;
    public static final int DB_ADMIN_LOCKED = 1;

	/**
	 * 顶级父节点id
	 */
    public static final Long TOP_PARENT_ID = 0L;

	/**
	 * 管理员对应的租户ID
	 */
    public static final String ADMIN_TENANT_ID = "000000";

	/**
	 * 日志默认状态
	 */
    public static final String LOG_NORMAL_TYPE = "1";

	/**
	 * 默认为空消息
	 */
    public static final String DEFAULT_NULL_MESSAGE = "暂无承载数据";
	/**
	 * 默认成功消息
	 */
    public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
	/**
	 * 默认失败消息
	 */
    public static final String DEFAULT_FAILURE_MESSAGE = "操作失败";
	/**
	 * 默认未授权消息
	 */
    public static final String DEFAULT_UNAUTHORIZED_MESSAGE = "签名认证失败";
	/**
	 * 菜单类型
	 * 
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
}
