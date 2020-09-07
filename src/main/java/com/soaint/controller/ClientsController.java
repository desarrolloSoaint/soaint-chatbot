package com.soaint.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcClients;
import com.soaint.entity.AcClientsPrivate;
import com.soaint.repository.AcClientsPrivateRepository;
import com.soaint.repository.AcClientsRepository;
import com.soaint.service.ClientsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Api(tags = "CLIENTS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class ClientsController {

    @Autowired
    ClientsService clientsService;
    @Autowired
    private AcClientsRepository acClientsRepository;
    @Autowired
    private AcClientsPrivateRepository acClientsPrivateRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    //Get todos los Registros de los Clientes Publicos
    @GetMapping("/registros")
    @ApiOperation(value = "Data de Registro de Cliente Publicos", notes = "Todos los Registros")
    public List<AcClients> findAll(){ return clientsService.obtenerTodos(); }

    //Get para Search(Busqueda) email estilo Json de los Clientes Publicos
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Correo de los Clientes Publicos Ascendente y Descendente por Email", notes = "Lista Correos Registrados de Clientes Publicos")
    public List<AcClients> findAll(
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
        List<AcClients> list = acClientsRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    AcClients dao = mapper.readValue(search, AcClients.class);

                    if (!StringUtils.isEmpty(dao.getEmail())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("email")), "%" + dao.getEmail().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("email")), dao.getEmail().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("email")), dao.getEmail().toLowerCase() + "%")));
                        }

                    }

                    if (!StringUtils.isEmpty(dao.getId())) {
                        if (dao.getId() != 0) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                    root.get("id"), dao.getId()
                            )));
                        }
                    }


                } catch (IOException io) {
                    io.printStackTrace();
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, order);
        return list;
    }

    //Get por Id registro de los Clientes Publicos
    @GetMapping("/{id}")
    @ApiOperation(value = "Busca Registro de un Cliente Publico por Id", notes = "Devuelve los datos relativos a un cliente publico")
    public ResponseEntity<AcClients> getOne(@PathVariable Integer id){
        if(!clientsService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe ese cliente"), HttpStatus.NOT_FOUND);
        AcClients clients = clientsService.obtenerPorId(id).get();
        return new ResponseEntity<AcClients>(clients, HttpStatus.OK);
    }

    //Save Registro Cliente Publico
    @PostMapping("/create-public")
    @ApiOperation(value = "Registra un Cliente Publico", notes = "Crea un nuevo cliente publico a partir de pais y un email. El email no debe existir")
    public ResponseEntity<?> create(@Valid @RequestBody AcClients acClients, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(clientsService.existePorEmail(acClients.getEmail()))
            return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        clientsService.guardar(acClients);
        return new ResponseEntity(new Mensaje("Cliente registrado."), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    @ApiOperation(value = "Acceso a Clientes Publicos", notes = "Crea un nuevo cliente publico a partir de pais y un email")
    public ResponseEntity<?> createpublic(@Valid @RequestBody AcClients acClients, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        clientsService.guardar(acClients);
        return new ResponseEntity(new Mensaje("Cliente registrado."), HttpStatus.CREATED);
    }

    //Update Registro Cliente Publico
    @PutMapping("/update/{id}")
    @ApiOperation(value = "Actualiza los Datos de Registro de un Cliente Publico por Id", notes = "Actualiza los datos del cliente que se corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> update(@Valid @RequestBody AcClients acClients,BindingResult bindingResult, @PathVariable("id") Integer id){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(!clientsService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el cliente"), HttpStatus.NOT_FOUND);
        if(clientsService.existePorEmail(acClients.getEmail()) &&
                clientsService.getByEmail(acClients.getEmail()).get().getId() != id)
            return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        AcClients acClientsUpdate = clientsService.obtenerPorId(id).get();
        acClientsUpdate.setEmail(acClients.getEmail());
        acClientsUpdate.setId_country(acClients.getId_country());
        clientsService.guardar(acClientsUpdate);
        return new ResponseEntity(new Mensaje("Cliente actualizado"), HttpStatus.CREATED);
    }

    //Delete Registro Cliente Publico
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Elimina Registro de un Cliente Publico por Id", notes = "Elimina los datos del cliente que se corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if(!clientsService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el cliente"), HttpStatus.NOT_FOUND);
        clientsService.borrar(id);
        return new ResponseEntity(new Mensaje("Cliente eliminado"), HttpStatus.OK);
    }

    //Get Correos de los Registros de los Clientes Publicos
    @GetMapping("/list-client-public")
    @ApiOperation(value = "Devuelve Correos de los Clientes Publicos Registrados", notes = "Muestra los Correos de los Clientes Publicos")
    public List<AcClients> ClientsPublic(){ return clientsService.ClientsPublic(); }

    //Get Conteo de los Registros de los Clientes Publicos
    @GetMapping("/count-client-public")
    @ApiOperation(value = "Devuelve la Cantidad de Clientes Publicos Registrados", notes = "Muestra la cantidad de Clientes Publicos")
    public Object ClientsPublicCount(){
        Long lista = clientsService.ClientsPublicCount();
        return lista;}

    @GetMapping("/count-week-client-public")
    @ApiOperation(value = "Devuelve la cantidad de clientes públicos registrados de la última semana", notes = "Muestra la cantidad de Clientes públicos registrados en la última semana")
    public Object ClientsPublicCountWeek(){
        return clientsService.ClientePublicCountLastWeek();
    }

    @GetMapping("/count-day-client-public")
    @ApiOperation(value = "Devuelve la cantidad de clientes públicos registrados en el último dia", notes = "Muestra la cantidad de clientes públicos registrados en el último dia")
    public Object ClientsPublicCountDay(){
        return clientsService.ClientePublicCountLastDay();
    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////CONTROLADORES DE CLIENTES PRIVADOS


//    ME TRAE TODO LOS REGISTROS DE LA BASE DE DATOS

    //Get todos los Registros de los Clientes Privados
    @GetMapping("/registros/private")
    @ApiOperation(value = "Devuelve la Data de Registros de los Clientes Privados", notes = "Todos los registros de clientes privados")
    public List<AcClientsPrivate> findAll1(){ return clientsService.obtenerTodosPrivados(); }

    //Get para Search(Busqueda) email estilo Json de los Clientes Privados
    @GetMapping("/find-all-searchPrivate")
    @ApiOperation(value = "Busqueda Correo de los Clientes Privados Ascendente y Descendente por Email", notes = "Lista Correos Registrados de CLientes Privados")
    public List<AcClientsPrivate> findAllPrivate(
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
        List<AcClientsPrivate> list = acClientsPrivateRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    AcClientsPrivate dao = mapper.readValue(search, AcClientsPrivate.class);

                    if (!StringUtils.isEmpty(dao.getEmail())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("email")), "%" + dao.getEmail().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("email")), dao.getEmail().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("email")), dao.getEmail().toLowerCase() + "%")));
                        }

                    }

                    if (!StringUtils.isEmpty(dao.getId())) {
                        if (dao.getId() != 0) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                    root.get("id"), dao.getId()
                            )));
                        }
                    }


                } catch (IOException io) {
                    io.printStackTrace();
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, order);
        return list;
    }

    //ME TRAE UN REGISTRO POR ID DE LA BASE DE DATOS
    @GetMapping("/private/{id}")
    @ApiOperation(value = "Busca Registro de un Cliente Privado por Id", notes = "Devuelve los datos relativos a un cliente privado")
    public ResponseEntity<AcClientsPrivate> getOnePrivate(@PathVariable Integer id){
        if(!clientsService.existePorIdPrivate(id))
            return new ResponseEntity(new Mensaje("No existe ese cliente privado"), HttpStatus.NOT_FOUND);
        AcClientsPrivate clientsPrivate = clientsService.obtenerPorIdPrivate(id).get();
        return new ResponseEntity<AcClientsPrivate>(clientsPrivate, HttpStatus.OK);
    }

    //Save acceso Cliente Privado
    @PostMapping("/acces-private")
    @ApiOperation(value = "Acceso un Cliente Privado", notes = "Accesa un nuevo cliente privado a partir de pais y un email. El email no debe existir")
    public ResponseEntity<?> accesPrivate(@Valid @RequestBody AcClientsPrivate acClientsPrivate, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        clientsService.savePrivado(acClientsPrivate);
        return new ResponseEntity(new Mensaje("Cliente registrado."), HttpStatus.CREATED);
    }

    //REGISTRO DE NUEVO CLIENTE PRIVADO
    @PostMapping("/create/private")
    @ApiOperation(value = "Registro de Cliente Privado", notes = "Crea un nuevo cliente a partir de pais, un email y el Password. El email no debe existir")
    public ResponseEntity<?> createPrivate(@Valid @RequestBody AcClientsPrivate acClientsPrivate, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(clientsService.existePorEmailPrivado(acClientsPrivate.getEmail()))
            return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);

        AcClientsPrivate acClientsPrivate1 =
                new AcClientsPrivate(
                        acClientsPrivate.getEmail(),
                        passwordEncoder.encode(acClientsPrivate.getPassword()),
                        acClientsPrivate.getId_country(),
                        acClientsPrivate.getCreated_at());


        clientsService.savePrivado(acClientsPrivate1);
        return new ResponseEntity(acClientsPrivate1, HttpStatus.CREATED);
    }

    //ACTUALIZA UN CLIENTE PRIVADO
    @PutMapping("/update/private/{id}")
    @ApiOperation(value = "Actualiza los Datos de Registro de un Cliente Privado por Id", notes = "Actualiza los datos del cliente que se corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> updatePrivate(@Valid @RequestBody AcClientsPrivate acClientsPrivate,BindingResult bindingResult, @PathVariable("id") Integer id){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(!clientsService.existePorIdPrivate(id))
            return new ResponseEntity(new Mensaje("No existe el cliente"), HttpStatus.NOT_FOUND);
        if(clientsService.existePorEmailPrivado(acClientsPrivate.getEmail()) &&
                clientsService.getByEmailPrivado(acClientsPrivate.getEmail()).get().getId() != id)
            return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        AcClientsPrivate acClientsPrivateUpdate = clientsService.obtenerPorIdPrivate(id).get();
        acClientsPrivateUpdate.setEmail(acClientsPrivate.getEmail());
        acClientsPrivateUpdate.setId_country(acClientsPrivate.getId_country());
        acClientsPrivateUpdate.setPassword(passwordEncoder.encode(acClientsPrivate.getPassword()));
        clientsService.savePrivado(acClientsPrivateUpdate);

        return new ResponseEntity(new Mensaje("Cliente Privado actualizado"), HttpStatus.CREATED);
    }

    //ELIMINA CLIENTE PRIVADO
    @DeleteMapping("/delete/private/{id}")
    @ApiOperation(value = "Elimina Registro de un Cliente Privado por Id", notes = "Elimina los datos del cliente privado que se corresponda con el id. El cliente debe existir")
    public ResponseEntity<?> deletePrivate(@PathVariable Integer id){
        if(!clientsService.existePorIdPrivate(id))
            return new ResponseEntity(new Mensaje("No existe el cliente"), HttpStatus.NOT_FOUND);
        clientsService.borrarPrivado(id);
        return new ResponseEntity(new Mensaje("Cliente eliminado"), HttpStatus.OK);
    }

    //Get Correos de los Registros de los Clientes Privados
    @GetMapping("/list-client-private")
    @ApiOperation(value = "Devuelve Correos de los Clientes Privados Registrados", notes = "Muestra los Correos de los Clientes Privados")
    public List<AcClientsPrivate> ClientsPrivate(){ return clientsService.ClientsPrivate(); }

    @GetMapping("/count-client-private")
    @ApiOperation(value = "Devuelve la Cantidad de Clientes Privados Registrados", notes = "Muestra la cantidad de Clientes Privados")
    public Object ClientsPrivateCount(){
        Long lista = clientsService.ClientsPrivateCount();
        return lista;}

    @GetMapping("/count-week-client-private")
    @ApiOperation(value = "Devuelve la cantidad de clientes privados registrados de la última semana", notes = "Muestra la cantidad de clientes privados registrados en la última semana")
    public Object ClientsPrivateCountWeek(){
        return clientsService.ClientePrivadoCountLastWeek();
    }

    @GetMapping("/count-day-client-private")
    @ApiOperation(value = "Devuelve la cantidad de clientes privados registrados en el último dia", notes = "Muestra la cantidad de clientes privados registrados en el último dia")
    public Object ClientsPrivateCountDay(){
        return clientsService.ClientePrivadoCountLastDay();
    }

}
