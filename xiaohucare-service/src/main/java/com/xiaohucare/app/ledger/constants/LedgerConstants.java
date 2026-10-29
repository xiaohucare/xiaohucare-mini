package com.xiaohucare.app.ledger.constants;

/**
 * 台账/工单模块常量。
 *
 * <p>编码与前端 careEnums.js、数据库脚本第 7/9 节字典保持一致。</p>
 *
 * @author xiaohucare
 * @date 2026-10-27
 */
public class LedgerConstants {

	private LedgerConstants() {
	}

	/** 可自定义的机构维度字典类型(老人类型/服务需求项/隐患项) */
	public static final String[] CUSTOMIZABLE_DICT_TYPES = {
			"ledger_elder_type", "ledger_service_item", "ledger_risk_item"
	};

	/** 工单类别：探访 */
	public static final int ORDER_KIND_VISIT = 1;
	/** 工单类别：巡访 */
	public static final int ORDER_KIND_PATROL = 2;

	/** 探访工单状态：待分配 */
	public static final int VISIT_STATUS_WAIT_ASSIGN = 1;
	/** 探访工单状态：待上门 */
	public static final int VISIT_STATUS_WAIT_VISIT = 2;
	/** 探访工单状态：已完成 */
	public static final int VISIT_STATUS_FINISHED = 3;
	/** 探访工单状态：延期 */
	public static final int VISIT_STATUS_DELAY = 4;
	/** 探访工单状态：取消 */
	public static final int VISIT_STATUS_CANCEL = 5;
	/** 探访工单状态：重访 */
	public static final int VISIT_STATUS_REVISIT = 6;

	/** 来源：计划生成 */
	public static final int SOURCE_PLAN = 1;
	/** 来源：人工新建 */
	public static final int SOURCE_MANUAL = 2;
	/** 来源：风险预警触发(巡访) */
	public static final int SOURCE_WARN = 3;

	// ================= 巡访工单 =================
	/** 巡访类型：1日常安全 2高危专项 3投诉巡访 4隐患复查 */
	public static final int PATROL_TYPE_DAILY = 1;
	public static final int PATROL_TYPE_HIGH_RISK = 2;
	public static final int PATROL_TYPE_COMPLAINT = 3;
	public static final int PATROL_TYPE_RECHECK = 4;
	/** 巡访工单状态：1待分派 2待巡访 3处置中 4待复查 5已结案 6取消 */
	public static final int PATROL_STATUS_WAIT_DISPATCH = 1;
	public static final int PATROL_STATUS_WAIT_PATROL = 2;
	public static final int PATROL_STATUS_DISPOSING = 3;
	public static final int PATROL_STATUS_WAIT_REVIEW = 4;
	public static final int PATROL_STATUS_CLOSED = 5;
	public static final int PATROL_STATUS_CANCEL = 6;

	/** 流转动作 */
	public static final String ACTION_ASSIGN = "assign";
	public static final String ACTION_TRANSFER = "transfer";
	public static final String ACTION_REVOKE = "revoke";
	public static final String ACTION_SIGN = "sign";
	public static final String ACTION_START = "start";
	public static final String ACTION_FINISH = "finish";
	public static final String ACTION_DELAY = "delay";
	public static final String ACTION_CANCEL = "cancel";
	public static final String ACTION_REJECT = "reject";
	public static final String ACTION_CLOSE = "close";
	public static final String ACTION_REVIEW = "review";
	public static final String ACTION_RECTIFY = "rectify";

	/** 频次周期：每天 */
	public static final int FREQ_DAILY = 1;
	/** 频次周期：每周 */
	public static final int FREQ_WEEKLY = 2;
	/** 频次周期：每月 */
	public static final int FREQ_MONTHLY = 3;
}
