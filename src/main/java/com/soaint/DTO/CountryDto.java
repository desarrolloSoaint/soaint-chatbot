package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("Class Data Transfer Object (DTO) => Country")
public class CountryDto {

    @NotBlank
    @ApiModelProperty(value = "Campo nombre pais", required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
