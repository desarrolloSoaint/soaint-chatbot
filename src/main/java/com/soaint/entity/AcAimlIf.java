package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ac_aiml_if")
@ApiModel(value = "Modelo => AIMLIF")
public class AcAimlIf {

    @SequenceGenerator(
            name = "AcAimlIfSeq",
            sequenceName = "AC_AIML_IF_SEQ",
            initialValue = 18,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value="Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AcAimlIfSeq")
    private Long id;

    @ApiModelProperty(value="Campo id autoincrementable")
    @Column(name="aiml_if")
    private String aimlIf;

    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date createdAt;

    @ApiModelProperty(value = "Campo de fecha actualización")
    @Column(name="updated_at")
    private Date updatedAt;

    public AcAimlIf() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAimlIf() {
        return aimlIf;
    }

    public void setAimlIf(String aimlIf) {
        this.aimlIf = aimlIf;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
