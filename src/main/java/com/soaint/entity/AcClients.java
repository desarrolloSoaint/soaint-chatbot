package com.soaint.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="ac_clients")
@ApiModel("Modelo => Cliente Publico")
public class AcClients {

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

    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date created_at;

    @ApiModelProperty(value = "Campo de fecha actualización")
    @Column(name="updated_at")
    private Date updated_at;

//    public AcClients(@NotNull Long id, @NotNull Long id_country, @NotNull String email) {
//        this.id = id;
//        this.id_country = id_country;
//        this.email = email;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getId_country() {
        return id_country;
    }

    public void setId_country(Country id_country) {
        this.id_country = id_country;
    }

    public Date getCreated_at() { return created_at; }

    public void setCreated_at(Date created_at) { this.created_at = created_at; }

    public Date getUpdated_at() { return updated_at; }

    public void setUpdated_at(Date updated_at) { this.updated_at = updated_at; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcClients ac_clients = (AcClients) o;
        return Objects.equals(id, ac_clients.id) &&
                Objects.equals(id_country, ac_clients.id_country) &&
                Objects.equals(email, ac_clients.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, id_country, email);
    }

    @Override
    public String toString() {
        return "Client{" +
                ", id_country='" + id_country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}