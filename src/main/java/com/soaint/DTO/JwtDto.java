package com.soaint.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@ApiModel("Class Data Transfer Object (DTO) => Generar JSON Web Tokens")
public class JwtDto {

    @ApiModelProperty(value = "Campo id")
    private int id;
    @ApiModelProperty(value = "Campo token", required = true)
    private String token;
    @ApiModelProperty(value = "Campo bearer", required = true)
    private String bearer = "Bearer";
    @ApiModelProperty(value = "Campo email", required = true)
    private String email;
    @ApiModelProperty(value = "Campo para privilegios", required = true)
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(Integer id, String token, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.token = token;
        this.authorities = authorities;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
