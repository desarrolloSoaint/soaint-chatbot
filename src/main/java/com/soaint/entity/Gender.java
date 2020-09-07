package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="l_gender")
@ApiModel("Modelo => Género")
public class Gender {
    @SequenceGenerator(
            name = "GenderSeq",
            sequenceName = "L_GENDER_SEQ",
            initialValue = 4,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GenderSeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo género", required = true)
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "Campo de género abreviado")
    @Column(name = "abbreviation")
    private String abbreviation;

    public Gender() {
    }

    public Gender(String name, String abbreviation) {
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
        Gender gender = (Gender) o;
        return Objects.equals(id, gender.id) &&
                Objects.equals(name, gender.name) &&
                Objects.equals(abbreviation, gender.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abbreviation);
    }

    @Override
    public String toString() {
        return "Gender{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}