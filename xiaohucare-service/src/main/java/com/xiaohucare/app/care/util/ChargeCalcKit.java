package com.xiaohucare.app.care.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计费换算工具：统一「按年/按月/按天」折算口径，保证登记预估、交接页、结算单三处金额一致
 * <p>
 * 折算规则（按天折算、多退少补）：年价 / 365、月价 / 30，其余周期原值返回
 * 
 * @author xiaohucare
 * @date 2026-10-24
 */
public class ChargeCalcKit {

	/** 计费周期：按年 */
	public static final int CYCLE_YEAR = 0;
	/** 计费周期：按月 */
	public static final int CYCLE_MONTH = 1;
	/** 计费周期：按天 */
	public static final int CYCLE_DAY = 2;

	/** 计费方式：按天 */
	public static final int MODE_PER_DAY = 0;
	/** 计费方式：按次 */
	public static final int MODE_PER_TIME = 1;
	/** 计费方式：固定 */
	public static final int MODE_FIXED = 2;
	/** 计费方式：按重 */
	public static final int MODE_PER_WEIGHT = 3;
	/** 计费方式：按年 */
	public static final int MODE_PER_YEAR = 4;
	/** 计费方式：按月 */
	public static final int MODE_PER_MONTH = 5;

	/** 一年折算天数 */
	private static final BigDecimal DAYS_OF_YEAR = BigDecimal.valueOf(365);
	/** 一月折算天数 */
	private static final BigDecimal DAYS_OF_MONTH = BigDecimal.valueOf(30);
	/** 判定「满一年」的天数阈值 */
	private static final int DAYS_THRESHOLD_YEAR = 365;
	/** 判定「满月」的天数阈值 */
	private static final int DAYS_THRESHOLD_MONTH = 30;

	private ChargeCalcKit() {
	}

	/**
	 * 周期单价折算为日单价
	 * 
	 * @param chargeCycle 0-按年 1-按月 2-按天（null 按天处理）
	 * @param periodPrice 周期单价
	 * @return 日单价，保留2位小数
	 */
	public static BigDecimal toDailyPrice(Integer chargeCycle, BigDecimal periodPrice) {
		if (periodPrice == null) {
			return BigDecimal.ZERO;
		}
		int cycle = chargeCycle == null ? CYCLE_DAY : chargeCycle;
		if (cycle == CYCLE_YEAR) {
			return divide(periodPrice, DAYS_OF_YEAR);
		}
		if (cycle == CYCLE_MONTH) {
			return divide(periodPrice, DAYS_OF_MONTH);
		}
		return scale(periodPrice);
	}

	/**
	 * 费用项金额计算
	 * <ul>
	 * <li>按天：单价 × 数量 × 住养天数</li>
	 * <li>按年/按月：单价折算为日价后 × 数量 × 住养天数（多退少补）</li>
	 * <li>按次/固定/按重：单价 × 数量</li>
	 * </ul>
	 * 
	 * @param chargeMode 计费方式
	 * @param unitPrice  单价（按年/按月时为周期价）
	 * @param quantity   数量
	 * @param actualDays 实际住养天数
	 */
	public static BigDecimal calcItemAmount(Integer chargeMode, BigDecimal unitPrice, Integer quantity, Integer actualDays) {
		if (unitPrice == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal qty = BigDecimal.valueOf(quantity == null ? 1 : quantity);
		int mode = chargeMode == null ? MODE_PER_TIME : chargeMode;
		if (mode == MODE_PER_DAY) {
			return scale(unitPrice.multiply(qty).multiply(days(actualDays)));
		}
		if (mode == MODE_PER_YEAR || mode == MODE_PER_MONTH) {
			BigDecimal daily = mode == MODE_PER_YEAR ? divide(unitPrice, DAYS_OF_YEAR) : divide(unitPrice, DAYS_OF_MONTH);
			return scale(daily.multiply(qty).multiply(days(actualDays)));
		}
		return scale(unitPrice.multiply(qty));
	}

	/**
	 * 住养天数兜底：不足 1 天按 1 天计
	 */
	public static BigDecimal days(Integer actualDays) {
		return BigDecimal.valueOf(actualDays == null || actualDays < 1 ? 1 : actualDays);
	}

	/**
	 * 按预计住养天数选择计费周期（康养主按年、按月收费）：
	 * 满一年(>=365)按年、不满一年但满月(>=30)按月、不满一月按天。
	 * 
	 * @param estimatedDays 预计住养天数（null 视为 0，按天）
	 * @return CYCLE_YEAR / CYCLE_MONTH / CYCLE_DAY
	 */
	public static int resolveCycleByDays(Integer estimatedDays) {
		int d = estimatedDays == null ? 0 : estimatedDays;
		if (d >= DAYS_THRESHOLD_YEAR) {
			return CYCLE_YEAR;
		}
		if (d >= DAYS_THRESHOLD_MONTH) {
			return CYCLE_MONTH;
		}
		return CYCLE_DAY;
	}

	/**
	 * 根据套餐的年/月/天三价 + 预计天数，解析出适用周期与周期单价，并折算日单价。
	 * <ul>
	 * <li>按预计天数选定周期（满年/满月/否则天）；</li>
	 * <li>选定周期无对应价时，回退到更细粒度（天→月→年）的可用价，并同步调整周期以保证折算口径正确；</li>
	 * <li>三价均未配置（全空）时，沿用旧的 chargeCycle 且价格视为 0。</li>
	 * </ul>
	 */
	public static ComboPrice fromCombo(Integer legacyCycle,
			BigDecimal yearPrice, BigDecimal monthPrice, BigDecimal dayPrice, Integer estimatedDays) {
		boolean granular = positive(yearPrice) || positive(monthPrice) || positive(dayPrice);
		if (!granular) {
			int cycle = legacyCycle == null ? CYCLE_DAY : legacyCycle;
			return new ComboPrice(cycle, BigDecimal.ZERO, BigDecimal.ZERO);
		}
		int cycle = resolveCycleByDays(estimatedDays);
		BigDecimal periodPrice;
		if (cycle == CYCLE_YEAR) {
			if (positive(yearPrice)) {
				periodPrice = yearPrice;
			} else if (positive(monthPrice)) {
				periodPrice = monthPrice;
				cycle = CYCLE_MONTH;
			} else {
				periodPrice = dayPrice;
				cycle = CYCLE_DAY;
			}
		} else if (cycle == CYCLE_MONTH) {
			if (positive(monthPrice)) {
				periodPrice = monthPrice;
			} else if (positive(dayPrice)) {
				periodPrice = dayPrice;
				cycle = CYCLE_DAY;
			} else {
				periodPrice = yearPrice;
				cycle = CYCLE_YEAR;
			}
		} else {
			if (positive(dayPrice)) {
				periodPrice = dayPrice;
			} else if (positive(monthPrice)) {
				periodPrice = monthPrice;
				cycle = CYCLE_MONTH;
			} else {
				periodPrice = yearPrice;
				cycle = CYCLE_YEAR;
			}
		}
		return new ComboPrice(cycle, periodPrice, toDailyPrice(cycle, periodPrice));
	}

	/**
	 * 按「已选计费周期」从套餐年/月/天三价取周期单价（登记时长由 chargeCycle + chargeNum 表达，不再由天数反推）。
	 * <ul>
	 * <li>所选周期有对应价则直接用；</li>
	 * <li>所选周期缺价时回退到更细粒度（年→月→天）的可用价，并同步调整周期以保证折算口径正确；</li>
	 * <li>三价均未配置（全空）时，沿用所选周期且价格视为 0。</li>
	 * </ul>
	 */
	public static ComboPrice priceForCycle(Integer chosenCycle,
			BigDecimal yearPrice, BigDecimal monthPrice, BigDecimal dayPrice) {
		int cycle = chosenCycle == null ? CYCLE_DAY : chosenCycle;
		boolean granular = positive(yearPrice) || positive(monthPrice) || positive(dayPrice);
		if (!granular) {
			return new ComboPrice(cycle, BigDecimal.ZERO, BigDecimal.ZERO);
		}
		BigDecimal periodPrice;
		if (cycle == CYCLE_YEAR) {
			if (positive(yearPrice)) {
				periodPrice = yearPrice;
			} else if (positive(monthPrice)) {
				periodPrice = monthPrice;
				cycle = CYCLE_MONTH;
			} else {
				periodPrice = dayPrice;
				cycle = CYCLE_DAY;
			}
		} else if (cycle == CYCLE_MONTH) {
			if (positive(monthPrice)) {
				periodPrice = monthPrice;
			} else if (positive(dayPrice)) {
				periodPrice = dayPrice;
				cycle = CYCLE_DAY;
			} else {
				periodPrice = yearPrice;
				cycle = CYCLE_YEAR;
			}
		} else {
			if (positive(dayPrice)) {
				periodPrice = dayPrice;
			} else if (positive(monthPrice)) {
				periodPrice = monthPrice;
				cycle = CYCLE_MONTH;
			} else {
				periodPrice = yearPrice;
				cycle = CYCLE_YEAR;
			}
		}
		return new ComboPrice(cycle, periodPrice, toDailyPrice(cycle, periodPrice));
	}

	/** 计费周期单位对应的天数：年 365、月 30、天 1（null/未知按天） */
	public static int unitDays(Integer cycle) {
		if (cycle == null) {
			return 1;
		}
		if (cycle == CYCLE_YEAR) {
			return DAYS_OF_YEAR.intValue();
		}
		if (cycle == CYCLE_MONTH) {
			return DAYS_OF_MONTH.intValue();
		}
		return 1;
	}

	/** 计划总天数 = 数量 × 周期单位天数（用于登记预估与预计退住日期换算） */
	public static int plannedDays(Integer chargeNum, Integer cycle) {
		int n = chargeNum == null || chargeNum < 0 ? 0 : chargeNum;
		return n * unitDays(cycle);
	}

	/**
	 * 套餐基础费（康养费）逐段累加计费：
	 * 把总天数按 1年=365天、1月=30天 拆成「年 + 月 + 天」，
	 * bedFee = 年价×年 + 月价×月 + 日价×天；缺某档价时向更细粒度降级（年→12月、月→30天、无日价时按月/年价折算日价）。
	 * 
	 * @param yearPrice  套餐年价快照（元/年）
	 * @param monthPrice 套餐月价快照（元/月）
	 * @param dayPrice   套餐日价快照（元/天）
	 * @param totalDays  计费天数（登记传预计天数、结算传实际天数）
	 * @return 基础费，保留 2 位小数
	 */
	public static BigDecimal calcBedFee(BigDecimal yearPrice, BigDecimal monthPrice, BigDecimal dayPrice, Integer totalDays) {
		int total = totalDays == null || totalDays < 0 ? 0 : totalDays;
		int years = total / 365;
		int rem = total % 365;
		int months = rem / 30;
		int days = rem % 30;
		BigDecimal fee = BigDecimal.ZERO;
		// 年段：无年价则把年折算进月（1年=12月）
		if (positive(yearPrice)) {
			fee = fee.add(yearPrice.multiply(BigDecimal.valueOf(years)));
		} else {
			months += years * 12;
		}
		// 月段：无月价则把月折算进天（1月=30天）
		if (positive(monthPrice)) {
			fee = fee.add(monthPrice.multiply(BigDecimal.valueOf(months)));
		} else {
			days += months * 30;
		}
		// 天段：无日价则用月/年价折算日价
		if (positive(dayPrice)) {
			fee = fee.add(dayPrice.multiply(BigDecimal.valueOf(days)));
		} else {
			BigDecimal daily = positive(monthPrice) ? divide(monthPrice, DAYS_OF_MONTH)
					: positive(yearPrice) ? divide(yearPrice, DAYS_OF_YEAR) : BigDecimal.ZERO;
			fee = fee.add(daily.multiply(BigDecimal.valueOf(days)));
		}
		return scale(fee);
	}

	/** 是否为有效正数价 */
	private static boolean positive(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 套餐周期价解析结果：周期类型 + 周期单价快照 + 折算日单价
	 */
	public static class ComboPrice {
		private final int cycle;
		private final BigDecimal periodPrice;
		private final BigDecimal dailyPrice;

		public ComboPrice(int cycle, BigDecimal periodPrice, BigDecimal dailyPrice) {
			this.cycle = cycle;
			this.periodPrice = periodPrice;
			this.dailyPrice = dailyPrice;
		}

		public int getCycle() {
			return cycle;
		}

		public BigDecimal getPeriodPrice() {
			return periodPrice;
		}

		public BigDecimal getDailyPrice() {
			return dailyPrice;
		}
	}

	private static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
		return value.divide(divisor, 2, RoundingMode.HALF_UP);
	}

	private static BigDecimal scale(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}
}
