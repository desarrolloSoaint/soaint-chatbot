package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="l_sta_civil")
@ApiModel("Modelo => Estado civil")
public class StaCivil {

    @SequenceGenerator(
            name = "Sta_CivilSeq",
            sequenceName = "L_STA_CIVIL_SEQ",
            initialValue = 5,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Sta_CivilSeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo estado civil", required = true)
    @Column(name="name")
    private String name;

    @ApiModelProperty(value = "Campo estado civil abreviado")
    @Column(name="abbreviation")
    private String abbreviation;

    public StaCivil(){

    }

    public StaCivil(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaCivil staCivil = (StaCivil) o;
        return Objects.equals(id, staCivil.id) &&
                Objects.equals(name, staCivil.name) &&
                Objects.equals(abbreviation, staCivil.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abbreviation);
    }

    @Override
    public String toString() {
        return "StaCivil{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
