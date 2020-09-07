package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="l_country")
@ApiModel("Modelo => COUNTRY")
public class Country {

    @SequenceGenerator(
            name = "CountrySeq",
            sequenceName = "L_COUNTRY_SEQ",
            initialValue = 247,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo Id Autoincrementable del Menu")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CountrySeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo name Descripcion")
    @Column(name="name")
    private String name;

    public Country(){

    }

    public Country(Long id){
        this.id = id;
    }

    public Country(@NotNull String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
