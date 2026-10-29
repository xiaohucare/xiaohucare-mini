package com.xiaohucare.app.care.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.xiaohucare.app.care.entity.ChargeComboEntity;
import com.xiaohucare.app.care.entity.ResidentCardRecordEntity;
import com.xiaohucare.app.care.entity.AppointmentEntity;
import com.xiaohucare.app.care.entity.CheckinEntity;
import com.xiaohucare.app.care.entity.CheckinFeeItemEntity;
import com.xiaohucare.app.care.entity.SettlementDetailEntity;
import com.xiaohucare.app.care.entity.SettlementEntity;
import com.xiaohucare.app.care.service.AppointmentService;
import com.xiaohucare.app.care.service.BedService;
import com.xiaohucare.app.care.service.CheckinFeeItemService;
import com.xiaohucare.app.care.service.CheckinService;
import com.xiaohucare.app.care.service.ResidentArchiveSyncService;
import com.xiaohucare.app.care.service.SettlementDetailService;
import com.xiaohucare.app.care.service.SettlementService;
import com.xiaohucare.app.care.util.ChargeCalcKit;
import com.xiaohucare.app.care.vo.CheckinFeeItemVO;
import com.xiaohucare.app.care.vo.CheckinVO;
import com.xiaohucare.app.care.wrapper.CheckinWrapper;
import com.xiaohucare.app.care.mapper.ResidentCardRecordMapper;
import com.xiaohucare.app.care.service.ChargeComboService;
import com.xiaohucare.app.care.service.ResidentCardService;
import com.xiaohucare.app.care.vo.ResidentCardVO;
import com.xiaohucare.cache.AppIdCache;
import com.xiaohucare.common.enums.BusinessType;
import com.xiaohucare.common.utils.DateUtils;
import com.xiaohucare.common.utils.StringUtils;
import com.xiaohucare.framework.annotation.Log;
import com.xiaohucare.framework.core.api.utils.ApiAssert;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;
import com.xiaohucare.framework.core.utils.poi.ExcelUtil;
import com.xiaohucare.framework.core.vo.VPage;
import com.xiaohucare.framework.core.vo.VQueryKit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 到店登记表
 * 
 * @author xiaohucare
 * @date 2026-03-21 11:02:33
 */
@Slf4j
@RestController
@RequestMapping("intra/hotel/checkin")
public class CheckinController extends BaseController {
	@Autowired
	private CheckinService checkinService;
	@Autowired
	private ChargeComboService chargeComboService;
	@Autowired
	private SettlementService settlementService;
	@Autowired
	private SettlementDetailService settlementDetailService;
	@Autowired
	private ResidentArchiveSyncService residentArchiveSyncService;
	@Autowired
	private AppIdCache appIdCache;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private ResidentCardService residentCardService;
	@Autowired
	private BedService bedService;
	@Autowired
	private CheckinFeeItemService checkinFeeItemService;
	@Autowired
	private ResidentCardRecordMapper cardRecordMapper;

	/**
	 * 列表
	 */
	@Operation(summary = "列表")
	@Parameters({ @Parameter(name = "residentName", description = "主人姓名"),
			@Parameter(name = "contactPhone", description = "联系电话"),
			@Parameter(name = "checkinNo", description = "登记单号"),
			@Parameter(name = "checkinStatus", description = "入住状态"),
			@Parameter(name = "residentId", description = "长者档案ID"), })
	@GetMapping("/browse")
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	public R<VPage<CheckinVO>> browse(@RequestParam Map<String, Object> params) {
		QueryWrapper<CheckinEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("residentName", "contactPhone", "checkinNo", "checkinStatus"), CheckinEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("residentName")), "resident_name",
				params.get("residentName"));
		queryWrapper.like(StringUtils.isNotNull(params.get("contactPhone")), "contact_phone",
				params.get("contactPhone"));
		queryWrapper.like(StringUtils.isNotNull(params.get("checkinNo")), "checkin_no", params.get("checkinNo"));
		queryWrapper.eq(StringUtils.isNotNull(params.get("checkinStatus")), "checkin_status",
				params.get("checkinStatus"));
		queryWrapper.eq(StringUtils.isNotNull(params.get("residentId")), "resident_id", params.get("residentId"));
		IPage<CheckinEntity> pages = checkinService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(CheckinWrapper.build().toVPageVO(pages));
	}

	@GetMapping("/listStat")
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	public R<Map<String, Object>> listStat(@RequestParam Map<String, Object> params) throws ParseException {
		QueryWrapper<CheckinEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				CheckinEntity.class);
		
		queryWrapper.select("count(id) as totalCount");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		if (params.containsKey("createDate")) {
			queryWrapper.between("create_at",
					DateUtils.getStartOfDate(DateUtils.str_ToDate(params.get("createDate").toString())),
					DateUtils.getEndOfDate(DateUtils.str_ToDate(params.get("createDate").toString())));
		}
		Map<String, Object> stat = checkinService.getMap(queryWrapper);
		return R.data(stat);
	}

	@GetMapping("/browseSettle")
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	public R<VPage<CheckinVO>> browseSettle(@RequestParam Map<String, Object> params) {
		QueryWrapper<CheckinEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params,
				Arrays.asList("residentName", "contactPhone", "checkinNo", "checkinStatus"), CheckinEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("residentName")), "resident_name",
				params.get("residentName"));
		queryWrapper.like(StringUtils.isNotNull(params.get("contactPhone")), "contact_phone",
				params.get("contactPhone"));
		queryWrapper.like(StringUtils.isNotNull(params.get("checkinNo")), "checkin_no", params.get("checkinNo"));
		// 支持按结算状态筛选：传 settled 则精确匹配，否则默认只查待结算及以上
		if (params.containsKey("settled")) {
			queryWrapper.eq("settled", params.get("settled"));
		} else {
			queryWrapper.ge("settled", 1); // 待结算
		}
		IPage<CheckinEntity> pages = checkinService.page(VQueryKit.getPage(params), queryWrapper);
		return R.data(CheckinWrapper.build().toVPageVO(pages));
	}

	@GetMapping("/getSettleStat")
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	public R<Map<String, Object>> getSettleStat(@RequestParam Map<String, Object> params) throws ParseException {
		// 构建返回结果
		Map<String, Object> result = new HashMap<>();

		// 1. 统计当前待结算数量（status=1 且 settled=1-待结算）
		QueryWrapper<CheckinEntity> pendingQuery = new QueryWrapper<>();
//        pendingQuery.eq("status", 2);  // 交接中
		pendingQuery.eq("settled", 1); // 待结算
		long pendingCountLong = checkinService.count(pendingQuery);
		Integer pendingCount = (int) pendingCountLong;
		result.put("pendingCount", pendingCount);

		// 2. 统计今天结算金额
		Date today = new Date();
		Date startOfDay = DateUtils.getStartOfDate(today);
		Date endOfDay = DateUtils.getEndOfDate(today);

		QueryWrapper<SettlementEntity> todayQuery = new QueryWrapper<>();
		todayQuery.between("create_at", startOfDay, endOfDay);
		todayQuery.select("SUM(actual_payment) as todayAmount");
		Map<String, Object> todayStat = settlementService.getMap(todayQuery);
		BigDecimal todayAmount = (todayStat != null && todayStat.get("todayAmount") != null)
				? new BigDecimal(todayStat.get("todayAmount").toString())
				: BigDecimal.ZERO;
		result.put("todayAmount", todayAmount);

		// 3. 统计累计结算金额
		QueryWrapper<SettlementEntity> totalQuery = new QueryWrapper<>();
		totalQuery.select("SUM(actual_payment) as totalAmount");
		Map<String, Object> totalStat = settlementService.getMap(totalQuery);
		BigDecimal totalAmount = (totalStat != null && totalStat.get("totalAmount") != null)
				? new BigDecimal(totalStat.get("totalAmount").toString())
				: BigDecimal.ZERO;
		result.put("totalAmount", totalAmount);

		// 4. 统计累计结算单数
		QueryWrapper<SettlementEntity> countQuery = new QueryWrapper<>();
		long totalCountLong = settlementService.count(countQuery);
		Integer totalCount = (int) totalCountLong;
		result.put("totalCount", totalCount);

		return R.data(result);
	}

	/**
	 * 结算费用明细（按登记单查 e_care_settlement_detail，供结算列表“明细”抽屉逐笔展示）
	 */
	@Operation(summary = "结算费用明细")
	@GetMapping("/settlementItems/{checkinId}")
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	public R<List<SettlementDetailEntity>> settlementItems(@PathVariable("checkinId") Long checkinId) {
		CheckinEntity checkin = checkinService.getById(checkinId);
		if (checkin == null) {
			return R.error("登记单不存在");
		}
		QueryWrapper<SettlementDetailEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("checkin_id", checkinId);
		queryWrapper.orderByAsc("id");
		return R.data(settlementDetailService.list(queryWrapper));
	}

	/**
	 * 信息
	 */
	@Operation(summary = "信息")
	@GetMapping("/getSettleDetail/{id}")
	@PreAuthorize("@ss.hasPermi('care:checkin:detail')")
	public R<CheckinVO> getSettleDetail(@PathVariable("id") Long id) {
		CheckinEntity checkin = checkinService.getById(id);
		CheckinVO checkinVO = CheckinWrapper.build().entityVO(checkin);
		// 一人一单：费用项直接挂在本登记单上，按实际天数折算金额
		checkinVO.setFeeItemList(checkinFeeItemService.listByCheckin(id, checkin.getActualDays()));
		fillCompatResidentList(checkinVO);
		ResidentCardVO cardVO = residentCardService.getCardByResidentId(checkin.getResidentId());
		checkinVO.setCardVO(cardVO);
		// 回填结算单信息（退住时预生成或已结算），供结算页回显费用明细
		QueryWrapper<SettlementEntity> settleQuery = new QueryWrapper<>();
		settleQuery.eq("checkin_id", id);
		settleQuery.orderByDesc("id");
		settleQuery.last("limit 1");
		SettlementEntity settlement = settlementService.getOne(settleQuery, false);
		if (settlement != null) {
			checkinVO.setBedFee(settlement.getBedFee());
			checkinVO.setServiceFee(settlement.getServiceFee());
			checkinVO.setMealFee(settlement.getMealFee());
			checkinVO.setCareFee(settlement.getCareFee());
			checkinVO.setOtherFee(settlement.getOtherFee());
			checkinVO.setSubtotal(settlement.getSubtotal());
			checkinVO.setTotalAmount(settlement.getTotalAmount());
			checkinVO.setTotalDeposit(settlement.getTotalDeposit());
			checkinVO.setSettledDiscount(settlement.getDiscount());
			checkinVO.setSettledFinalAmount(settlement.getFinalAmount());
			checkinVO.setSettledActualPayment(settlement.getActualPayment());
			checkinVO.setSettledPaymentMethod(settlement.getPaymentMethod());
			checkinVO.setSettlementId(settlement.getId());
		}
		return R.data(checkinVO);
	}

	@Operation(summary = "结算费用并添加消费记录")
	@PostMapping("/submitSettle")
	@PreAuthorize("@ss.hasPermi('care:checkin:submit')")
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> submitSettle(@Valid @RequestBody CheckinVO checkin) {
		try {
			// 1. 查询登记信息
			CheckinEntity checkinEntity = checkinService.getById(checkin.getId());
			if (checkinEntity == null) {
				return R.error("登记信息不存在");
			}
			// 防重复结算：已结算的登记单直接拒绝，避免重复生成结算单/重复扣款
			if (checkinEntity.getSettled() != null && checkinEntity.getSettled() == 2) {
				return R.error("该登记单已结算，请勿重复提交");
			}

			// 2. 创建/更新结算单（优先复用退住时预生成的待结算单，避免重复）
			QueryWrapper<SettlementEntity> settleQuery = new QueryWrapper<>();
			settleQuery.eq("checkin_id", checkinEntity.getId());
			settleQuery.eq("status", 0);
			settleQuery.last("limit 1");
			SettlementEntity settlement = settlementService.getOne(settleQuery, false);
			if (settlement == null) {
				settlement = new SettlementEntity();
				settlement.setSettlementNo("STL" + System.currentTimeMillis());
				settlement.setCheckinId(checkinEntity.getId());
				settlement.setResidentId(checkinEntity.getResidentId());
				settlement.setCreateAt(new Date());
				settlement.setCreateBy(getUsername());
				settlement.setIsDelete(0);
			}

			// 费用信息（从前端传入；未传时保留预生成结算单已有金额，避免被置零）
			settlement.setBedFee(
					checkin.getBedFee() != null ? checkin.getBedFee() : defaultIfNull(settlement.getBedFee()));
			settlement.setServiceFee(checkin.getServiceFee() != null ? checkin.getServiceFee()
					: defaultIfNull(settlement.getServiceFee()));
			settlement.setMealFee(
					checkin.getMealFee() != null ? checkin.getMealFee() : defaultIfNull(settlement.getMealFee()));
			settlement.setCareFee(
					checkin.getCareFee() != null ? checkin.getCareFee() : defaultIfNull(settlement.getCareFee()));
			settlement.setOtherFee(
					checkin.getOtherFee() != null ? checkin.getOtherFee() : defaultIfNull(settlement.getOtherFee()));
			settlement.setSubtotal(
					checkin.getSubtotal() != null ? checkin.getSubtotal() : defaultIfNull(settlement.getSubtotal()));
			settlement
					.setDiscount(checkin.getSettledDiscount() != null ? checkin.getSettledDiscount() : BigDecimal.ZERO);
			settlement.setTotalAmount(
					checkin.getTotalAmount() != null ? checkin.getTotalAmount() : settlement.getSubtotal());
			settlement.setTotalDeposit(
					checkinEntity.getTotalDepositPayMoney() != null ? checkinEntity.getTotalDepositPayMoney()
							: BigDecimal.ZERO);
			BigDecimal finalAmount = checkin.getSettledFinalAmount() != null ? checkin.getSettledFinalAmount()
					: BigDecimal.ZERO;
			settlement.setFinalAmount(finalAmount);

			// 支付信息
			settlement.setPaymentMethod(checkin.getSettledPaymentMethod());
			settlement.setActualPayment(
					checkin.getSettledActualPayment() != null ? checkin.getSettledActualPayment() : BigDecimal.ZERO);
			settlement.setPaymentRemark(checkin.getRemarks());

			// 押金处理：应收（正数）→ 押金抵扣；应退（负数）→ 退还押金
			if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
				settlement.setDepositStatus(2); // 已退款
				settlement.setDepositRefundTime(new Date());
				// 同步登记押金状态为已退款
				checkinEntity.setTotalDepositPayState(2);
				checkinEntity.setTotalDepositRefundTime(new Date());
			} else {
				settlement.setDepositStatus(1); // 押金已抵扣
			}

			settlement.setStatus(1); // 已结算
			settlement.setUpdateAt(new Date());
			settlement.setUpdateBy(getUsername());

			if (!settlementService.saveOrUpdate(settlement)) {
				return R.error("创建结算单失败");
			}

			// 3. 更新登记状态为已结算，并回写结算结果
			checkinEntity.setSettled(2); // 已结算
			checkinEntity.setSettlementId(settlement.getId());
			checkinEntity.setSettledDiscount(settlement.getDiscount());
			checkinEntity.setSettledFinalAmount(settlement.getFinalAmount());
			checkinEntity.setSettledActualPayment(settlement.getActualPayment());
			checkinEntity.setSettledPaymentMethod(settlement.getPaymentMethod());
			checkinEntity.setUpdateAt(new Date());
			checkinEntity.setUpdateBy(getUsername());
			if (!checkinService.updateById(checkinEntity)) {
				return R.error("更新登记状态失败");
			}

			// 同步长者档案：结算完成置为已结算
			residentArchiveSyncService.markSettled(checkinEntity.getResidentId(), true);

			// 4. 如果是会员卡支付，添加消费记录
			if ("member_card".equalsIgnoreCase(checkin.getSettledPaymentMethod())
					&& checkin.getSettledActualPayment() != null
					&& checkin.getSettledActualPayment().compareTo(BigDecimal.ZERO) > 0) {

				// 获取会员卡信息
				ResidentCardVO cardVO = residentCardService.getCardByResidentId(checkinEntity.getResidentId());
				if (cardVO == null || cardVO.getStatus() != 1) {
					return R.error("会员卡不存在或已失效");
				}

				// 检查余额
				if (cardVO.getBalance().compareTo(checkin.getSettledActualPayment()) < 0) {
					return R.error("会员卡余额不足");
				}

				// 保存交易前余额
				BigDecimal beforeBalance = cardVO.getBalance();
				BigDecimal beforeGift = cardVO.getGiftBalance() != null ? cardVO.getGiftBalance() : BigDecimal.ZERO;

				// 更新会员卡余额（优先扣减赠送余额）
				BigDecimal amount = checkin.getSettledActualPayment();
				if (beforeGift.compareTo(BigDecimal.ZERO) > 0) {
					if (beforeGift.compareTo(amount) >= 0) {
						cardVO.setGiftBalance(beforeGift.subtract(amount));
					} else {
						BigDecimal remain = amount.subtract(beforeGift);
						cardVO.setGiftBalance(BigDecimal.ZERO);
						cardVO.setBalance(beforeBalance.subtract(remain));
					}
				} else {
					cardVO.setBalance(beforeBalance.subtract(amount));
				}
				cardVO.setTotalConsume(cardVO.getTotalConsume().add(amount));
				cardVO.setUpdateAt(new Date());

				if (!residentCardService.updateById(cardVO)) {
					return R.error("更新会员卡余额失败");
				}

				// 添加消费记录
				ResidentCardRecordEntity record = new ResidentCardRecordEntity();
				record.setTradeNo("TRD" + System.currentTimeMillis());
				record.setCardId(cardVO.getId());
				record.setCardNo(cardVO.getCardNo());
				record.setResidentId(cardVO.getResidentId());
				record.setResidentName(cardVO.getResidentName());
				record.setTradeType("consume");
				record.setAmount(amount);
				record.setGiftAmount(BigDecimal.ZERO);
				record.setBeforeBalance(beforeBalance);
				record.setAfterBalance(cardVO.getBalance());
				record.setBeforeGift(beforeGift);
				record.setAfterGift(cardVO.getGiftBalance());
				record.setServiceType("boarding");
				record.setCheckinId(checkinEntity.getId());
				record.setSettlementId(settlement.getId());
				record.setUseGift(beforeGift.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0);
				record.setPayMethod("member_card");
				record.setRemark("结算支付 - 登记编号：" + checkinEntity.getCheckinNo());
				record.setTradeTime(new Date());
				record.setOperator(getUsername());
				record.setCreateAt(new Date());
				record.setUpdateAt(new Date());
				record.setCreateBy(getUsername());
				record.setUpdateBy(getUsername());
				record.setIsDelete(0);

				if (cardRecordMapper.insert(record) <= 0) {
					return R.error("添加消费记录失败");
				}
			}

			// 5. 更新长者收费项目（已有项更新数量/单价，新增项落库）
			if (checkin.getFeeItemList() != null && !checkin.getFeeItemList().isEmpty()) {
				for (CheckinFeeItemVO feeItemVO : checkin.getFeeItemList()) {
					if (feeItemVO.getId() != null) {
						// 更新收费项目的数量和单价
						checkinFeeItemService.updateById(feeItemVO);
					} else if (feeItemVO.getItemId() != null) {
						// 结算阶段新增的收费项目
						feeItemVO.setCheckinId(checkinEntity.getId());
						feeItemVO.setIsOriginal(0);
						feeItemVO.setCreateAt(new Date());
						feeItemVO.setCreateBy(getUsername());
						feeItemVO.setIsDelete(0);
						checkinFeeItemService.saveOrUpdate(feeItemVO);
					}
				}
			}

			// 7. 以库中最终费用项为准重建结算明细，保证逐笔可追溯
			List<CheckinFeeItemVO> finalFeeItems = checkinFeeItemService.listByCheckin(checkinEntity.getId(),
					checkinEntity.getActualDays());
			settlementDetailService.rebuild(settlement, checkinEntity, finalFeeItems);

			// 7. 生成提成记录
			if (settlement.getActualPayment() != null && settlement.getActualPayment().compareTo(BigDecimal.ZERO) > 0) {
				try {
					// 获取负责员工ID
					Long handlerId = checkinEntity.getHandlerId();

					if (handlerId != null) {
						Date serviceDate = checkinEntity.getCheckoutDate() != null ? checkinEntity.getCheckoutDate()
								: new Date();
					} else {
						log.warn("康养登记未指定负责员工，跳过提成计算: checkinId={}", checkinEntity.getId());
					}
				} catch (Exception e) {
					log.error("康养登记提成计算异常: checkinId={}", checkinEntity.getId(), e);
				}
			}

			return R.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("结算失败：" + e.getMessage());
		}
	}

	/** 金额空值兜底 */
	private BigDecimal defaultIfNull(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	@Operation(summary = "导出")
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('care:checkin:list')")
	@PostMapping("/toExport")
	public void toExport(HttpServletResponse response, @RequestParam Map<String, Object> params) {
		QueryWrapper<CheckinEntity> queryWrapper = VQueryKit.getQueryWrapperByMap(params, Arrays.asList("name"),
				CheckinEntity.class);
		
		queryWrapper.orderByDesc("id");
		queryWrapper.like(StringUtils.isNotNull(params.get("name")), "name", params.get("name"));
		List<CheckinEntity> checkinList = checkinService.list(queryWrapper);
		ExcelUtil<CheckinEntity> util = new ExcelUtil<>(CheckinEntity.class);
		util.exportExcel(response, checkinList, "参数数据");
	}

	/**
	 * 信息
	 */
	@Operation(summary = "信息")
	@GetMapping("/detail/{id}")
	@PreAuthorize("@ss.hasPermi('care:checkin:detail')")
	public R<CheckinVO> detail(@PathVariable("id") Long id) {
		CheckinEntity checkin = checkinService.getById(id);
		CheckinVO checkinVO = CheckinWrapper.build().entityVO(checkin);
		checkinVO.setFeeItemList(checkinFeeItemService.listByCheckin(id, checkin.getActualDays()));
		fillCompatResidentList(checkinVO);
		return R.data(checkinVO);
	}

	/**
	 * 保存&更新
	 */
	@Operation(summary = "保存&更新")
	@PostMapping("/submit")
	@PreAuthorize("@ss.hasPermi('care:checkin:submit')")
	@Transactional(rollbackFor = Exception.class)
	public R<CheckinVO> submit(@Valid @RequestBody CheckinVO checkin) {
		if (checkin.getId() == null) {
			checkin.setCreateAt(new Date());
			checkin.setIsDelete(0);
			String checkNo = appIdCache.genCommonIdDayEndRadom("CHK", "CHK", 1);
			checkin.setCheckinNo(checkNo);
			checkin.setCheckinStatus(0);
			if (checkin.getCheckoutStatus() == null) {
				checkin.setCheckoutStatus(0);
			}
			checkin.setCreateBy(getUsername());
			checkin.setOperatorName(getUsername());
		}
		checkin.setUpdateAt(new Date());
		if (checkin.getResidentId() == null) {
			ApiAssert.error("请选择入住长者");
		}
		// 计费周期/单价快照 + 折算日单价 + 押金（来自套餐）
		if (checkin.getComboId() != null) {
			ChargeComboEntity combo = chargeComboService.getById(checkin.getComboId());
			if (combo != null) {
				// 按用户选定的计费周期（年/月/天）从套餐三价取周期单价（缺价向更细粒度降级）
				ChargeCalcKit.ComboPrice price = ChargeCalcKit.priceForCycle(
						checkin.getChargeCycle() != null ? checkin.getChargeCycle() : combo.getChargeCycle(),
						combo.getYearPrice(), combo.getMonthPrice(), combo.getDayPrice());
				checkin.setChargeCycle(price.getCycle());
				checkin.setPeriodPrice(price.getPeriodPrice());
				// 回填套餐年/月/天三价快照，结算时按实际天数逐段重算（不满年少按月、不满月按天）
				checkin.setComboYearPrice(combo.getYearPrice());
				checkin.setComboMonthPrice(combo.getMonthPrice());
				checkin.setComboDayPrice(combo.getDayPrice());
				if (combo.getDepositAmount() != null) {
					checkin.setDeposit(combo.getDepositAmount());
					checkin.setTotalDeposit(combo.getDepositAmount());
				}
			}
		}
		// 预计退住日期 = 入住日期 + 计划时长（chargeNum × 计费周期单位天数）
		if (checkin.getCheckinDate() != null && checkin.getChargeNum() != null) {
			checkin.setCheckoutDate(DateUtils.addDays(checkin.getCheckinDate(),
					ChargeCalcKit.plannedDays(checkin.getChargeNum(), checkin.getChargeCycle())));
		}
		// 记录换床前的床位
		Long oldBedId = null;
		if (checkin.getId() != null) {
			CheckinEntity pre = checkinService.getById(checkin.getId());
			if (pre != null && pre.getBedId() != null) {
				oldBedId = pre.getBedId();
			}
		}
		if (!checkinService.saveOrUpdate(checkin)) {
			return R.error("数据错误");
		}
		// 占床 / 换床联动（床位状态与房间入住人数由 BedService 统一维护）
		Long newBedId = checkin.getBedId();
		if (newBedId != null && !newBedId.equals(oldBedId)) {
			if (oldBedId != null) {
				bedService.changeBed(oldBedId, newBedId, checkin.getResidentId(), checkin.getResidentName(),
						checkin.getId());
			} else {
				bedService.occupyBed(newBedId, checkin.getResidentId(), checkin.getResidentName(), checkin.getId());
			}
		}
		// 本单费用项（一人一单，直接挂登记单）
		saveFeeItems(checkin.getId(), checkin.getFeeItemList());
		if (checkin.getAppointmentId() != null && checkin.getAppointmentId() > 0) {
			AppointmentEntity appointment = appointmentService.getById(checkin.getAppointmentId());
			if (appointment != null && appointment.getStatus() != null && appointment.getStatus() != 3) {
				appointment.setStatus(3);
				appointment.setUpdateAt(new Date());
				appointmentService.updateById(appointment);
			}
		}
		CheckinVO result = CheckinWrapper.build().entityVO(checkinService.getById(checkin.getId()));
		result.setFeeItemList(checkinFeeItemService.listByCheckin(checkin.getId(), checkin.getActualDays()));
		fillCompatResidentList(result);
		return R.ok(result);
	}

	/** 保存费用项并逻辑删除本次未提交的项目，保证移除后的项目不再参与结算 */
	private void saveFeeItems(Long checkinId, List<CheckinFeeItemVO> feeItemVoList) {
		List<Long> keepIds = new ArrayList<>();
		if (feeItemVoList != null) {
			for (CheckinFeeItemVO feeItem : feeItemVoList) {
				// 防止前端把收费项目档案 ID 误当费用项记录 ID 传回，造成串改他人数据
				if (feeItem.getId() != null) {
					CheckinFeeItemEntity existed = checkinFeeItemService.getById(feeItem.getId());
					if (existed == null || !checkinId.equals(existed.getCheckinId())) {
						feeItem.setId(null);
					}
				}
				if (feeItem.getId() == null) {
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
		if (!keepIds.isEmpty()) {
			removeQuery.notIn("id", keepIds);
		}
		checkinFeeItemService.remove(removeQuery);
	}

	/** 兼容未发版小程序：明细列表字段回填当前登记的浅拷贝（断开自引用） */
	private void fillCompatResidentList(CheckinVO checkinVO) {
		CheckinVO.fillCompatResidentList(checkinVO);
	}

	@Operation(summary = "扣除保证金完成登记")
	@PostMapping("/submitDepositAndComplete")
	@PreAuthorize("@ss.hasPermi('care:checkin:submit')")
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> submitDepositAndComplete(@Valid @RequestBody CheckinVO checkin) {
		try {
			// 1. 更新押金支付信息
			CheckinEntity checkinEntity = checkinService.getById(checkin.getId());
			if (checkinEntity == null) {
				return R.error("登记信息不存在");
			}

			// 更新押金相关字段
			checkinEntity.setTotalDepositPayMoney(checkin.getTotalDepositPayMoney());
			checkinEntity.setTotalDepositPaymentMethod(checkin.getTotalDepositPaymentMethod());
			checkinEntity.setTotalDepositPayState(checkin.getTotalDepositPayState());
			checkinEntity.setCheckinStatus(2); // 押金确认后转为康养中（退住完成后变更为 3-已退住）
			checkinEntity.setTotalDeposit(checkinEntity.getDeposit());
			if (checkinEntity.getCheckoutStatus() == null) {
				checkinEntity.setCheckoutStatus(0);
			}
			checkinEntity.setUpdateAt(new Date());
			checkinEntity.setUpdateBy(getUsername());

			if (!checkinService.updateById(checkinEntity)) {
				return R.error("更新登记信息失败");
			}

			// 同步长者档案：置为在住，并快照房间/床位与入住时间
			residentArchiveSyncService.markCheckedIn(checkinEntity.getResidentId(), checkinEntity);

			// 3. 如果是会员卡支付，添加消费记录
			if ("member_card".equals(checkin.getTotalDepositPaymentMethod())
					&& checkin.getTotalDepositPayMoney() != null
					&& checkin.getTotalDepositPayMoney().compareTo(BigDecimal.ZERO) > 0) {

				// 获取会员卡信息
				ResidentCardVO cardVO = residentCardService.getCardByResidentId(checkinEntity.getResidentId());
				if (cardVO == null || cardVO.getStatus() != 1) {
					return R.error("会员卡不存在或已失效");
				}

				// 检查余额
				if (cardVO.getBalance().compareTo(checkin.getTotalDepositPayMoney()) < 0) {
					return R.error("会员卡余额不足");
				}

				// 保存交易前余额
				BigDecimal beforeBalance = cardVO.getBalance();
				BigDecimal beforeGift = cardVO.getGiftBalance() != null ? cardVO.getGiftBalance() : BigDecimal.ZERO;

				// 更新会员卡余额（优先扣减赠送余额）
				BigDecimal amount = checkin.getTotalDepositPayMoney();
				if (beforeGift.compareTo(BigDecimal.ZERO) > 0) {
					if (beforeGift.compareTo(amount) >= 0) {
						// 赠送余额足够
						cardVO.setGiftBalance(beforeGift.subtract(amount));
					} else {
						// 赠送余额不足，扣减部分现金余额
						BigDecimal remain = amount.subtract(beforeGift);
						cardVO.setGiftBalance(BigDecimal.ZERO);
						cardVO.setBalance(beforeBalance.subtract(remain));
					}
				} else {
					cardVO.setBalance(beforeBalance.subtract(amount));
				}
				cardVO.setTotalConsume(cardVO.getTotalConsume().add(amount));
				cardVO.setUpdateAt(new Date());

				if (!residentCardService.updateById(cardVO)) {
					return R.error("更新会员卡余额失败");
				}

				// 添加消费记录
				ResidentCardRecordEntity record = new ResidentCardRecordEntity();
				record.setTradeNo("TRD" + System.currentTimeMillis());
				record.setCardId(cardVO.getId());
				record.setCardNo(cardVO.getCardNo());
				record.setResidentId(cardVO.getResidentId());
				record.setResidentName(cardVO.getResidentName());
				record.setTradeType("consume");
				record.setAmount(amount);
				record.setGiftAmount(BigDecimal.ZERO);
				record.setBeforeBalance(beforeBalance);
				record.setAfterBalance(cardVO.getBalance());
				record.setBeforeGift(beforeGift);
				record.setAfterGift(cardVO.getGiftBalance());
				record.setServiceType("boarding"); // 康养服务
				record.setCheckinId(checkinEntity.getId());
				record.setUseGift(beforeGift.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0);
				record.setPayMethod("member_card");
				record.setRemark("押金支付 - 登记编号：" + checkinEntity.getCheckinNo());
				record.setTradeTime(new Date());
				record.setOperator(getUsername());
				record.setCreateAt(new Date());
				record.setUpdateAt(new Date());
				record.setCreateBy(getUsername());
				record.setUpdateBy(getUsername());
				record.setIsDelete(0);

				if (cardRecordMapper.insert(record) <= 0) {
					return R.error("添加消费记录失败");
				}
			}

			return R.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("操作失败：" + e.getMessage());
		}
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@PostMapping("/delete")
	@PreAuthorize("@ss.hasPermi('care:checkin:delete')")
	public R<Boolean> delete(@RequestBody Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		// 一人一单：删除登记时一并逻辑删除本单费用项
		QueryWrapper<CheckinFeeItemEntity> feeQuery = new QueryWrapper<>();
		feeQuery.in("checkin_id", idList);
		checkinFeeItemService.remove(feeQuery);
		return R.ok(checkinService.removeByIds(idList));
	}

}
