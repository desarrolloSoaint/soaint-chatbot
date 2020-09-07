package com.soaint.DTO;

import com.soaint.entity.Rol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ApiModel("Class Data Transfer Object (DTO) => Nuevo Usuario")
public class NewUser {

    @ApiModelProperty(value = "Campo email", required = true)
    @Email
    private String email;
    @ApiModelProperty(value = "Campo contraseña", required = true)
    @NotBlank
    private String password;
    @ApiModelProperty(value = "Campo para asignar roles")
    private Set<Rol> roles = new HashSet<>();

    @ApiModelProperty(value = "Campo de fecha creación de Users")
    @Column(name="created_at")
    private Date created_at;

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

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
