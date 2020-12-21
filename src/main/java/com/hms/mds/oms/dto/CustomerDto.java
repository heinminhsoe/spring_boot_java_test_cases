package com.hms.mds.oms.dto;

import com.hms.mds.oms.util.ValidationErrors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerDto {
    @ApiModelProperty(readOnly = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "Customer name", required = true, example = "Fribarg")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    private String name;

    @ApiModelProperty(value = "Email", required = true, example = "fribarg@gmail.com")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    @Email(message = ValidationErrors.MSG_INVALID_EMAIL)
    private String email;

    @ApiModelProperty(value = "Phone number", required = true, example = "+959667711675")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    private String phone;

    @ApiModelProperty(value = "Full address", required = true, example = "No. 155D, Than St., 11 Quarter, Hlaing Tsp., Yangon")
    @NotBlank(message = ValidationErrors.MSG_NOT_BLANK)
    private String address;
}
