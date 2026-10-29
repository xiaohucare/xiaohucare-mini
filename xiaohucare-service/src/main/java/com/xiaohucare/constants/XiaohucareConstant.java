package com.xiaohucare.constants;

public class XiaohucareConstant {
	
	public static final Integer APP_LB_TENANT_CKL_ID = 20000;
	//银杏树康养 wxba37988c05725faf
//	public static final String APP_LB_WX = "wxba37988c05725faf";
	public static final String APP_MSXC_WX = "wxc58a85f4c0f33fc1";
	public static final String COMPANY_SN_LB = "COMPANY_SN_LB";
	public static final String SYS_USER_TYPE_SYS = "ADMIN";
	public static final String SYS_USER_TYPE_PLAT = "PLAT";
	public static final String SYS_USER_TYPE_PLAT_GROUP = "PLAT_GROUP";
	public static final String SYS_USER_TYPE_PLAT_SW = "PLAT_SW";

	public static final String SYS_CZ_PHONE = "SYS_CZ_PHONE";

	public static final String CW_BS_NAME = "CW_BS_NAME";
	public static final String CW_BS_PHONE = "CW_BS_PHONE";
	public static final String CW_BS_SFZ = "CW_BS_SFZ";
	 public final static String N_STORAGE_PLAT_ACCOUNT_KEY = "N_STORAGE_PLAT_ACCOUNT_KEY";
	 public final static String N_STORAGE_PLAT_XCX_QR_KEY = "N_STORAGE_PLAT_XCX_QR_KEY";
	 public final static String N_STORAGE_PLAT_ABOUT_QR_KEY = "N_STORAGE_PLAT_ABOUT_QR_KEY";
	 public final static String N_STORAGE_PLAT_APP_QR_KEY = "N_STORAGE_PLAT_APP_QR_KEY";
	 
	 public enum APP_SMS_SIGN {
         XKApp("闲客App","闲客App平台"),
         PEIZHEN("99陪诊","99陪诊"),
         DLY("大礼鱼","大礼鱼WEB"),
         CKLAPP("银杏树康养","银杏树康养"),
         ;
         public String nCode;
         public String nCodeName;
         private APP_SMS_SIGN(String _nCode,String _nCodeName) {
             this.nCode = _nCode;
             this.nCodeName = _nCodeName;
         }
    };
	 public enum APPPLUGINTYPE {
	    	WX_GZH(1,"微信公众号"),
	    	WX_XCX(11,"微信小程序"),
	    	WX_APP(12,"APP[微信]"),
			ALI_APP(20,"APP[支付宝]"),
			TT_DY_XCX(30,"抖音小程序"),
			TT_DY_WEB(31,"抖音WEB"),
			TT_DY_APP(32,"抖音app"),
		 	DLY_QR_WEB(60,"大礼鱼WEB");
	        public int nCode;
	        public String nCodeName;
	        private APPPLUGINTYPE(int _nCode,String _nCodeName) {
	            this.nCode = _nCode;
	            this.nCodeName = _nCodeName;
	        }
	    };
	    /** 超级管理员ID */
		public static final long SUPER_ADMIN = 2012L;
    /**
     * 超级用户角色
     */
    public static final long SYS_ROLE_ADMIN_ID = 100L ;
    public static final long SYS_ROLE_PLAT_ID = 99L;

    /**
     * ORACLE、MYSQL
     */
    public static final String USE_DATA = "MYSQL";
    /**
     * 分页条数
     */
    public static final int pageSize = 10;
    /**
     * 权限前缀
     */
    public static final String PERMS_LIST = "permsList";
    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
    /**
     * 短信配置KEY
     */
    public final static String SMS_CONFIG_KEY = "SMS_CONFIG_KEY";
    /**
     * 权限前缀
     */
    public static final String SESSION_KEY = "SESSIONID_";

    /**
     * 排序方式名称 asc:正序 | desc:倒序
     */
    public static final String SORT_ORDER = "sortOrder";
    /**
     * 当前登录用户
     */
    public static final String CURRENT_USER = "curUser";
    /*
     * 当前系统关联企业
     */
    public static final String CURRENT_COMPANY_USER = "curCompanyUser";
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASS_WORD = "888888";
    /**
     * 系统缓存前缀
     */
    public static final String SYS_CACHE = "SYS_CACHE:";
    

    public static final String SYS_MAIN_XCX_QR_ONE = "SYS_MAIN_XCX_QR_ONE";
    public static final String SYS_MAIN_XCX_QR_TWO = "SYS_MAIN_XCX_QR_TWO";
    
    public static final String SYS_LOGIN_SMS_CODE = "SYS_LOGIN_SMS_CODE";
    
    
    public final static String SYS_PATH = "/root/allApplication/data-xrs/data/videos/";

    /**
     * 菜单类型
     *
     * @author xiaohucare
     * @date 2016年11月15日 下午1:24:29
     */
    public final static long MenuRootId=100;
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

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author xiaohucare
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

        private ScheduleStatus(int value) {
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
        QCLOUD(3),
        /**
         * 服务器存储
         */
        DISCK(4);

        private int value;

        private CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    
    /**
     * 短信服务商 //类型 1:创瑞云SMS 2：云片  3：阿里云  4：腾讯云 
     */
    public enum CloudSmsService {
        /**
         * 创瑞云SMS 
         */
        CYY(1),
        /**
         * 云片
         */
        YUNPIAN(2),
        /**
         * 阿里云
         */
        ALIYUN(3),
        /**
         * 腾讯云
         */
        QCLOUD(4);

        private int value;

        private CloudSmsService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 是否类型
     */
    public enum YESNO {
        /**
         * 是
         */
        YES("0"),
        /**
         * 否
         */
        NO("1");
        private String value;

        private YESNO(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
