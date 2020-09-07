package com.soaint.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="l_currency")
@ApiModel("Modelo => Moneda")
public class Currency {

    @SequenceGenerator(
            name = "CurrencySeq",
            sequenceName = "L_CURRENCY_SEQ",
            initialValue = 18,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CurrencySeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo de la moneda", required = true)
    @Column(name="money")
    private String money;

    @ApiModelProperty(value = "Campo de moneda abreviada")
    @Column(name="abbreviation")
    private String abbreviation;

    @ApiModelProperty(value = "Campo del simbolo de la moneda")
    @Column(name="symbol")
    private String symbol;

    public Currency(){

    }

    public Currency(String money, String abbreviation, String symbol) {
        this.money = money;
        this.abbreviation = abbreviation;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id) &&
                Objects.equals(money, currency.money) &&
                Objects.equals(abbreviation, currency.abbreviation) &&
                Objects.equals(symbol, currency.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money, abbreviation, symbol);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", money='" + money + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}