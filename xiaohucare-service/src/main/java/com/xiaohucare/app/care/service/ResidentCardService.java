package com.xiaohucare.app.care.service;
import java.util.Map;

import com.xiaohucare.app.care.entity.ResidentCardEntity;
import com.xiaohucare.app.care.vo.ResidentCardVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 会员卡表
 *
 * @author xiaohucare
 * @date 2026-03-27 13:18:36
 */
public interface ResidentCardService extends BaseService<ResidentCardEntity> {

    /**
     * 开卡：创建会员卡 + 写入开卡流水
     */
    ResidentCardVO openCard(ResidentCardVO vo);

    /**
     * 充値：更新余额 + 写入充値流水
     */
    boolean recharge(Map<String, Object> params);

    /**
     * 消费：扣减余额 + 写入消费流水
     */
    boolean consume(Map<String, Object> params);

    /**
     * 挂失/解挂/注销：更新卡状态
     */
    boolean toggleStatus(Long id, Integer status);

    /**
     * 会员卡统计
     */
    Map<String, Object> getStat();

	ResidentCardVO getCardByResidentId(Long residentId);
}

