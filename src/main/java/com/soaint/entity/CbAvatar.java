package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="cb_avatar")
@ApiModel(value = "Modelo => AVATAR")
public class CbAvatar {

    @SequenceGenerator(
            name = "AvatarSeq",
            sequenceName = "CB_AVATAR_SEQ",
            initialValue = 2,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AvatarSeq")
    private Long id;

    @ApiModelProperty(value = "Campo avatar", required = true)
    @Column(name = "avatar")
    private String avatar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbAvatar cbAvatar = (CbAvatar) o;
        return Objects.equals(id, cbAvatar.id) &&
                Objects.equals(avatar, cbAvatar.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatar);
    }

    @Override
    public String toString() {
        return "Avatar{" +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}