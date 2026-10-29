package com.xiaohucare.app.care.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaohucare.framework.annotation.Excel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 员工入职VO
 */
@Data
@Schema(description = "员工入职VO")
public class EmployeeHireVO {

    @Schema(description = "员工ID", required = true)
    private Long id;

    @Schema(description = "入职日期", required = true)
    @NotNull(message = "入职日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hireDate;

    @Schema(description = "入职文件URL（多个用逗号分隔）")
    private String hireFile;
    private String hireReferee;
    private String hireRefereeReason;

    @Schema(description = "入职备注")
    private String remark;
    /**
	 * 开户银行
	 */
	@Schema(description = "开户银行")
	@Excel(name = "开户银行")
	private String bankName;
	/**
	 * 银行账号（用于发放工资）
	 */
	@Schema(description = "银行账号（用于发放工资）")
	@Excel(name = "银行账号（用于发放工资）")
	private String bankAccount;
	/**
	 * 基本工资
	 */
	@Schema(description = "基本工资")
	@Excel(name = "基本工资")
	private BigDecimal baseSalary;
	
	
	private Integer level;
	private String position;
}
