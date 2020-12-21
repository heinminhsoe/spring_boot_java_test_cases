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

@ApiModel

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductDto {
    @ApiModelProperty(readOnly = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "Product name", required = true, example = "iPhone")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    private String name;

    @ApiModelProperty(value = "Unit price", required = true, example = "100000")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private Double unitPrice;
}
