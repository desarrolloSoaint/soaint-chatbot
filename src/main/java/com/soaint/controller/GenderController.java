package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.GenderDto;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.Currency;
import com.soaint.entity.Gender;
import com.soaint.exception.ResourceNotFoundException;
import com.soaint.repository.GenderRepository;
import com.soaint.service.GenderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gender")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class GenderController {

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private GenderService genderService;

    //    @PreAuthorize("hasRole('ADMIN')")
    //get Gender forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Generos Ascendente por Nombre", notes = "Lista de generos")
    public List<Gender> findAll(){

        return genderRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    //Get para Search(Busqueda) name estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Generos Ascendente y Descendente por Id y Nombre", notes = "Lista de Generos")
    public List<Gender> findAll(
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
        List<Gender> list = genderRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Gender dao = mapper.readValue(search, Gender.class);

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
    //get cantidad gender
    @GetMapping("/count-gender")
    @ApiOperation(value = "Devuelve Cantidad de Registros de Genero", notes = "Cuenta la cantidad de registro de genero")
    public long countAll(){
        return genderService.contadorGender();
    }

    //save Gender
    @PostMapping("/create-gender")
    @ApiOperation(value = "Crear nuevo Genero", notes = "Crear genero")
    public ResponseEntity<?> create(@Valid @RequestBody GenderDto genderDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(genderService.existsByName(genderDto.getName()))
            return new ResponseEntity(new Mensaje("El género ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Gender gender = new Gender(genderDto.getName(), genderDto.getAbbreviation());
        genderService.guardarGender(gender);
        return new ResponseEntity(new Mensaje("Genero registrado exitosamente."), HttpStatus.CREATED);
    }

    //get Gender by id
    @GetMapping("/find-gender/{id}")
    @ApiOperation(value = "Buscar Genero por Id", notes = "Buscar Genero")
    public ResponseEntity<Gender> getOne(@PathVariable Long id){
        if(!genderService.genderId(id))
            return new ResponseEntity(new Mensaje("No existe ese Genero"), HttpStatus.NOT_FOUND);
        Gender gender = genderService.genderPorId(id).get();
        return new ResponseEntity<Gender>(gender, HttpStatus.OK);
    }

    // update Gender
    @PutMapping("/update-gender/{id}")
    @ApiOperation(value = "Actualizar Genero por Id", notes = "Actualizar Gender")
    public ResponseEntity<?> update(@RequestBody GenderDto genderDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!genderService.genderId(id))
            return new ResponseEntity(new Mensaje("No existe ese Gender"), HttpStatus.NOT_FOUND);
        if(genderService.existsByName(genderDto.getName()) &&
                genderService.getByName(genderDto.getName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El género ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Gender genderUpdate = genderService.genderPorId(id).get();
        genderUpdate.setName(genderDto.getName());
        genderUpdate.setAbbreviation(genderDto.getAbbreviation());
        genderService.guardarGender(genderUpdate);
        return new ResponseEntity(new Mensaje("Género actualizado"), HttpStatus.CREATED);
    }


    //delete Gender
    @DeleteMapping("/delete-gender/{id}")
    @ApiOperation(value = "Eliminar Genero por Id", notes = "Eliminar genero")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!genderService.genderId(id))
            return new ResponseEntity(new Mensaje("No existe ese genero"), HttpStatus.NOT_FOUND);
        genderService.borrarGender(id);
        return new ResponseEntity(new Mensaje("Genero eliminado exitosamente"), HttpStatus.OK);
    }



}