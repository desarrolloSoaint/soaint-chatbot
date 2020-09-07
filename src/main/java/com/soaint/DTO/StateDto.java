package com.soaint.DTO;

import com.soaint.entity.Country;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Class Data Transfer Object (DTO) => Estados")
public class StateDto {

    @ApiModelProperty(value = "Campo nombre estado", required = true)
    private String name;
    @ApiModelProperty(value = "Campo relacion con país", required = true)
    Country country_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Country country_id) {
        this.country_id = country_id;
    }
}
