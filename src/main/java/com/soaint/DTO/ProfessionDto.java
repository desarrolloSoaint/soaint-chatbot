package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@ApiModel("Class Data Transfer Object (DTO) => Profesión")
public class ProfessionDto {

    @NotNull
    @ApiModelProperty(value = "Campo profesión", required = true)
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
