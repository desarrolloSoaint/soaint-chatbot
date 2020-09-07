package com.soaint.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="ac_clients_private")
@ApiModel("Modelo => Cliente Privado")
public class AcClientsPrivate {

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotNull
    @ApiModelProperty(value = "Campo id_country")
    @ManyToOne
    @JoinColumn(name="id_country")
    Country id_country;

    @NotNull
    @Email
    @ApiModelProperty(value = "Campo correo")
    @Column(name="email")
    private String email;

    @ApiModelProperty(value = "Campo password")
    @Column(name="password")
    private String password;

    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date created_at;

    public AcClientsPrivate() {
    }

    public AcClientsPrivate(@NotNull String email, String password, Country id_country, Date created_at) {
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.id_country = id_country;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Country getId_country() {
        return id_country;
    }

    public void setId_country(Country id_country) {
        this.id_country = id_country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcClientsPrivate ac_clients_private = (AcClientsPrivate) o;
        return Objects.equals(id, ac_clients_private.id) &&
                Objects.equals(id_country, ac_clients_private.id_country) &&
                Objects.equals(email, ac_clients_private.email) &&
                Objects.equals(password, ac_clients_private.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id_country, email, password);
    }

    @Override
    public String toString() {
        return "ClientPrivate{" +
                ", id_country='" + id_country + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
