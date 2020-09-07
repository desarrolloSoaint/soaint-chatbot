package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@ApiModel("Modelo => Rol")
public class Rol {

    @SequenceGenerator(
            name = "RolSeq",
            sequenceName = "L_ROL_SEQ",
            initialValue = 3,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "RolSeq")
    private int id;

    @NotNull
    //Se coloco campo unico para que el campo nombre del rol sea unico
    @Column(unique = true)
    @ApiModelProperty(value = "Campo rol", required = true)
    private String rolName;

    public Rol() {
    }

    public Rol(@NotNull String rolName) {
        this.rolName = rolName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

}
