package com.hms.mds.oms.dto;

import com.hms.mds.oms.util.ValidationErrors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderDto {
    @ApiModelProperty(readOnly = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "Order number", required = true, example = "10001")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private Long orderNo;

    @ApiModelProperty(value = "Order date", required = true, example = "2020-12-21")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private LocalDate orderDate;

    @ApiModelProperty(value = "Status", required = true, example = "PENDING")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    private String status;

    @ApiModelProperty(value = "Customer ID", required = false, example = "2")
    private Integer customerId;

    @ApiModelProperty(value = "Fribarg", readOnly = true, example = "iPhone")
    private String customerName;
}

