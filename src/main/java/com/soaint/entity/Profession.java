package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="l_profession")
@ApiModel("Modelo => Profesión")
public class Profession {

    @SequenceGenerator(
            name = "ProfessionSeq",
            sequenceName = "L_PROFESSION_SEQ",
            initialValue = 15,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProfessionSeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo profesión", required = true)
    @Column(name = "name")
    private String name;

    public Profession(){

    }

    public Profession(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession that = (Profession) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Profession{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}