package com.xiaohucare.app.care.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.service.BedService;
import com.xiaohucare.app.care.service.CheckinFeeItemService;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.service.ResidentArchiveSyncService;
import com.xiaohucare.app.care.service.SettlementDetailService;
import com.xiaohucare.app.care.service.SettlementService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.app.care.vo.CheckinVO;
import com.xiaohucare.app.care.vo.CheckoutVO;
import com.xiaohucare.app.care.wrapper.CheckinWrapper;
import com.xiaohucare.cache.AppIdCache;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
/**
 * 退住交接（一人一单：交接信息直接落在 e_care_checkin 上）
 * 说明：登记主流程（列表/统计/详情/提交/删除/押金/结算）见 {@link CheckinController}
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@RestController
@RequestMapping("intra/hotel/checkin")
public class CheckOutController extends BaseController{
    @Autowired
    private CheckinService checkinService;
    @Autowired
    private CheckinFeeItemService checkinFeeItemService;
    @Autowired
    private BedService bedService;
    @Autowired
    private ResidentArchiveSyncService residentArchiveSyncService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private SettlementDetailService settlementDetailService;
    @Autowired
    private AppIdCache appIdCache;

    /**
     * 长者退住交接列表
     */
    @Operation(summary = "长者退住交接列表")
    @Parameters({
        @Parameter(name = "residentName", description = "长者姓名"),
        @Parameter(name = "contactPhone", description = "联系电话"),
        @Parameter(name = "checkinNo", description = "登记单号"),
        @Parameter(name = "checkinDate", description = "入住日期"),
        @Parameter(name = "checkoutStatus", description = "交接状态")
    })
    @GetMapping("/browseCheckOut")
    @PreAuthorize("@ss.hasPermi('care:checkin:list')")
    public R<VPage<CheckinVO>> browseCheckOut(@RequestParam Map<String, Object> params){
        QueryWrapper<CheckinEntity> queryWrapper=VQueryKit.getQueryWrapperByMap(params,Arrays.asList("residentName","contactPhone","checkinNo","checkinDate","checkoutStatus"), CheckinEntity.class);
        
        // 只展示已完成登记（康养中/已退住）的单据，登记中的不进入退住列表
        queryWrapper.ge("checkin_status", 2);
        queryWrapper.orderByDesc("id");
        queryWrapper.like(StringUtils.isNotNull(params.get("residentName")),"resident_name", params.get("residentName"));
        // 合并后家属电话就在本行，直接模糊匹配
        queryWrapper.like(StringUtils.isNotNull(params.get("contactPhone")),"contact_phone", params.get("contactPhone"));
        queryWrapper.like(StringUtils.isNotNull(params.get("checkinNo")),"checkin_no", params.get("checkinNo"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("checkinDate")),"checkin_date", params.get("checkinDate"));
        queryWrapper.eq(StringUtils.isNotNull(params.get("checkoutStatus")),"checkout_status", params.get("checkoutStatus"));
        IPage<CheckinEntity> pages = checkinService.page(VQueryKit.getPage(params), queryWrapper);
        return R.data(CheckinWrapper.build().toVPageVO(pages));
    }

    /**
     * 退住交接详情
     */
    @Operation(summary = "退住交接详情")
    @GetMapping("/getPetCheckOut/{id}")
    @PreAuthorize("@ss.hasPermi('care:checkin:detail')")
    public R<CheckinVO> getCheckOut(@PathVariable("id") Long id){
        CheckinEntity checkin = checkinService.getById(id);
        if(checkin==null) {
            return R.error("退住数据不存在");
        }
        CheckinVO checkinVO = CheckinWrapper.build().entityVO(checkin);
        fillCompatResidentList(checkinVO);
        checkinVO.setFeeItemList(checkinFeeItemService.listByCheckin(id, checkin.getActualDays()));
        return R.data(checkinVO);
    }

    /**
     * 保存退住交接草稿
     */
    @Operation(summary = "保存退住交接草稿")
    @PostMapping("/submitDraftCheckOut")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    public R<CheckinVO> submitDraftCheckOut(@Valid @RequestBody CheckoutVO checkoutVO){
        if(checkoutVO.getId()==null) {
            return R.error("退住数据不存在");
        }
        CheckinEntity dbCheckin = checkinService.getById(checkoutVO.getId());
        if(dbCheckin==null) {
            return R.error("退住数据不存在");
        }
        if(dbCheckin.getCheckoutStatus()!=null && dbCheckin.getCheckoutStatus()==2) {
            return R.error("该长者已完成退住交接，不能重复编辑");
        }
        CheckinEntity update = buildHandoverEntity(checkoutVO, dbCheckin);
        update.setCheckoutStatus(1);
        if(!checkinService.updateById(update)) {
            return R.error("数据错误");
        }
        saveFeeItems(update.getId(), checkoutVO.getFeeItemList(), dbCheckin.getActualDays());
        return R.ok(loadVO(update.getId()));
    }

    /**
     * 完成退住交接（放床、更新长者档案与登记状态、生成待结算单）
     */
    @Operation(summary = "完成退住交接")
    @PostMapping("/submitCheckOut")
    @PreAuthorize("@ss.hasPermi('care:checkin:submit')")
    @Transactional(rollbackFor = Exception.class)
    public R<CheckinVO> submitCheckOut(@Valid @RequestBody CheckoutVO checkoutVO){
        if(checkoutVO.getId()==null) {
            return R.error("退住数据不存在");
        }
        CheckinEntity dbCheckin = checkinService.getById(checkoutVO.getId());
        if(dbCheckin==null) {
            return R.error("退住数据不存在");
        }
        if(dbCheckin.getCheckoutStatus()!=null && dbCheckin.getCheckoutStatus()==2) {
            return R.error("该长者已完成退住交接，请勿重复提交");
        }
        if(dbCheckin.getCheckinStatus()!=null && dbCheckin.getCheckinStatus()==0) {
            return R.error("登记尚未确认（押金未付），不能办理退住");
        }
        CheckinEntity update = buildHandoverEntity(checkoutVO, dbCheckin);
        update.setCheckoutStatus(2);
        update.setCheckinStatus(3);
        update.setSettled(1);
        // 交接页只提交交接字段，其余字段从库里取，床位与档案才能正确处理
        Date actualCheckoutDate = checkoutVO.getCheckoutDate()!=null ? checkoutVO.getCheckoutDate() : new Date();
        update.setActualCheckoutDate(actualCheckoutDate);
        if(update.getActualDays()==null) {
            update.setActualDays(calcActualDays(dbCheckin, actualCheckoutDate));
        }
        if(!checkinService.updateById(update)) {
            return R.error("数据错误");
        }
        saveFeeItems(update.getId(), checkoutVO.getFeeItemList(), update.getActualDays());
        // 释放床位（同床仍有人在住时保持占用）
        if(dbCheckin.getBedId()!=null) {
            bedService.releaseBed(dbCheckin.getBedId());
        }
        // 更新长者档案：置为已退住、清空房间床位、记录离店时间（统一由档案同步服务维护）
        if(dbCheckin.getResidentId()!=null) {
            residentArchiveSyncService.markCheckedOut(dbCheckin.getResidentId(), actualCheckoutDate);
        }
        List<CheckinFeeItemVO> feeItems = checkinFeeItemService.listByCheckin(update.getId(), update.getActualDays());
        dbCheckin.setActualDays(update.getActualDays());
        dbCheckin.setActualCheckoutDate(actualCheckoutDate);
        buildSettlement(dbCheckin, feeItems);
        return R.ok(loadVO(update.getId()));
    }

    /** 交接字段组装：未提交的快照字段从库里补齐 */
    private CheckinEntity buildHandoverEntity(CheckoutVO checkoutVO, CheckinEntity dbCheckin) {
        CheckinEntity update = new CheckinEntity();
        update.setId(dbCheckin.getId());
        update.setCheckoutNo(buildCheckoutNo(checkoutVO.getCheckoutNo(), dbCheckin.getCheckoutNo()));
        update.setActualDays(checkoutVO.getActualDays());
        update.setHealthChangeDesc(checkoutVO.getHealthChangeDesc());
        update.setAppearanceChanges(checkoutVO.getAppearanceChanges());
        update.setCareSummary(checkoutVO.getCareSummary());
        update.setCheckoutPhotos(checkoutVO.getCheckoutPhotos());
        update.setReturnedItems(checkoutVO.getReturnedItems());
        update.setItemsStatus(checkoutVO.getItemsStatus());
        update.setItemsConfirmed(checkoutVO.getItemsConfirmed());
        update.setItemsRemark(checkoutVO.getItemsRemark());
        update.setPerformance(checkoutVO.getPerformance());
        update.setHealthReminder(checkoutVO.getHealthReminder());
        update.setSuggestions(checkoutVO.getSuggestions());
        update.setCustomerSatisfaction(checkoutVO.getCustomerSatisfaction());
        update.setCustomerFeedback(checkoutVO.getCustomerFeedback());
        update.setUpdateAt(new Date());
        update.setUpdateBy(getUsername());
        return update;
    }

    /** 已存在实际住养天数则沿用，否则按入住日期到退住日期计算（不足 1 天按 1 天） */
    private Integer calcActualDays(CheckinEntity dbCheckin, Date actualCheckoutDate) {
        if(dbCheckin.getActualDays()!=null && dbCheckin.getActualDays()>0) {
            return dbCheckin.getActualDays();
        }
        if(dbCheckin.getCheckinDate()==null) {
            return 1;
        }
        long diff = DateUtils.differentDaysByMillisecond(dbCheckin.getCheckinDate(), actualCheckoutDate);
        return diff < 1 ? 1 : (int) diff;
    }

    /** 交接单号：沿用已有单号，避免每次保存草稿重新生成 */
    private String buildCheckoutNo(String submittedNo, String existedNo) {
        if(StringUtils.isNotBlank(submittedNo)) {
            return submittedNo;
        }
        if(StringUtils.isNotBlank(existedNo)) {
            return existedNo;
        }
        return appIdCache.genCommonIdDayEndRadom("CHKO","CHKO",1);
    }

    /** 保存费用项并逻辑删除本次未提交的项目，保证移除后的项目不再参与结算 */
    private void saveFeeItems(Long checkinId, List<CheckinFeeItemVO> feeItemVoList, Integer actualDays) {
        List<Long> keepIds = new ArrayList<>();
        if(feeItemVoList!=null) {
            for(CheckinFeeItemVO feeItem : feeItemVoList) {
                // 防止前端把收费项目档案 ID 误当费用项记录 ID 传回，造成串改他人数据
                if(feeItem.getId()!=null) {
                    CheckinFeeItemEntity existed = checkinFeeItemService.getById(feeItem.getId());
                    if(existed==null || !checkinId.equals(existed.getCheckinId())) {
                        feeItem.setId(null);
                    }
                }
                if(feeItem.getId()==null) {
                    feeItem.setCreateAt(new Date());
                    feeItem.setCreateBy(getUsername());
                    feeItem.setIsDelete(0);
                }
                feeItem.setCheckinId(checkinId);
                checkinFeeItemService.saveOrUpdate(feeItem);
                keepIds.add(feeItem.getId());
            }
        }
        QueryWrapper<CheckinFeeItemEntity> removeQuery = new QueryWrapper<>();
        removeQuery.eq("checkin_id", checkinId);
        if(!keepIds.isEmpty()) {
            removeQuery.notIn("id", keepIds);
        }
        checkinFeeItemService.remove(removeQuery);
    }

    /**
     * 退住完成后预生成待结算的结算单（供结算列表收款、退押金使用）
     */
    private void buildSettlement(CheckinEntity checkin, List<CheckinFeeItemVO> feeItemList) {
        QueryWrapper<SettlementEntity> existQuery = new QueryWrapper<>();
        existQuery.eq("checkin_id", checkin.getId());
        existQuery.eq("status", 0);
        SettlementEntity existing = settlementService.getOne(existQuery, false);
        if (existing != null) {
            // 已存在待结算单：仅补建/刷新明细，避免重复生成结算单
            settlementDetailService.rebuild(existing, checkin, feeItemList);
            return;
        }
        SettlementEntity settlement = new SettlementEntity();
        settlement.setSettlementNo("STL" + System.currentTimeMillis() + RandomUtil.randomNumbers(4));
        settlement.setCheckinId(checkin.getId());
        settlement.setResidentId(checkin.getResidentId());
        Integer actualDays = checkin.getActualDays();
        // 康养费：有套餐三价快照时按实际天数逐段重算（不满年少按月、不满月按天）；旧登记单无三价则回退冻结日单价×实际天数
        BigDecimal bedFee;
        if (checkin.getComboYearPrice() != null || checkin.getComboMonthPrice() != null || checkin.getComboDayPrice() != null) {
            bedFee = ChargeCalcKit.calcBedFee(checkin.getComboYearPrice(), checkin.getComboMonthPrice(), checkin.getComboDayPrice(), actualDays);
        } else {
            // 无套餐三价快照（旧登记单）：无法逐段计费，置 0（迁移脚本已为存量单补齐三价）
            bedFee = BigDecimal.ZERO;
        }
        settlement.setBedFee(bedFee);
        // 按类型汇总费用项：1-增值 2-餐饮 3-医疗 4-照护 5-赔偿 6-其他
        BigDecimal careFee = BigDecimal.ZERO;
        BigDecimal mealFee = BigDecimal.ZERO;
        BigDecimal serviceFee = BigDecimal.ZERO;
        BigDecimal otherFee = BigDecimal.ZERO;
        if(feeItemList!=null) {
            for(CheckinFeeItemVO item : feeItemList) {
                BigDecimal total = ChargeCalcKit.calcItemAmount(item.getChargeMode(), item.getUnitPrice(), item.getQuantity(), actualDays);
                Integer type = item.getItemType();
                if(type==null || type==0) {
                    // 0-基础康养费已按日单价计入 bedFee，避免重复计费
                    continue;
                } else if(type==1) {
                    serviceFee = serviceFee.add(total);
                } else if(type==2) {
                    mealFee = mealFee.add(total);
                } else if(type==3) {
                    careFee = careFee.add(total);
                } else if(type==4) {
                    serviceFee = serviceFee.add(total);
                } else if(type==5) {
                    otherFee = otherFee.add(total);
                } else {
                    otherFee = otherFee.add(total);
                }
            }
        }
        settlement.setCareFee(careFee);
        settlement.setMealFee(mealFee);
        settlement.setServiceFee(serviceFee);
        settlement.setOtherFee(otherFee);
        BigDecimal subtotal = bedFee.add(careFee).add(mealFee).add(serviceFee).add(otherFee).setScale(2, BigDecimal.ROUND_HALF_UP);
        settlement.setSubtotal(subtotal);
        settlement.setDiscount(BigDecimal.ZERO);
        settlement.setTotalAmount(subtotal);
        // 押金抵扣以实际已缴押金为准（未缴/部分缴纳时不应按名义押金全额抵扣），与结算收款同口径
        BigDecimal paidDeposit = checkin.getTotalDepositPayMoney()!=null ? checkin.getTotalDepositPayMoney()
                : (checkin.getDeposit()!=null ? checkin.getDeposit() : BigDecimal.ZERO);
        settlement.setTotalDeposit(paidDeposit.setScale(2, BigDecimal.ROUND_HALF_UP));
        settlement.setFinalAmount(subtotal.subtract(paidDeposit).setScale(2, BigDecimal.ROUND_HALF_UP));
        settlement.setDepositStatus(0);
        settlement.setActualPayment(BigDecimal.ZERO);
        settlement.setStatus(0);
        settlement.setCreateAt(new Date());
        settlement.setUpdateAt(new Date());
        settlement.setCreateBy(getUsername());
        settlement.setUpdateBy(getUsername());
        settlement.setIsDelete(0);
        settlementService.save(settlement);
        // 落库后按费用项重建明细，使每笔费用可追溯
        settlementDetailService.rebuild(settlement, checkin, feeItemList);
    }

    /**
     * 统计退住情况
     * 条件：预计退住日期 <= 今天 且 checkin_status=2（康养中）
     * 按 checkoutStatus 分组统计
     */
    @GetMapping("/getTodayChechoutStat")
    @PreAuthorize("@ss.hasPermi('care:checkin:list')")
    public R<Map<String, Object>> getTodayChechoutStat(@RequestParam Map<String, Object> params) throws ParseException {
        Date today = new Date();
        Date endOfDay = DateUtils.getEndOfDate(today);

        QueryWrapper<CheckinEntity> queryWrapper = new QueryWrapper<>();
        
        // 条件 1：预计退住日期 <= 今天
        queryWrapper.le("checkout_date", endOfDay);
        // 条件 2：康养中，退住状态为空视为待退住
        queryWrapper.eq("checkin_status", 2);
        queryWrapper.and(wrapper -> wrapper.isNull("checkout_status").or().le("checkout_status", 2));
        // checkout_status 为 tinyint(1)，JDBC 默认按 Boolean 返回，非 1/2 无法区分，需 CAST 成整型；
        // GROUP BY 必须与 SELECT 表达式完全一致，否则 only_full_group_by 会报错
        queryWrapper.select(
            "CAST(IFNULL(checkout_status, 0) AS SIGNED) AS checkout_status",
            "count(id) AS count"
        );
        queryWrapper.groupBy("CAST(IFNULL(checkout_status, 0) AS SIGNED)");

        List<Map<String, Object>> resultList = checkinService.listMaps(queryWrapper);

        Map<String, Object> result = new HashMap<>();
        Integer total = 0;
        result.put("status0", 0); // 待退住
        result.put("status1", 0); // 交接中
        result.put("status2", 0); // 已完成
        for (Map<String, Object> item : resultList) {
            int status = toInt(item.get("checkout_status"));
            Long count = ((Number) item.get("count")).longValue();
            if (status == 0) {
                result.put("status0", count.intValue());
            } else if (status == 1) {
                result.put("status1", count.intValue());
            } else if (status == 2) {
                result.put("status2", count.intValue());
            }
            total += count.intValue();
        }
        result.put("total", total);
        return R.data(result);
    }

    /** 兼容未发版小程序：明细列表字段回填当前登记的浅拷贝（断开自引用） */
    private void fillCompatResidentList(CheckinVO checkinVO) {
        CheckinVO.fillCompatResidentList(checkinVO);
    }

    private CheckinVO loadVO(Long id) {
        CheckinVO checkinVO = CheckinWrapper.build().entityVO(checkinService.getById(id));
        fillCompatResidentList(checkinVO);
        checkinVO.setFeeItemList(checkinFeeItemService.listByCheckin(id, checkinVO.getActualDays()));
        return checkinVO;
    }

    /**
     * 兼容不同 JDBC 类型映射（Integer/Long/BigDecimal/Boolean）取整数
     */
    private int toInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        return 0;
    }

}
