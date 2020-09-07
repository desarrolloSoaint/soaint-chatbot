package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="ac_menu")
@ApiModel("Modelo => MENU")
public class AcMenu {

    @Id
    @NotNull
    @ApiModelProperty(value = "Campo Id Autoincrementable del Menu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo Id Descripcion", required = true)
    @Column(name="description")
    private String description;

    @NotNull
    @ApiModelProperty(value = "Campo url", required = true)
    @Column(name="url")
    private String url;



    @ApiModelProperty(value = "Campo de relacion con tabla Rol ", required = true)
    @ManyToOne
    @JoinColumn(name="id_rol")
    Rol id_rol;

    @ApiModelProperty(value = "Campo Icono")
    @Column(name="icon")
    private String icon;

    @ApiModelProperty(value = "Campo Nivel")
    @Column(name="level")
    private Long level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Rol getId_rol() {
        return id_rol;
    }

    public void setId_rol(Rol id_rol) {
        this.id_rol = id_rol;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcMenu acMenu = (AcMenu) o;
        return id.equals(acMenu.id) &&
                description.equals(acMenu.description) &&
                url.equals(acMenu.url) &&
                id_rol.equals(acMenu.id_rol) &&
                icon.equals(acMenu.icon) &&
                level.equals(acMenu.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, url, id_rol, icon, level);
    }

    @Override
    public String toString() {
        return "AcMenu{" +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", id_rol='" + id_rol + '\'' +
                ", icon='" + icon + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

}
