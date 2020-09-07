package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("Class Data Transfer Object (DTO) => Moneda")
public class CurrencyDto {

    @NotBlank
    @ApiModelProperty(value = "Campo nombre de moneda", required = true)
    private String money;
    @ApiModelProperty(value = "Campo abreviación de moneda")
    private String abbreviation;
    @ApiModelProperty(value = "Campo simbolo de moneda")
    private String symbol;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
