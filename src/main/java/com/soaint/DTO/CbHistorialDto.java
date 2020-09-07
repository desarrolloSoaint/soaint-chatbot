package com.soaint.DTO;

import com.soaint.entity.AcClients;
import com.soaint.entity.AcClientsPrivate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@ApiModel("Class Data Transfer Object (DTO) => CbHistorialDto")
public class CbHistorialDto {
    @NotBlank
    @ApiModelProperty(value = "Campo año")
    private Integer year_time_question_client;

    @NotBlank
    @ApiModelProperty(value = "Campo mes")
    private Integer month_time_question_client;

    @NotBlank
    @ApiModelProperty(value = "Campo dia")
    private Integer day_time_question_client;

    @NotBlank
    @ApiModelProperty(value = "Campo año")
    private Integer year_time_response_soniat;

    @NotBlank
    @ApiModelProperty(value = "Campo mes")
    private Integer month_time_response_soniat;

    @NotBlank
    @ApiModelProperty(value = "Campo dia")
    private Integer day_time_response_soniat;

    @ApiModelProperty(value = "Campo de relacion con tabla AcClients ", required = true)
    @ManyToOne
    @JoinColumn(name = "id_client")
    private AcClients id_client;


    @ApiModelProperty(value = "Campo de relacion con tabla AcClientsPrivate ", required = true)
    @ManyToOne
    @JoinColumn(name = "id_client_private")
    private AcClientsPrivate id_client_private;

    public Integer getYear_time_question_client() {
        return year_time_question_client;
    }

    public void setYear_time_question_client(Integer year_time_question_client) {
        this.year_time_question_client = year_time_question_client;
    }

    public Integer getMonth_time_question_client() {
        return month_time_question_client;
    }

    public void setMonth_time_question_client(Integer month_time_question_client) {
        this.month_time_question_client = month_time_question_client;
    }

    public Integer getDay_time_question_client() {
        return day_time_question_client;
    }

    public void setDay_time_question_client(Integer day_time_question_client) {
        this.day_time_question_client = day_time_question_client;
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

    public Integer getYear_time_response_soniat() {
        return year_time_response_soniat;
    }

    public void setYear_time_response_soniat(Integer year_time_response_soniat) {
        this.year_time_response_soniat = year_time_response_soniat;
    }

    public Integer getMonth_time_response_soniat() {
        return month_time_response_soniat;
    }

    public void setMonth_time_response_soniat(Integer month_time_response_soniat) {
        this.month_time_response_soniat = month_time_response_soniat;
    }

    public Integer getDay_time_response_soniat() {
        return day_time_response_soniat;
    }

    public void setDay_time_response_soniat(Integer day_time_response_soniat) {
        this.day_time_response_soniat = day_time_response_soniat;
    }
}
