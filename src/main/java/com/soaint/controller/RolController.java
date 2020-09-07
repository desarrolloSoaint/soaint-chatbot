package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.Rol;
import com.soaint.repository.RolRepository;
import com.soaint.service.RolService;
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
@RequestMapping("/api/v1/rol")
@Api(tags = "USERS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class RolController {

    @Autowired
    RolService rolService;

    @Autowired
    RolRepository rolRepository;

    //get cantidad Roles forma Ascendente por tabla rol_name
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Lista de Roles Ascendente por Nombre de la tabla rolname", notes = "Lista de roles")
    public List<Rol> findAll() {

        return rolService.getAll();
    }

    //Get para Search(Busqueda) rolname estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Rol Ascendente y Descendente por Id y RolName", notes = "Lista de Roles")
    public List<Rol> findAll(
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
        List<Rol> list = rolRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Rol dao = mapper.readValue(search, Rol.class);

                    if (!StringUtils.isEmpty(dao.getRolName())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("rolName")), "%" + dao.getRolName().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("rolName")), dao.getRolName().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("rolName")), dao.getRolName().toLowerCase() + "%")));
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

    //get cantidad Roles
    @GetMapping("/count-rol")
    @ApiOperation(value = "Devuelve Cantidad de Registro de Roles", notes = "Cuenta la cantidad de registro de Roles")
    public long countAll(){
        return rolService.contadorRol();
    }

    //Get rol by Id
    @GetMapping("/{id}")
    @ApiOperation(value = "Buscar un Rol por Id", notes = "Devuelve los datos relativos de un rol")
    public ResponseEntity<Rol> getOne(@PathVariable int id){
        if(!rolService.existById(id))
            return new ResponseEntity(new Mensaje("No existe el rol con ese id"), HttpStatus.NOT_FOUND);
        Rol rol = rolService.getById(id).get();
        return new ResponseEntity<Rol>(rol, HttpStatus.OK);
    }

    //Crear Rol
    @PostMapping("/create")
    @ApiOperation(value = "Crear un Nuevo Rol", notes = "Crear rol")
    public ResponseEntity<?> create(@Valid @RequestBody Rol rol, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(rolService.existsByRolName(rol.getRolName()))
            return new ResponseEntity(new Mensaje("El rol ya existe"), HttpStatus.BAD_REQUEST);
        rolService.save(rol);
        return new ResponseEntity(new Mensaje("Rol registrado exitosamente."), HttpStatus.CREATED);
    }

    //update Rol
    @PutMapping("/update/{id}")
    @ApiOperation(value = "Actualizar Un Rol por Id", notes = "Actualizar el rol que se corresponda con el id. El rol debe existir")
    public ResponseEntity<?> update(@Valid @RequestBody Rol rol, @PathVariable("id") int id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!rolService.existById(id))
            return new ResponseEntity(new Mensaje("No existe el rol con el id ingresado"), HttpStatus.NOT_FOUND);
        //Verifica si el rol existe y si no es el mismo rol
        if(rolService.existsByRolName(rol.getRolName()) &&
                rolService.getByRolName(rol.getRolName()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El rol ya existe"), HttpStatus.BAD_REQUEST);
        Rol rolUpdate = rolService.getById(id).get();
        rolUpdate.setRolName(rol.getRolName());
        rolService.save(rolUpdate);
        return new ResponseEntity(new Mensaje("Rol actualizado exitosamente"), HttpStatus.CREATED);
    }

    //Delete Rol
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Eliminar un Rol por Id", notes = "Elimina el rol que se corresponda con el id. El rol debe existir")
    public ResponseEntity<?> delete(@PathVariable int id){
        if(!rolService.existById(id))
            return new ResponseEntity(new Mensaje("No existe el rol con el id ingresado"), HttpStatus.NOT_FOUND);
        rolService.delete(id);
        return new ResponseEntity(new Mensaje("Rol eliminado"), HttpStatus.OK);
    }


}
