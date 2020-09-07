package com.soaint.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="ac_quiz")
@ApiModel("Modelo => Quiz de Satisfaccion")
public class AcQuiz {

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(value = "Campo de relacion con tabla AcClients")
    @ManyToOne
    @JoinColumn(name = "id_client")
    AcClients id_client;

    @ApiModelProperty(value = "Campo de relacion con tabla AcClientsPrivate")
    @ManyToOne
    @JoinColumn(name = "id_client_private")
    AcClientsPrivate id_client_private;

    @NotNull
    @ApiModelProperty(value = "Campo quiz one", required = true)
    @Column(name="quiz_one")
    private String quiz_one;

    @NotNull
    @ApiModelProperty(value = "Campo quiz two", required = true)
    @Column(name="quiz_two")
    private String quiz_two;

    @NotNull
    @ApiModelProperty(value = "Campo quiz three", required = true)
    @Column(name="quiz_three")
    private String quiz_three;

    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AcClients getId_client() {
        return id_client;
    }

    public void setId_client(AcClients id_client) {
        this.id_client = id_client;
    }

    public AcClientsPrivate getId_client_private() {
        return id_client_private;
    }

    public void setId_client_private(AcClientsPrivate id_client_private) {
        this.id_client_private = id_client_private;
    }

    public String getQuiz_one() {
        return quiz_one;
    }

    public void setQuiz_one(String quiz_one) {
        this.quiz_one = quiz_one;
    }

    public String getQuiz_two() {
        return quiz_two;
    }

    public void setQuiz_two(String quiz_two) {
        this.quiz_two = quiz_two;
    }

    public String getQuiz_three() {
        return quiz_three;
    }

    public void setQuiz_three(String quiz_three) {
        this.quiz_three = quiz_three;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }





}
