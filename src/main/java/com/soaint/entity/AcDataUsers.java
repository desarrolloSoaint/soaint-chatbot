package com.soaint.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="ac_data_users")
@ApiModel("Modelo => DATA_USERS")
public class AcDataUsers {

    @SequenceGenerator(
            name = "DataUsersSeq",
            sequenceName = "DATA_USERS_SEQ",
            initialValue = 2,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo Id Autoincrementable de Usuarios")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DataUsersSeq")
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
    private Date birth_date;

    @NotNull
    @ApiModelProperty(value = "Campo de relacion con tabla Genero ", required = true)
    @ManyToOne
    @JoinColumn(name="id_gender")
    Gender id_gender;

    @ApiModelProperty(value = "Campo Telefono Celular")
    @Column(name="mobile_phone")
    private String mobile_phone;

    @ApiModelProperty(value = "Campo Telefono Local")
    @Column(name="local_telephone")
    private String local_telephone;

    @ApiModelProperty(value = "Campo Imagen Usuario")
    @Column(name="image_user")
    private String image_user;

    @NotNull
    @ApiModelProperty(value = "Campo de relacion con tabla Usuario ", required = true)
    @ManyToOne
    @JoinColumn(name="id_user")
    Users id_user;

    @NotNull
    @ApiModelProperty(value = "Campo de relacion con tabla Pais ", required = true)
    @ManyToOne
    @JoinColumn(name="id_country")
    Country id_country;


    public AcDataUsers() {
    }

    public AcDataUsers(@NotNull Long identification_card, @NotNull String names, @NotNull String last_names, @NotNull Date birth_date, @NotNull Gender id_gender, @NotNull String mobile_phone, @NotNull String local_telephone, @NotNull Users id_user, @NotNull Country id_country) {
        this.identification_card = identification_card;
        this.names = names;
        this.last_names = last_names;
        this.birth_date = birth_date;
        this.id_gender = id_gender;
        this.mobile_phone = mobile_phone;
        this.local_telephone = local_telephone;
        this.id_user = id_user;
        this.id_country = id_country;

    }

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

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public Gender getId_gender() {
        return id_gender;
    }

    public void setId_gender(Gender id_gender) {
        this.id_gender = id_gender;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getLocal_telephone() {
        return local_telephone;
    }

    public void setLocal_telephone(String local_telephone) {
        this.local_telephone = local_telephone;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public Users getId_user() {
        return id_user;
    }

    public void setId_user(Users id_user) {
        this.id_user = id_user;
    }

    public Country getId_country() {
        return id_country;
    }

    public void setId_country(Country id_country) {
        this.id_country = id_country;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcDataUsers that = (AcDataUsers) o;
        return Objects.equals(id, that.id) &&
                identification_card.equals(that.identification_card) &&
                names.equals(that.names) &&
                last_names.equals(that.last_names) &&
                birth_date.equals(that.birth_date) &&
                id_gender.equals(that.id_gender) &&
                mobile_phone.equals(that.mobile_phone) &&
                local_telephone.equals(that.local_telephone) &&
                image_user.equals(that.image_user) &&
                id_user.equals(that.id_user) &&
                id_country.equals(that.id_country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identification_card, names, last_names, birth_date, id_gender, mobile_phone, local_telephone, image_user, id_user, id_country);
    }

    @Override
    public String toString() {
        return "AcDataUsers{" +
                ", identification_card='" + identification_card + '\'' +
                ", names='" + names + '\'' +
                ", last_names='" + last_names + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", id_gender='" + id_gender + '\'' +
                ", mobile_phone='" + mobile_phone + '\'' +
                ", local_telephone='" + local_telephone + '\'' +
                ", image_user='" + image_user + '\'' +
                ", id_user='" + id_user + '\'' +
                ", id_country='" + id_country + '\'' +
                '}';
    }

}
