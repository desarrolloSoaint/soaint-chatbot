package com.soaint.entity;

import com.soaint.DTO.CountryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="l_state")
@ApiModel(value = "Modelo => STATE")
public class State {

    @SequenceGenerator(
            name = "StateSeq",
            sequenceName = "L_STATE_SEQ",
            initialValue = 2206,
            allocationSize = 10
    )


    @Id
    @ApiModelProperty(value = "Campo Id Autoincrementable del state")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StateSeq")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Campo name", required = true)
    @Column(name="name")
    private String name;

    @NotNull
    @ApiModelProperty(value = "Campo de relacion con tabla Pais ", required = true)
    @ManyToOne
    @JoinColumn(name="country_id")
    Country country_id;

    public State() {
    }

    public State(@NotNull String name, @NotNull Country country_id) {
        this.name = name;
        this.country_id = country_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Country country_id) {
        this.country_id = country_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id.equals(state.id) &&
                name.equals(state.name) &&
                country_id.equals(state.country_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country_id);
    }

    @Override
    public String toString() {
        return "State{" +

                ", name='" + name + '\'' +
                ", country_id='" + country_id + '\'' +
                '}';
    }

}
