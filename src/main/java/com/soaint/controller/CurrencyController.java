package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.CurrencyDto;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.Currency;
import com.soaint.repository.CurrencyRepository;
import com.soaint.service.CurrencyService;
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
@RequestMapping("/api/v1/currency")
@Api(tags = "LISTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class CurrencyController {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyService currencyService;

    //get Currency forma Ascendente por tabla name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Cantidad de Monedas Ascendente por Nombre en la tabla Money", notes = "Lista de monedas")
    public List<Currency> findAll(){

        return currencyRepository.findAll(Sort.by(Sort.Direction.ASC, "money"));
    }

    //Get para Search(Busqueda) money estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Monedas Ascendente y Descendente por Id y Money", notes = "Lista de Monedas")
    public List<Currency> findAll(
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
        List<Currency> list =  currencyRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Currency dao = mapper.readValue(search, Currency.class);

                    if (!StringUtils.isEmpty(dao.getMoney())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("money")), "%" + dao.getMoney().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("money")), dao.getMoney().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("money")), dao.getMoney().toLowerCase() + "%")));
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

    //get cantidad currency
    @GetMapping("/count-currency")
    @ApiOperation(value = "Cantidad de Registro del moneda", notes = "Cuenta la cantidad de registro de moneda")
    public long countAll(){
        return currencyService.contadorCurrency();
    }

    //save Currency
    @PostMapping("/create-currency")
    @ApiOperation(value = "Crear Moneda", notes = "Crear Moneda")
    public ResponseEntity<?> create(@Valid @RequestBody CurrencyDto currencyDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(currencyService.existsByMoney(currencyDto.getMoney()))
            return new ResponseEntity(new Mensaje("La moneda ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Currency currency = new Currency(currencyDto.getMoney(), currencyDto.getAbbreviation(),
                                            currencyDto.getSymbol());
        currencyService.guardarCurrency(currency);
        return new ResponseEntity(new Mensaje("Moneda registrada exitosamente."), HttpStatus.CREATED);
    }

    //get Currency by id
    @GetMapping("/find-currency/{id}")
    @ApiOperation(value = "Buscar Moneda", notes = "Buscar Moneda")
    public ResponseEntity<Currency> getOne(@PathVariable Long id){
        if(!currencyService.currencyId(id))
            return new ResponseEntity(new Mensaje("No existe esa moneda"), HttpStatus.NOT_FOUND);
        Currency currency = currencyService.currencyPorId(id).get();
        return new ResponseEntity<Currency>(currency, HttpStatus.OK);
    }

    // update Currency
    @PutMapping("/update-currency/{id}")
    @ApiOperation(value = "Actualizar moneda", notes = "Actualizar moneda")
    public ResponseEntity<?> update(@Valid @RequestBody CurrencyDto currencyDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!currencyService.currencyId(id))
            return new ResponseEntity(new Mensaje("No existe esa moneda"), HttpStatus.NOT_FOUND);
        if(currencyService.existsByMoney(currencyDto.getMoney()) &&
                currencyService.getByMoney(currencyDto.getMoney()).get().getId() != id)
            return new ResponseEntity(new Mensaje("La moneda ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        Currency currencyUpdate = currencyService.currencyPorId(id).get();
        currencyUpdate.setMoney(currencyDto.getMoney());
        currencyUpdate.setAbbreviation(currencyDto.getAbbreviation());
        currencyUpdate.setSymbol(currencyDto.getSymbol());
        currencyService.guardarCurrency(currencyUpdate);
        return new ResponseEntity(new Mensaje("Moneda actualizada"), HttpStatus.OK);
    }


    //delete Currency
    @DeleteMapping("/delete-currency/{id}")
    @ApiOperation(value = "Eliminar Currency", notes = "Eliminar Currency")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!currencyService.currencyId(id))
            return new ResponseEntity(new Mensaje("No existe esa Currency"), HttpStatus.NOT_FOUND);
        currencyService.borrarCurrency(id);
        return new ResponseEntity(new Mensaje("Moneda eliminada Exitosamente"), HttpStatus.OK);
    }

}