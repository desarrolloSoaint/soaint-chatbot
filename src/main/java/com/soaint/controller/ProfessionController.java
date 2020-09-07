package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.DTO.ProfessionDto;
import com.soaint.entity.Profession;
import com.soaint.exception.ResourceNotFoundException;
import com.soaint.repository.ProfessionRepository;
import com.soaint.service.ProfessionService;
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
@RequestMapping("/api/v1/profession")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class ProfessionController {

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private ProfessionService professionService;

    //get Profession forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Profesiones Ascendente por Nombre", notes = "Lista de profesiones")
    public List<Profession> findAll(){

        return professionRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    //Get para Search(Busqueda) name estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Profession Ascendente y Descendente por Id y Nombre", notes = "Lista de Profesiones")
    public List<Profession> findAll(
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
        List<Profession> list = professionRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Profession dao = mapper.readValue(search, Profession.class);

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

    //get cantidad profession
    @GetMapping("/count-profession")
    @ApiOperation(value = "Devuelve Cantidad de Registros de Profesión", notes = "Cuenta la cantidad de registro de profesión")
    public long countAll(){
        return professionService.contadorProfession();
    }

    //save Profession
    @PostMapping("/create-profession")
    @ApiOperation(value = "Crear nueva Profesión", notes = "Crear profesión")
    public ResponseEntity<?> create(@Valid @RequestBody ProfessionDto professionDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        Profession profession = new Profession(professionDto.getName());
        if(professionService.existsByName(professionDto.getName()))
            return new ResponseEntity(new Mensaje("La profesión ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        professionService.guardarProfession(profession);
        return new ResponseEntity(new Mensaje("Profesión registrada exitosamente."), HttpStatus.CREATED);
    }

    //get Profession by id
    @GetMapping("/find-profession/{id}")
    @ApiOperation(value = "Buscar Profesion por Id", notes = "Buscar Profesion")
    public ResponseEntity<Profession> getOne(@PathVariable Long id){
        if(!professionService.professionId(id))
            return new ResponseEntity(new Mensaje("No existe esa Profesion"), HttpStatus.NOT_FOUND);
        Profession profession = professionService.professionPorId(id).get();
        return new ResponseEntity<Profession>(profession, HttpStatus.OK);
    }

    // update Profession
    @PutMapping("/update-profession/{id}")
    @ApiOperation(value = "Actualizar Profesión por Id", notes = "Actualizar profesión")
    public ResponseEntity<?> update(@RequestBody ProfessionDto professionDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!professionService.professionId(id))
            return new ResponseEntity(new Mensaje("No existe esa Profesion"), HttpStatus.NOT_FOUND);
        if(professionService.existsByName(professionDto.getName()) &&
                professionService.getByName(professionDto.getName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("La profesión ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Profession professionUpdate = professionService.professionPorId(id).get();
        professionUpdate.setName(professionDto.getName());
        professionService.guardarProfession(professionUpdate);
        return new ResponseEntity(new Mensaje("Profesión actualizada"), HttpStatus.CREATED);
    }


    //delete Profession
    @DeleteMapping("/delete-profession/{id}")
    @ApiOperation(value = "Eliminar Profesión por Id", notes = "Eliminar profesión")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!professionService.professionId(id))
            return new ResponseEntity(new Mensaje("No existe esa profesión"), HttpStatus.NOT_FOUND);
        professionService.borrarProfesion(id);
        return new ResponseEntity(new Mensaje("Profesión eliminada exitosamente"), HttpStatus.OK);
    }


}
