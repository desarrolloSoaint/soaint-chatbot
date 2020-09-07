package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.DTO.StateDto;
import com.soaint.entity.State;
import com.soaint.exception.ResourceNotFoundException;
import com.soaint.repository.StateRepository;
import com.soaint.service.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/state")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class StateController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateService stateService;

    //get State forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Estados Ascendente por Nombre", notes = "Lista de estados")
    public List<State> findAll(){

        return stateRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    //Get para Search(Busqueda) name estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Estado Civil Ascendente y Descendente por Id y Nombre", notes = "Lista de Estado Civil")
    public List<State> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sensitive", required = false) Boolean sensitive,
            @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
            @RequestParam(value = "columnNames", required = false) List<String> columnNames
    ) {

        Sort order;
        List<Sort.Order> sorts = new ArrayList<>();
        switch (orderBy.toUpperCase()) {

            case "DESC":
                for (String s : columnNames
                ) {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, s));
                }
                order = new Sort(sorts);
                break;
            default:
                for (String s : columnNames
                ) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, s));
                }
                order = new Sort(sorts);
                break;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<State> list = stateRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    State dao = mapper.readValue(search, State.class);

                    if (!StringUtils.isEmpty(dao.getName())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("name")), "%" + dao.getName().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("name")), dao.getName().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("name")), dao.getName().toLowerCase() + "%")));
                        }

                    }

                    if (!StringUtils.isEmpty(dao.getId())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                root.get("id"), dao.getId()
                        )));
                    }


                } catch (IOException io) {
                    io.printStackTrace();
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, order);
        return list;
    }

    //get cantidad state
    @GetMapping("/count-state")
    @ApiOperation(value = "Devuelve Cantidad de Registro del Estados", notes = "Cuenta la cantidad de registro de List State")
    public long countAll(){
        return stateService.contadorState();
    }

    //save State
    @PostMapping("/create-state")
    @ApiOperation(value = "Crear un Estado", notes = "Crear Estado" )
    public ResponseEntity<?> create(@Valid @RequestBody StateDto stateDto)
    {
        if(stateService.existsByName(stateDto.getName()))
            return new ResponseEntity(new Mensaje("El estado ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        State state = new State(stateDto.getName(), stateDto.getCountry_id());
        stateService.guardarState(state);
        return new ResponseEntity(new Mensaje("Estado registrado exitosamente."), HttpStatus.CREATED);
    }

    //get State by id
    @GetMapping("/find-state/{id}")
    @ApiOperation(value = "Buscar Estado por Id", notes = "Buscar State")
    public ResponseEntity<State> getOne(@PathVariable Long id){
        if(!stateService.stateId(id))
            return new ResponseEntity(new Mensaje("No existe ese estado"), HttpStatus.NOT_FOUND);
        State state = stateService.statePorId(id).get();
        return new ResponseEntity<State>(state, HttpStatus.OK);
    }

    // update State
    @PutMapping("/update-state/{id}")
    @ApiOperation(value = "Actualizar Estado por Id", notes = "Actualizar State")
    public ResponseEntity<?> update(@RequestBody StateDto stateDto, @PathVariable("id") Long id){
        if(!stateService.stateId(id))
            return new ResponseEntity(new Mensaje("No existe ese State"), HttpStatus.NOT_FOUND);
        if(stateService.existsByName(stateDto.getName()) &&
                stateService.getByName(stateDto.getName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El estado ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        State stateUpdate = stateService.statePorId(id).get();
        stateUpdate.setName(stateDto.getName());
        stateUpdate.setCountry_id(stateDto.getCountry_id());
        stateService.guardarState(stateUpdate);
        return new ResponseEntity(new Mensaje("Estado actualizado exitosamente"), HttpStatus.CREATED);
    }


    //delete State
    @DeleteMapping("/delete-state/{id}")
    @ApiOperation(value = "Eliminar Estado por Id", notes = "Eliminar State")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!stateService.stateId(id))
            return new ResponseEntity(new Mensaje("No existe ese State"), HttpStatus.NOT_FOUND);
        stateService.borrarState(id);
        return new ResponseEntity(new Mensaje("Estado eliminado exitosamente"), HttpStatus.OK);
    }

}
