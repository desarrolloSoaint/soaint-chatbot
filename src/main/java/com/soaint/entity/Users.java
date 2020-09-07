package com.soaint.entity;

import com.soaint.entity.Rol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@ApiModel("Modelo => Usuario")
@Table(name="users")
public class Users {

    @SequenceGenerator(
            name = "UsersSeq",
            sequenceName = "USERS_SEQ",
            initialValue = 2,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable", required = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UsersSeq")
    private int id;
    @NotNull
    @ApiModelProperty(value = "Campo email", required = true)
    @Column(unique = true)
    private String email;
    @ApiModelProperty(value = "Campo contraseña", required = true)
    @NotNull
    private String password;
    @ApiModelProperty(value = "Campo rol", required = true)
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_rol", joinColumns = @JoinColumn( name= "users_id"),
    inverseJoinColumns = @JoinColumn(name="rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @ApiModelProperty(value = "Campo de fecha creación de Users")
    @Column(name="created_at")
    private Date created_at;

    public Users() {
    }

    public Users(String email, String password, Set<Rol> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
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
