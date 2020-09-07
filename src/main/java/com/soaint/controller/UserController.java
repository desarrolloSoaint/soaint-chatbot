package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.DTO.NewUser;
import com.soaint.entity.Rol;
import com.soaint.entity.Users;
import com.soaint.repository.UserRepository;
import com.soaint.service.RolService;
import com.soaint.service.UserService;
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
import java.util.*;

@Api(tags = "USERS")
@RequestMapping("/api/v1/user")
@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RolService rolService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //Get Usuarios Registrados
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Lista de Usuarios Registrados", notes = "Todos los registros de usuarios")
    public List<Users> findAll(){
        return userService.obtenerTodos();
    }

    //Get Usuarios Registrados por Id
    @GetMapping("/{id}")
    @ApiOperation(value = "Busca un usuario por ID", notes = "Devuelve los datos relativos de un usuario")
    public ResponseEntity<Users> getOne(@PathVariable int id){
        if(!userService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe ese usuario"), HttpStatus.NOT_FOUND);
        Users user = userService.obtenerPorId(id).get();
        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    //Get para Search(Busqueda) email estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Usuarios  Ascendente y Descendente por Email", notes = "Lista Correos Registrados de Usuarios")
    public List<Users> findAll(
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
        List<Users> list = userRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    Users dao = mapper.readValue(search, Users.class);

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

    //Update Usuarios
    @PutMapping("/update/{id}")
    @ApiOperation(value = "Actualiza los Datos de un Usuario Registrado por Id", notes = "Actualiza los datos del usuario que se corresponda con el id. El usuario debe existir")
    public ResponseEntity<?> update(@Valid @RequestBody NewUser newUser, @PathVariable("id") int id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados o email invalido"), HttpStatus.BAD_REQUEST);
        if(!userService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);
        //Verifica si el email existe y si no es el mismo email
        if(userService.existsByEmail(newUser.getEmail()) &&
                userService.getByEmail(newUser.getEmail()).get().getId() != id)
            return new ResponseEntity(new Mensaje("El email ya existe"), HttpStatus.BAD_REQUEST);
        Users userUpdate = userService.obtenerPorId(id).get();
        userUpdate.setEmail(newUser.getEmail());
        userUpdate.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userUpdate.setRoles(newUser.getRoles());
        userUpdate.setCreated_at(newUser.getCreated_at());
        userService.save(userUpdate);
        return new ResponseEntity(new Mensaje("Usuario actualizado exitosamente"), HttpStatus.CREATED);
    }

    //Delete Usuario
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Elimina un Usuario Registrado por Id", notes = "Elimina el usuario que se corresponda con el id. El usuario debe existir ademas elimina su data")
    public ResponseEntity<?> delete(@PathVariable int id){
        if(!userService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);
        userService.borrar(id);
        return new ResponseEntity(new Mensaje("Usuario eliminado"), HttpStatus.OK);
    }

    //get cantidad usuarios
    @GetMapping("/count-users")
    @ApiOperation(value = "Devuelve Cantidad de Usuarios en el sistema", notes = "Cuenta la cantidad de Usuarios en el sistema")
    public long countAll(){
        return userService.contadorUser();
    }

    //Get Cantidad Usuarios por Semana
    @GetMapping("/count-week-users")
    @ApiOperation(value = "Devuelve Cantidad de Usuarios Registrados en el sistema por Semana", notes = "Cuenta la cantidad de Usuarios en el sistema por Semana")
    public Object UsersCountWeek(){
        Long cantidadSemanal = userService.UserCountLastWeek();
        return cantidadSemanal;
    }

    //Get Cantidad Usuarios por Dia
    @GetMapping("/count-day-users")
    @ApiOperation(value = "Devuelve Cantidad de Usuarios Registrados en el sistema por Dia", notes = "Cuenta la cantidad de Usuarios en el sistema por Dia")
    public Object UsersCountDay(){
        Long cantidadDia = userService.UserCountLastDay();
        return cantidadDia;
    }
}
