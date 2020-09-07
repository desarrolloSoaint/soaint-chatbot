package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel("Class Data Transfer Object (DTO)  => Login Usuario")
public class LoginUser {

    @ApiModelProperty(value = "Campo email", required = true)
    @Email
    private String email;
    @ApiModelProperty(value = "Campo contraseña", required = true)
    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
