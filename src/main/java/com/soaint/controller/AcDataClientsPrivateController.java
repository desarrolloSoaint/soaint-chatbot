package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcDataClientsPrivate;
import com.soaint.repository.AcDataClientsPrivateRepository;
import com.soaint.service.AcDataClientsPrivateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients-data")
@Api(tags = "CLIENTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AcDataClientsPrivateController {

    @Autowired
    AcDataClientsPrivateService acDataClientsPrivateService;

    @Autowired
    AcDataClientsPrivateRepository acDataClientsPrivateRepository;
    private List<String> id;
    private List<String> columnNames;

    //Get a toda la data de los Clientes Privados
    @GetMapping("/find-data-client-private")
    @ApiOperation(value = "Devuelve toda la Data Registrada de los Clientes Privados", notes = "Devuelve toda la data de los clientes del sistema")
    public List<AcDataClientsPrivate> findAll(){
        return acDataClientsPrivateService.obtenerDataClientsPrivate();
    }

    //Get por Id la data de los Clientes Privados
    @GetMapping("/find-data-clients-private/{id}")
    @ApiOperation(value = "Busca Data de un Cliente Privado por Id", notes = "Devuelve los datos relativos de un cliente privado")
    public ResponseEntity<AcDataClientsPrivate> getOne(@PathVariable Integer id){
        if(!acDataClientsPrivateService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe ese cliente"), HttpStatus.NOT_FOUND);
        AcDataClientsPrivate acDataClientsPrivate = acDataClientsPrivateService.dataPorId(id).get();
        return new ResponseEntity<AcDataClientsPrivate>(acDataClientsPrivate, HttpStatus.OK);
    }

    //Get para Search(Busqueda) names, last_names y identification_card estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Data del Cliente Privado Ascendente y Descendente por Arreglo Names, Last_Names y Identification_Card", notes = "Busqueda de Clientes Privados Registrados")
    public List<AcDataClientsPrivate> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sensitive", required = false) Boolean sensitive,
            @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
            @RequestParam(value = "columnNames", required = false) List<String> columnNames
    ) {


        // En el front columnNames deben pasar datos asi si se va a ordenar más de una
        // columna. ejemplo: columnName=id,firstName,lastName. En el front sería ["id","firstName","lastName"]
        // si es un solo elemento sería ["id"]
        Boolean sensitives = (StringUtils.isEmpty(sensitive)) ? false : sensitive;

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

        List<AcDataClientsPrivate> list = acDataClientsPrivateRepository.findAll((Specification<AcDataClientsPrivate>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(search)) {
                try {
                    AcDataClientsPrivate dao = mapper.readValue(search, AcDataClientsPrivate.class);


                    if (!StringUtils.isEmpty(dao.getNames())) {
                        if (!StringUtils.isEmpty(sensitives)) {
                            if (!!sensitives) {
                                System.out.println("__names__****____#");
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("names")), "%" + dao.getNames().toLowerCase() + "%")));
                            }
                        } else {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("names"), dao.getNames())));
                        }

                    }

                    if (!StringUtils.isEmpty(dao.getLast_names())) {
                        if (!StringUtils.isEmpty(sensitives)) {
                            if (!!sensitives) {
                                System.out.println("__last_names__****____#");
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("last_names")), "%" + dao.getLast_names().toLowerCase() + "%")));
                            }
                        } else {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("last_names"), dao.getLast_names())));
                        }

                    }

                    if (!StringUtils.isEmpty(dao.getIdentification_card())) {

                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("identification_card"), dao.getIdentification_card())));

                    }

                } catch (IOException io) {
                    System.out.println(io.getMessage());
                }
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, order);

        return list;

    }

    //Save Data Cliente Privado
    @PostMapping("/create-data-clients-private")
    @ApiOperation(value = "Crear Data del Cliente Privado", notes = "Crear data de un nuevo cliente privado")
    public ResponseEntity<?> create(@RequestBody AcDataClientsPrivate acDataClientsPrivate){
        acDataClientsPrivateService.saveData(acDataClientsPrivate);
        return new ResponseEntity(new Mensaje("Data del Usuario registrado."), HttpStatus.CREATED);
    }

    //Update Data Cliente Privado
    @PutMapping("/update-data-clients-private/{id}")
    @ApiOperation(value = "Actualizar Data de un Cliente Privado por Id", notes = "Actualiza los datos del cliente que se corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> update(@RequestBody AcDataClientsPrivate acDataClientsPrivate, @PathVariable("id") Integer id){
        if(!acDataClientsPrivateService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe el cliente"), HttpStatus.NOT_FOUND);
        AcDataClientsPrivate dataClientUpdate = acDataClientsPrivateService.dataPorId(id).get();
        dataClientUpdate.setIdentification_card(acDataClientsPrivate.getIdentification_card());
        dataClientUpdate.setNames(acDataClientsPrivate.getNames());
        dataClientUpdate.setLast_names(acDataClientsPrivate.getLast_names());
        dataClientUpdate.setBirth_date(acDataClientsPrivate.getBirth_date());
        dataClientUpdate.setMobile_phone(acDataClientsPrivate.getMobile_phone());
        dataClientUpdate.setId_client_private(acDataClientsPrivate.getId_client_private());
        acDataClientsPrivateService.saveData(dataClientUpdate);
        return new ResponseEntity(new Mensaje("Usuario actualizado"), HttpStatus.CREATED);
    }

    //Delete Data Cliente Privado
    @DeleteMapping("/delete-data-client-private/{id}")
    @ApiOperation(value = "Eliminar la Data de un Cliente Privado por Id", notes = "Elimina la data del cliente que corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if(!acDataClientsPrivateService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe data del cliente"), HttpStatus.NOT_FOUND);
        acDataClientsPrivateService.borrarData(id);
        return new ResponseEntity(new Mensaje("Data del CLiente eliminado"), HttpStatus.OK);
    }
}
