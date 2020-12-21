package com.hms.mds.oms.dto;

import com.hms.mds.oms.util.ValidationErrors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderItemsDto {
    @ApiModelProperty(readOnly = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "Quantity", required = true, example = "2")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private Integer quantity;

    @ApiModelProperty(value = "Total price", required = true, example = "100000")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private Double total;

    @ApiModelProperty(value = "Product ID", required = true, example = "2")
    @NotNull(message = ValidationErrors.MSG_NOT_NULL)
    private Integer productId;

    @ApiModelProperty(value = "Product Name", readOnly = true, example = "iPhone")
    private String productName;
}

