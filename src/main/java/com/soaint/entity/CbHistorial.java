package com.soaint.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="cb_historial")
@ApiModel("Modelo => Historial de Chat")
public class CbHistorial {

    @Id
    @ApiModelProperty(value = "Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ApiModelProperty(value = "Campo de relacion con tabla AcClients ", required = true)
    @ManyToOne
    @JoinColumn(name = "id_client")
    AcClients id_client;


    @ApiModelProperty(value = "Campo de relacion con tabla AcClientsPrivate ", required = true)
    @ManyToOne
    @JoinColumn(name = "id_client_private")
    AcClientsPrivate id_client_private;

    @ApiModelProperty(value = "Campo question_client", required = true)
    @Column(name="question_client",length = 500)
    private String question_client;

    @ApiModelProperty(value = "Campo response_soniat", required = true)
    @Column(name="response_soniat",length = 500)
    private String response_soniat;


    @ApiModelProperty(value = "Campo time_question_client", required = true)
    @Column(name="time_question_client")
    private Date time_question_client;


    @ApiModelProperty(value = "Campo time_response_soniat", required = true)
    @Column(name="time_response_soniat")
    private Date time_response_soniat;


    @ApiModelProperty(value = "Campo expired_time", required = true)
    @Column(name="expired_time")
    private Date expired_time;

//    public CbHistorial( Integer id,AcClients id_client,AcClientsPrivate id_client_private, String question_client, String response_soniat, Date time_question_client, Date time_response_soniat) {
//        this.id = id;
//        this.id_client = id_client;
//        this.id_client_private  = id_client_private;
//        this.question_client = question_client;
//        this.response_soniat = response_soniat;
//        this.time_question_client = time_question_client;
//        this.time_response_soniat = time_response_soniat;
//    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

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

    public String getQuestion_client() {
        return question_client;
    }

    public void setQuestion_client(String question_client) {
        this.question_client = question_client;
    }

    public String getResponse_soniat() {
        return response_soniat;
    }

    public void setResponse_soniat(String response_soniat) {
        this.response_soniat = response_soniat;
    }

    public Date getTime_question_client() {
        return time_question_client;
    }

    public void setTime_question_client(Date time_question_client) {
        this.time_question_client = time_question_client;
    }

    public Date getTime_response_soniat() {
        return time_response_soniat;
    }

    public void setTime_response_soniat(Date time_response_soniat) {
        this.time_response_soniat = time_response_soniat;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbHistorial cbHistorial = (CbHistorial) o;
        return Objects.equals(id, cbHistorial.id) &&
                Objects.equals(id_client, cbHistorial.id_client) &&
                Objects.equals(id_client_private, cbHistorial.id_client_private) &&
                Objects.equals(question_client, cbHistorial.question_client) &&
                Objects.equals(response_soniat, cbHistorial.response_soniat) &&
                Objects.equals(time_question_client, cbHistorial.time_question_client) &&
                Objects.equals(time_response_soniat, cbHistorial.time_response_soniat) &&
                Objects.equals(expired_time, cbHistorial.expired_time);
    }

    @Override
    public int hashCode() { return Objects.hash(id, id_client, id_client_private,  question_client, response_soniat); }

    @Override
    public String toString() {
        return "Historial{" +
                ", id_client='" + id_client + '\'' +
                ", id_client_private='" + id_client_private + '\'' +
                ", question_client='" + question_client + '\'' +
                ", response_soniat='" + response_soniat + '\'' +
                ", time_question_client='" + time_question_client + '\'' +
                ", time_response_soniat='" + time_response_soniat + '\'' +
                ", expired_time='" + expired_time + '\'' +
                '}';
    }
}