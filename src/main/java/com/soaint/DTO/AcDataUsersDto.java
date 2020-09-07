package com.soaint.DTO;


import com.soaint.entity.Country;
import com.soaint.entity.Gender;
import com.soaint.entity.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel("Class Data Transfer Object (DTO) => Data User")
public class AcDataUsersDto {

    @ApiModelProperty(value = "Campo Documento de Identificacion", required = true)
    private Long identification_card;
    @ApiModelProperty(value = "Campo Nombres", required = true)
    private String names;
    @ApiModelProperty(value = "Campo Apellidos", required = true)
    private String last_names;
    @ApiModelProperty(value = "Campo Fecha de Nacimiento", required = true)
    private Date birth_date;
    @ApiModelProperty(value = "Campo Telefono Celular")
    private String mobile_phone;
    @ApiModelProperty(value = "Campo Telefono Local")
    private String local_telephone;
    @ApiModelProperty(value = "Campo Imagen Usuario")
    private String image_user;
    @ApiModelProperty(value = "Campo de relacion con tabla Genero ", required = true)
    Gender id_gender;
    @ApiModelProperty(value = "Campo de relacion con tabla Usuario ", required = true)
    Users id_user;
    @ApiModelProperty(value = "Campo de relacion con tabla Pais ", required = true)
    Country id_country;

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

    public Gender getId_gender() {
        return id_gender;
    }

    public void setId_gender(Gender id_gender) {
        this.id_gender = id_gender;
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

}
