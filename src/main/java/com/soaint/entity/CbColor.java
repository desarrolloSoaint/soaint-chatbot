package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="cb_color")
@ApiModel("Modelo => Colors")
public class CbColor {

    @SequenceGenerator(
            name = "ColorSeq",
            sequenceName = "CB_COLOR_SEQ",
            initialValue = 2,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo Id Autoincrementable de Usuarios")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ColorSeq")
    private Long id;

    @ApiModelProperty(value = "Campo Color")
    @Column(name="color")
    private String color;

    public CbColor(){

    }

    public CbColor(@NotNull String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbColor that = (CbColor) o;
        return id.equals(that.id) &&
                color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color);
    }

    @Override
    public String toString() {
        return "CbColor{" +
                ", color='" + color + '\'' +
                '}';
    }
}
