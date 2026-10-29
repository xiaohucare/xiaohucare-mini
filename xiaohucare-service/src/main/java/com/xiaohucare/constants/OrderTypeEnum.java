package com.xiaohucare.constants;

public enum OrderTypeEnum {
	TO_HOME(0,"货到付款"),
    SHOPPING(1,"商城订单"),
    TOSHOP_PAY(10000,"到店支付"),
    PLAT_PAY(10100,"平台内"),
    PLAT_INIT_PAY(10200,"平台内"),
    PLAT_BOX_PAY(10600,"喵喵Box"),
	PROMOTE_LOTTERY(3026,"抽奖"),
	SHOPPING_CART(100,"购物车"),
    SHOPPING_POINT(101,"积分兑换"),
    
    
    SHOPPING_STORE(150,"门店购买"),
    SHOPPING_STORE_PLAN(151,"洗护"),
    SHOPPING_STORE_JY(152,"康养"),
    SHOPPING_STORE_PET(153,"活体"),
    SHOPPING_STORE_EVENT(154,"活动"),
    SHOPPING_STORE_SKU(156,"门店收银台"),
    
	
	SHOPPING_EVERY_DAY_POINT(102,"每日释放"),
	SHOPPING_EVERY_DAY_POINT_TRY(103,"每日体验释放"),
	SHOPPING_LIVING_STORE(2200,"活体商城订单"),
	
	
	CARD_ORDER(202,"会员卡订单"),
	CARD_TO_SHOP_BALANCE_PAY(203,"会员卡余额支付"),
	CARD_SHARE_ORDER(2021,"会员卡推荐订单"),
	CARD_SHARE_ORDER_AGENT(2022,"会员卡渠道"),
	CARD_SHARE_ORDER_STORE(2022,""),
	CARD_INVITER_ORDER_POINT(2022,"会员卡推荐奖励影豆"),
	CARD_VIP_TAKE(2023,"会员卡核销"),
	
	WALLET_RECHANGER(3,"金额充值"),
	WALLET_RECHANGER_API(35,"三方金额充值"),
	WALLET_TX(5,"金额提现"),
	MEMBER_MOBILE(500,"用户绑定手机"),
	MEMBER_SHARE(510,"推荐用户"),
	NO(-1,"未知"),
	;
	public int nCode;
	public String nCodeName;
	private OrderTypeEnum(int _nCode,String _nCodeName) {
		this.nCode = _nCode;
		this.nCodeName = _nCodeName;
	}
	public static OrderTypeEnum findByCode(int _nCode) {
		for (OrderTypeEnum type : OrderTypeEnum.values()) {
			if(_nCode==type.nCode) {
				return type;
			}
		}
		return OrderTypeEnum.NO;
	}
	
}
