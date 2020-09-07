package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@ApiModel("Class Data Transfer Object (DTO) => Genero")
public class GenderDto {

    @NotNull
    @ApiModelProperty(value = "Campo género", required = true)
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "Campo de género abreviado")
    @Column(name = "abbreviation")
    private String abbreviation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
