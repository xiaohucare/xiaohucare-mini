package com.xiaohucare.cache;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiaohucare.common.core.redis.RedisUtil;
import com.xiaohucare.common.extra.tool.utils.DateUtils;
import com.xiaohucare.common.extra.tool.utils.RandomUtil;
import com.xiaohucare.common.extra.utils.DateUtil;

import lombok.AllArgsConstructor;

@Component("appIdCache")
@AllArgsConstructor
public class AppIdCache  {
	@Autowired
	private RedisUtil redisUtil;
	private static final String platApp="CKLHOTELAPP_";
	
	private static  String LB_MEMBER_CODE_KEY = "lb.member.code.generate.key.%s";
	private static  String LB_MEMBER_SHARE_CODE_KEY = "lb.member.share.code.generate.key.%s";
	private static  String LB_STORE_CODE_KEY = "lb.store.code.generate.key.%s";
	private static  String LB_GOOD_CODE_KEY = "lb.good.code.generate.key.%s";
	private static  String LB_MALL_ORDER_CODE_KEY = "lb.mall.order.code.generate.key.%s";
	private static  String LB_PAY_CODE_KEY = "lb.pay.code.generate.key.%s";
	private static  String LB_POINT_CODE_KEY = "lb.point.code.generate.key.%s";
	private static  String LB_SHARE_CODE_KEY = "lb.share.code.generate.key.%s";
	private static  String LB_PET_CODE_KEY = "lb.pet.code.generate.key.%s";
	private static  String LB_SHOW_QR_CODE_KEY = "lb.show.qr.code.generate.key.%s";
	private static  String LB_BATCH_CODE_KEY = "lb.batch.qr.code.generate.key.%s";
	private static  String LB_BOX_CODE_KEY = "lb.box.qr.code.generate.key.%s";
	private static  String LB_FW_QR_CODE_KEY = "lb.fw.qr.code.generate.key.%s";
	private static  String LB_PET_QR_CODE_KEY = "lb.pet.qr.code.generate.key.%s";
	
	
	public static  String  ID_WORK_TYPE_QR_CARD_ORDER_NO="gen.key.%s_QR_CARD_ORDER_NO";
	public static  String  ID_WORK_TYPE_COMPANY_SN="gen.key.%s_COMPANY_SN";
	public static  String  ID_WORK_TYPE_HALL_SN="gen.key.%s_HALL_SN";
	public static  String  ID_WORK_TYPE_HALL_ORDER_SN="gen.key.%s_HALL_ORDER_SN";
	public static  String  ID_WORK_TYPE_ART_SN="gen.key.%s_ART_SN";
	public static  String  ID_WORK_TYPE_ART_WORK_SN="gen.key.%s_ART_WORK_SN";
	public static  String  ID_WORK_TYPE_ART_MUSEUM_SN="gen.key.%s_ART_MUSEUM_SN";
	public static  String  ID_WORK_TYPE_ART_BASE_SN="gen.key.%s_ART_BASE_SN";
	public static  String  ID_WORK_TYPE_HISTORY_SN="gen.key.%s_HISTORY_SN";
	public static  String  ID_WORK_TYPE_ART_BASE_BOOK_ORDER_SN="gen.key.%s_ART_BASE_BOOK_ORDER_SN";
	public static  String  ID_WORK_TYPE_ACTIVITY_ITEM_SN="gen.key.%s_ACTIVITY_ITEM_SN";//
	public static  String  ID_WORK_TYPE_SHOP_PRODUCT_SN="gen.key.%s_SHOP_PRODUCT_SN";//
	public static  String  ID_WORK_TYPE_IDENTIFY_SN="gen.key.%s_IDENTIFY_SN";//
	public static  String  ID_WORK_TYPE_ZB_SN="gen.key.%s_ZB_SN";//
	public static  String  ID_WORK_TYPE_SHOP_BID_SN="gen.key.%s_SHOP_BID_SN";
	public static  String  ID_WORK_TYPE_BIG_SN="gen.key.%s_BIG_SN";
	public static  String  ID_WORK_TYPE_BIG_PB_SN="gen.key.%s_BIG_PB_SN";
	public static  String  ID_WORK_TYPE_PAY_SN="gen.key.%s_PAY_SN";
	public static  String  ID_WORK_TYPE_LIVE_PARTNER_SETTLE_SN="gen.key.%s_LIVE_PARTNER_SETTLE_SN";
	private static String LB_LEVEL_CODE_KEY = "lb.level.code.generate.key.%s";
	private static String LB_CARD_CODE_KEY = "lb.card.code.generate.key.%s";
	private static String LB_SHOW_CODE_KEY = "lb.show.code.generate.key.%s";
	private static String LB_SEAT_CODE_KEY = "lb.seat.code.generate.key.%s";
	private static String LB_HALL_CODE_KEY = "lb.hall.code.generate.key.%s";
	private static String LB_SEAT_ORDER_CODE_KEY = "lb.seat.order.code.generate.key.%s";
	
	private static String LB_QUEUE_ORDER_CODE_KEY = "lb.queue.order.code.generate.key.%s";
	
	private static String LB_CHAIN_CODE_KEY = "lb.chain.code.generate.key.%s";
	
	private static String LB_EVENT_CODE_KEY = "lb.build.code.generate.key.%s";
	private static String LB_SHARE_ONCE_CODE_KEY = "lb.share.once.code.generate.key.%s";
	private static String LB_SHARE_QR_CODE_KEY = "lb.share.qr.code.generate.key.%s";
	public String genCommonIdDayEndRadom(String prefixStr,String idType, int randomLength) {
		String timeFormat =DateUtils.format(new Date(),"yyMMdd"); 
		String key =platApp+ String.format(idType, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtils.addDays(new Date(), 2));
		}
		return String.format("%sA%s%03d%s",prefixStr, timeFormat, id,RandomUtil.getRandomByLength(randomLength));
	}
	
	public static void main(String[] args) {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key = String.format(LB_PAY_CODE_KEY, timeFormat);
		Long id = 10L;
		String no= String.format("V%01d",3331);
		System.out.println(timeFormat);
		System.out.println(key);
		System.out.println(no);
		System.out.println(no.length());
		
		     timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
	         key = String.format(LB_STORE_CODE_KEY, timeFormat);
	         id =1l;
	        String aa= String.format("S%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	        System.out.println(aa);
	        
	         timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
	         key = String.format(LB_GOOD_CODE_KEY, timeFormat);
	        id =1l;
	        aa= String.format("G%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
            System.out.println(aa);
            System.out.println(aa.length());
            timeFormat =DateUtil.format(new Date(),"MMdd"); 
            aa=String.format("B8%03d%s%02d",101, timeFormat, 2);
            System.out.println(aa);
	}
	public String genBatchNo() {
        String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
        String key = platApp+String.format(LB_BATCH_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("P%s%03d", timeFormat, id);
    }
	public String genBoxNo(Integer cateId) {
        String timeFormat =DateUtil.format(new Date(),"MMdd"); 
        String key = platApp+String.format(LB_BOX_CODE_KEY, timeFormat);
        key=key+"_C_"+cateId;
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("B8%03d%s%02d",cateId, timeFormat, id);
    }
	public String genEventNo() {
        String timeFormat = DateUtil.format(new Date(), "yyMMdd");
        String key = platApp + String.format(LB_EVENT_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key, 1L);
        int i = 2;
        if (id < i) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("E%s%03d%01d", timeFormat, id, RandomUtil.getRandom(1, 9));
    }
	public String genShowQrSn() {
        String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
        String key = platApp+String.format(LB_SHOW_QR_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("P%s%03d", timeFormat, id);
    }
	public String genFwQrSn() {
        String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
        String key = platApp+String.format(LB_FW_QR_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("P%s%03d", timeFormat, id);
    }
	public String genPetSn() {
	    String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
	    String key = String.format(LB_PET_CODE_KEY, timeFormat);
	    Long id = redisUtil.incr(key,1L);
	    if (id < 2) {
	        redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
	    }
	    return String.format("P%s%03d", timeFormat, id);
	}
	public String genPetQrSn() {
        String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
        String key =platApp+ String.format(LB_PET_QR_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("P%s%03d", timeFormat, id);
    }
	public String genChainNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key =platApp+ String.format(LB_CHAIN_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		int i = 2;
		if (id < i) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("S%s%03d%d", timeFormat, id,RandomUtil.getRandom(1, 9));
	}
	public String genSeatOrderNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_SEAT_ORDER_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("S%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genSeatNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_SEAT_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("S%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genHallNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_HALL_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("H%s%06d%03d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genLevelNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_LEVEL_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("VM%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genShowNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key =platApp+ String.format(LB_SHOW_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("S%s%03d%d", timeFormat, id,RandomUtil.getRandom(1, 9));
	}
	
	public String genCardNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key =platApp+ String.format(LB_CARD_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("V%s%03d%d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	
	public String getIdDayHHMMEndRadom(String prefixStr,String idType, int randomLength) {
		String timeFormat =DateUtils.format(new Date(),"yyMMddHHmm"); 
		String key = String.format(idType, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtils.addDays(new Date(), 2));
		}
		return String.format("%sA%s%03d%s",prefixStr, timeFormat, id,RandomUtil.getRandomByLength(randomLength));
	}
	public String getIdDayEndRadom(String prefixStr,String idType, int randomLength) {
		String timeFormat =DateUtils.format(new Date(),"yyMMdd"); 
		String key = String.format(idType, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtils.addDays(new Date(), 2));
		}
		return String.format("%sA%s%03d%s",prefixStr, timeFormat, id,RandomUtil.getRandomByLength(randomLength));
	}
	
	
	public String genUserNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+String.format(LB_MEMBER_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("VM%s%02d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String shareCode() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key = String.format(LB_MEMBER_SHARE_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("V%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String shPaySn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_SHARE_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("SH%s%03d", timeFormat, id);
	}
	public String shTakeVipSn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+ String.format(LB_PET_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("SH%s%03d", timeFormat, id);
	}
	public String shTakeVipQueueNo() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key =platApp+ String.format(LB_QUEUE_ORDER_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		id=id+6000;
		return String.format("%03d%01d",id,RandomUtil.getRandom(0, 9));
	}
	public String genStoreSn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key =platApp+  String.format(LB_STORE_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("S%s%02d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	
	public String genGoodSn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
		String key = String.format(LB_GOOD_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("G%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genMallOrder() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+  String.format(LB_MALL_ORDER_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("MO%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genPaySn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key =platApp+  String.format(LB_PAY_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("PN%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genPointSn() {
		String timeFormat =DateUtil.format(new Date(),"yyMMddHHmm"); 
		String key = platApp+String.format(LB_POINT_CODE_KEY, timeFormat);
		Long id = redisUtil.incr(key,1L);
		if (id < 2) {
			redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
		}
		return String.format("PNT%s%03d%01d", timeFormat, id,RandomUtil.getRandom(0, 9));
	}
	public String genShareOnceSn() {
        String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
        String key =platApp+ String.format(LB_SHARE_ONCE_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("S%s%04d%d", timeFormat, id,RandomUtil.getRandom(1, 9));
    }
	public String genShowQrNo() {
        String timeFormat =DateUtil.format(new Date(),"yyMMdd"); 
        String key =platApp+ String.format(LB_SHARE_QR_CODE_KEY, timeFormat);
        Long id = redisUtil.incr(key,1L);
        
        if (id < 2) {
            redisUtil.expireAt(key, DateUtil.plusDays(new Date(), 2L));
        }
        return String.format("SQ%s%03d%d", timeFormat, id,RandomUtil.getRandom(1, 9));
    }
}
