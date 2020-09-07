package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.CountryDto;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.Country;
import com.soaint.exception.ResourceNotFoundException;
import com.soaint.repository.CountryRepository;
import com.soaint.service.CountryService;
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
@RequestMapping("/api/v1/country")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    //get Country forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Países Ascendente por Nombre", notes = "Lista de países")
    public List<Country> findAll(){

        return countryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    //Get para Search(Busqueda) name estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Países Ascendente y Descendente por Id y Nombre", notes = "Lista de países")
    public List<Country> findAll(
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
        List<Country> list = countryRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Country dao = mapper.readValue(search, Country.class);

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

    //save Country
    @PostMapping("/create-country")
    @ApiOperation(value = "Crear un nuevo País", notes = "Crear país")
    public ResponseEntity<?> create(@Valid @RequestBody CountryDto countryDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(countryService.existsByName(countryDto.getName()))
            return new ResponseEntity(new Mensaje("El país ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Country country = new Country(countryDto.getName());
        countryService.guardarCountry(country);
        return new ResponseEntity(new Mensaje("País registrado exitosamente."), HttpStatus.CREATED);
    }

    //get country by id
    @GetMapping("/find-country/{id}")
    @ApiOperation(value = "Buscar País por Id", notes = "Buscar país")
    public ResponseEntity<Country> getOne(@PathVariable Long id){
        if(!countryService.countryId(id))
            return new ResponseEntity(new Mensaje("No existe ese País"), HttpStatus.NOT_FOUND);
        Country country = countryService.countryPorId(id).get();
        return new ResponseEntity<Country>(country, HttpStatus.OK);
    }

    //get cantidad country
    @GetMapping("/count-country")
    @ApiOperation(value = "Devuelve Cantidad de Registro de los Paises", notes = "Cuenta la cantidad de registro de List Country")
    public long countAll(){
        return countryService.contadorCountry();
    }

    // update Country
    @PutMapping("/update-country/{id}")
    @ApiOperation(value = "Actualizar País por Id", notes = "Actualizar país")
    public ResponseEntity<?> update(@Valid @RequestBody CountryDto countryDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!countryService.dataCountry(id))
            return new ResponseEntity(new Mensaje("No existe ese Country"), HttpStatus.NOT_FOUND);
        if(countryService.existsByName(countryDto.getName()) &&
                countryService.getByName(countryDto.getName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El país ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Country countryUpdate = countryService.countryPorId(id).get();
        countryUpdate.setName(countryDto.getName());
        countryService.guardarCountry(countryUpdate);
        return new ResponseEntity(new Mensaje("País actualizado"), HttpStatus.CREATED);
    }

    //delete country
    @DeleteMapping("/delete-country/{id}")
    @ApiOperation(value = "Eliminar País por Id", notes = "Eliminar país")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!countryService.countryId(id))
            return new ResponseEntity(new Mensaje("No existe ese Country"), HttpStatus.NOT_FOUND);
        countryService.borrarCountry(id);
        return new ResponseEntity(new Mensaje("País con sus regiones eliminados exitosamente "), HttpStatus.OK);
    }


}