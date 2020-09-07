package com.soaint.DTO;

import com.soaint.entity.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@ApiModel("Class Data Transfer Object (DTO) => Color")
public class CbColorDto {

    @ApiModelProperty(value = "Campo Color")
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
