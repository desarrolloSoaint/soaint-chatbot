package com.soaint.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.DTO.StaCivilDto;
import com.soaint.entity.StaCivil;
import com.soaint.exception.ResourceNotFoundException;
import com.soaint.repository.StaCivilRepository;
import com.soaint.service.StaCivilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statecivil")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class StaCivilController {

    @Autowired
    private StaCivilRepository staCivilRepository;

    @Autowired
    private StaCivilService staCivilService;

    //get StaCivil forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Estado Civil Ascendente por Nombre", notes = "Lista de estado civil")
    public List<StaCivil> findAll(){

        return staCivilRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    //Get para Search(Busqueda) name estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Estado Civil Ascendente y Descendente por Id y Nombre", notes = "Lista de Estado Civil")
    public List<StaCivil> findAll(
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
        List<StaCivil> list = staCivilRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    StaCivil dao = mapper.readValue(search, StaCivil.class);

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

    //get cantidad StaCivil
    @GetMapping("/count-staCivil")
    @ApiOperation(value = "Devuelve Cantidad de Registro del Estado Civil", notes = "Cuenta la cantidad de registro de estado civil")
    public long countAll(){
        return staCivilService.contadorStaCivil();
    }

    //save StaCivil
    @PostMapping("/create-staCivil")
    @ApiOperation(value = "Crear nuevo Estado Civil", notes = "Crear Estado Civil")
    public ResponseEntity<?> create(@Valid @RequestBody StaCivilDto staCivilDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(staCivilService.existsByName(staCivilDto.getName()))
            return new ResponseEntity(new Mensaje("El estado civil ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        StaCivil staCivil = new StaCivil(staCivilDto.getName(), staCivilDto.getAbbreviation());
        staCivilService.guardarStaCivil(staCivil);
        return new ResponseEntity(new Mensaje("Estado civil registrado exitosamente."), HttpStatus.CREATED);
    }

    //get StaCivil by id
    @GetMapping("/find-staCivil/{id}")
    @ApiOperation(value = "Buscar Estado Civil por Id", notes = "Buscar estado civil")
    public ResponseEntity<StaCivil> getOne(@PathVariable Long id){
        if(!staCivilService.staCivilId(id))
            return new ResponseEntity(new Mensaje("No existe ese estado civil"), HttpStatus.NOT_FOUND);
        StaCivil staCivil = staCivilService.staCivilPorId(id).get();
        return new ResponseEntity<StaCivil>(staCivil, HttpStatus.OK);
    }

    // update StaCivil
    @PutMapping("/update-staCivil/{id}")
    @ApiOperation(value = "Actualizar Estado Civil por Id", notes = "Actualizar estado civil")
    public ResponseEntity<?> update(@RequestBody StaCivilDto staCivilDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!staCivilService.staCivilId(id))
            return new ResponseEntity(new Mensaje("No existe ese estado civil"), HttpStatus.NOT_FOUND);
        if(staCivilService.existsByName(staCivilDto.getName()) &&
                staCivilService.getByName(staCivilDto.getName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El estado civil ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        StaCivil staCivilUpdate = staCivilService.staCivilPorId(id).get();
        staCivilUpdate.setName(staCivilDto.getName());
        staCivilUpdate.setAbbreviation(staCivilDto.getAbbreviation());
        staCivilService.guardarStaCivil(staCivilUpdate);
        return new ResponseEntity(new Mensaje("Estado civil actualizado exitosamente"), HttpStatus.CREATED);
    }


    //delete StaCivil
    @DeleteMapping("/delete-staCivil/{id}")
    @ApiOperation(value = "Eliminar Estado Civil por Id", notes = "Eliminar Estado civil")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!staCivilService.staCivilId(id))
            return new ResponseEntity(new Mensaje("No existe ese Estado civil"), HttpStatus.NOT_FOUND);
        staCivilService.borrarStaCivil(id);
        return new ResponseEntity(new Mensaje("Estado civil eliminado exitosamente"), HttpStatus.OK);
    }


}
