package com.soaint.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ac_aiml")
@ApiModel(value = "Modelo => AIML")
public class AcAiml {

    @SequenceGenerator(
            name = "AcAimlSeq",
            sequenceName = "AC_AIML_SEQ",
            initialValue = 18,
            allocationSize = 10
    )

    @Id
    @ApiModelProperty(value="Campo id autoincrementable")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AcAimlSeq")
    private Long id;

    @ApiModelProperty(value="Campo id autoincrementable")
    @Column(name="aiml")
    private String aiml;

    @ApiModelProperty(value = "Campo de fecha creación")
    @Column(name="created_at")
    private Date createdAt;

    @ApiModelProperty(value = "Campo de fecha actualización")
    @Column(name="updated_at")
    private Date updatedAt;

    public AcAiml() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAiml() {
        return aiml;
    }

    public void setAiml(String aiml) {
        this.aiml = aiml;
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
