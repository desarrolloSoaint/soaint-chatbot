package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="ac_data_clients_private")
@ApiModel("Modelo => Datos del Cliente Privado")
public class AcDataClientsPrivate {

    @Id
    @NotNull
    @ApiModelProperty(value = "Campo Id Autoincrementable del Cliente Privado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ApiModelProperty(value = "Campo Documento de Identificacion", required = true)
    @Column(name="identification_card")
    private Long identification_card;

    @NotNull
    @ApiModelProperty(value = "Campo Nombres", required = true)
    @Column(name="names")
    private String names;

    @NotNull
    @ApiModelProperty(value = "Campo Apellidos", required = true)
    @Column(name="last_names")
    private String last_names;

    @NotNull
    @ApiModelProperty(value = "Campo Fecha de Nacimiento", required = true)
    @Column(name="birth_date")
    private String birth_date;

    @ApiModelProperty(value = "Campo Telefono Celular")
    @Column(name="mobile_phone")
    private String mobile_phone;


    @NotNull
    @ApiModelProperty(value = "Campo Id Cliente Privado", required = true)
    @ManyToOne
    @JoinColumn(name="id_client_private")
    AcClientsPrivate id_client_private;



    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getIdentification_card() {
        return identification_card;
    }

    public void setIdentification_card(Long identification_card) {
        this.identification_card = identification_card;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLast_names() {
        return last_names;
    }

    public void setLast_names(String last_names) {
        this.last_names = last_names;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public AcClientsPrivate getId_client_private() {
        return id_client_private;
    }

    public void setId_client_private(AcClientsPrivate id_client_private) {
        this.id_client_private = id_client_private;
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
        AcDataClientsPrivate ac_data_clients_private = (AcDataClientsPrivate) o;
        return Objects.equals(id, ac_data_clients_private.id) &&
                Objects.equals(identification_card, ac_data_clients_private.identification_card) &&
                Objects.equals(names, ac_data_clients_private.names) &&
                Objects.equals(last_names, ac_data_clients_private.last_names) &&
                Objects.equals(birth_date, ac_data_clients_private.birth_date) &&
                Objects.equals(mobile_phone, ac_data_clients_private.mobile_phone) &&
                Objects.equals(id_client_private, ac_data_clients_private.id_client_private);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identification_card, names, last_names,
                birth_date, mobile_phone, id_client_private );
    }

    @Override
    public String toString() {
        return "DataClientPrivate{" +
                ", id_client_private='" + id_client_private + '\'' +
                ", names='" + names + '\'' +
                ", last_names='" + last_names + '\'' +
                ", identification_card='" + identification_card + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", mobile_phone='" + mobile_phone + '\'' +
                '}';
    }
}
