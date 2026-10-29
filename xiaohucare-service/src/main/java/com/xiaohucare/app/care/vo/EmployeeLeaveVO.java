package com.xiaohucare.app.care.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 员工离职VO
 */
@Data
@Schema(description = "员工离职VO")
public class EmployeeLeaveVO {

    @Schema(description = "员工ID", required = true)
    private Long id;

    @Schema(description = "离职日期", required = true)
    @NotNull(message = "离职日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date leaveDate;

    @Schema(description = "离职文件URL（多个用逗号分隔）")
    private String leaveFile;

    @Schema(description = "离职原因", required = true)
    @NotBlank(message = "离职原因不能为空")
    private String leaveReason;
}
