package com.xiaohucare.app.care.mapper;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.ResidentCardEntity;
/**
 * 会员卡表
 * 
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
@Mapper
public interface ResidentCardMapper extends BaseMapper<ResidentCardEntity> {

    @Select("SELECT " +
        "(SELECT COUNT(1) FROM e_resident_card WHERE is_delete=0) AS totalCards, " +
        "(SELECT COUNT(1) FROM e_resident_card WHERE is_delete=0 AND status=1) AS activeCards, " +
        "(SELECT IFNULL(SUM(balance+gift_balance),0) FROM e_resident_card WHERE is_delete=0 AND status=1) AS totalBalance, " +
        "(SELECT IFNULL(SUM(amount),0) FROM e_resident_card_record " +
        "  WHERE is_delete=0 AND trade_type='recharge' " +
        "  AND DATE_FORMAT(trade_time,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m')) AS monthRecharge " +
        "FROM DUAL")
    Map<String, Object> selectStat();
}
