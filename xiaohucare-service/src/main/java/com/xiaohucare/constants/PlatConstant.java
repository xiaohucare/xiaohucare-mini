package com.xiaohucare.constants;

public class PlatConstant {
        public static final String platApp="CKLPLATAPP_";
        public enum PLAT_CONFIG_Enum{
            N_STORAGE_PLAT_ACCOUNT_KEY("N_STORAGE_PLAT_ACCOUNT_KEY","平台账号"),
            N_STORAGE_PLAT_TIPS_KEY("N_STORAGE_PLAT_TIPS_KEY","平台公告"),
            N_STORAGE_PLAT_XCX_QR_KEY("N_STORAGE_PLAT_XCX_QR_KEY","二维码"),
            N_STORAGE_PLAT_ABOUT_QR_KEY("N_STORAGE_PLAT_ABOUT_QR_KEY","关于我们"),
            N_STORAGE_PLAT_APP_QR_KEY("N_STORAGE_PLAT_APP_QR_KEY","APP二维码"),
            N_STORAGE_PLAT_APP_UPDATE_TIPS_KEY("N_STORAGE_PLAT_APP_UPDATE_TIPS_KEY","APP版本信息"),
            N_STORAGE_PLAT_BANK_KEY("N_STORAGE_PLAT_BANK_KEY","平台银行账号"),
            N_STORAGE_PLAT_PAY_KEY("N_STORAGE_PLAT_PAY_KEY","支付控制"),
            ;
            public String nKey;
            public String nTips;
            private PLAT_CONFIG_Enum(String _nKey,String _nTips) {
                this.nKey = _nKey;
                this.nTips = _nTips;
            }
        };
        public enum AGENT_BONUS_TYPE_Enum{
            DAY(0,"天"),
            WEEK(1,"周"),
            MONTH(2,"月"),
            SEASON(3,"季"),
            YEAR(4,"年"),
            ;
            public int nCode;
            public String nCodeName;
            private AGENT_BONUS_TYPE_Enum(int _nCode,String _nCodeName) {
                this.nCode = _nCode;
                this.nCodeName = _nCodeName;
            }
        };
	    public enum AGENT_TYPE {
	    	TYPE_MEMBER(0,"个人"),
	    	TYPE_GROUP(1,"企业"),
	    	TYPE_PROVINCE(10,"省代"),
	    	TYPE_CITY(11,"市代"),
	    	TYPE_REGION(100,"区代"),
	    	TYPE_YYS(12,"运营商"),
		 	;
	        public int nCode;
	        public String nCodeName;
	        private AGENT_TYPE(int _nCode,String _nCodeName) {
	            this.nCode = _nCode;
	            this.nCodeName = _nCodeName;
	        }
	    };
	    public enum AGENT_KIND {
	    	TYPE_PROVINCE(10,"省代",0,0),
	    	TYPE_CITY(11,"市代",99999,500000),
	    	TYPE_REGION(100,"区代",29999,135000),
	    	TYPE_YYS(12,"运营商",9999,40000),
		 	;
	        public int nCode;
	        public String nCodeName;
	        public Integer nMoney;
	        public Integer nFund;
	        private AGENT_KIND(int _nCode,String _nCodeName,Integer _nMoney,Integer _nFund) {
	            this.nCode = _nCode;
	            this.nCodeName = _nCodeName;
	            this.nMoney = _nMoney;
	            this.nFund = _nFund;
	        }
	    	public static AGENT_KIND getEnumType(Integer value) {
				if (value == null) {
					return null;
				}
				AGENT_KIND[] enumItem = AGENT_KIND.values();
				for (AGENT_KIND item : enumItem) {
					if (item.nCode == value) {
						return item;
					}
				}
				return null;
			}
	    };
	    
}
