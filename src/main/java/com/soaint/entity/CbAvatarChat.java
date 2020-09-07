package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="cb_avatar_chat")
@ApiModel(value = "Modelo => AVATAR_CHAT")
public class CbAvatarChat {

    @SequenceGenerator(
            name = "ChatSeq",
            sequenceName = "CB_AVATAR_CHAT_SEQ",
            initialValue = 2,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ChatSeq")
    private Long id;

    @ApiModelProperty(value = "Campo avatar chat", required = true)
    @Column(name = "avatar_chat")
    private String avatar_chat;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarChat() {
        return avatar_chat;
    }

    public void setAvatarChat(String avatar_chat) {
        this.avatar_chat = avatar_chat;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbAvatarChat cbAvatarChat = (CbAvatarChat) o;
        return Objects.equals(id, cbAvatarChat.id) &&
                Objects.equals(avatar_chat, cbAvatarChat.avatar_chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatar_chat);
    }

    @Override
    public String toString() {
        return "Avatar{" +
                ", avatar_chat='" + avatar_chat + '\'' +
                '}';
    }

}
