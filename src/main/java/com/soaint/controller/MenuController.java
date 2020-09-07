package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcMenu;
import com.soaint.repository.AcMenuRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.soaint.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/menu")
@Api(tags = "MENU")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private AcMenuRepository acMenuRepository;

    //Get Data Menu
    @GetMapping("/menu-all")
    @ApiOperation(value = "Devuelve Informacion de DATA MENU", notes = "INFORMACION DEL MENU")
    public List<AcMenu> findAll(){
        return menuService.obtenerDataMenu();
    }

    //Get para Search(Busqueda) description estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Descripcion  Ascendente y Descendente por Tabla description", notes = "Lista Descripcion Registrados de Menu")
    public List<AcMenu> findAll(
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
        List<AcMenu> list = acMenuRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    AcMenu dao = mapper.readValue(search, AcMenu.class);

                    if (!StringUtils.isEmpty(dao.getDescription())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("description")), "%" + dao.getDescription().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("description")), dao.getDescription().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("description")), dao.getDescription().toLowerCase() + "%")));
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

    //Crear Menu
    @PostMapping("/create-menu")
    @ApiOperation(value = "Crear la Opcion del Menu", notes = "Crear opcion para el menu")
    public ResponseEntity<?> create(@RequestBody AcMenu acMenu){
        menuService.guardarMenu(acMenu);
        return new ResponseEntity(new Mensaje("Opcion del Menu registrada."), HttpStatus.CREATED);
    }

    //Update Menu
    @PutMapping("/update-menu/{id}")
    @ApiOperation(value = "Actualizar Menu por Id", notes = "Actualizar Menu")
    public ResponseEntity<?> update(@RequestBody AcMenu acMenu, @PathVariable("id") Long id){
        if(!menuService.dataMenu(id))
            return new ResponseEntity(new Mensaje("No existe esa opcion del menu"), HttpStatus.NOT_FOUND);
        AcMenu menuUpdate = menuService.menuPorId(id).get();
        menuUpdate.setDescription(acMenu.getDescription());
        menuUpdate.setUrl(acMenu.getUrl());
        menuUpdate.setId_rol(acMenu.getId_rol());
        menuUpdate.setIcon(acMenu.getIcon());
        menuUpdate.setLevel(acMenu.getLevel());
        menuService.guardarMenu(menuUpdate);
        return new ResponseEntity(new Mensaje("Opcion del Menu actualizada"), HttpStatus.CREATED);
    }

    //Eliminar Menu
    @DeleteMapping("/delete-menu/{id}")
    @ApiOperation(value = "Eliminar una Opcion del Menu por Id", notes = "Elimina una opcion del menu que corresponda con el id. La opcion debe existir")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!menuService.dataMenu(id))
            return new ResponseEntity(new Mensaje("No existe esa opcion del menu"), HttpStatus.NOT_FOUND);
        menuService.borrarMenu(id);
        return new ResponseEntity(new Mensaje("Opcion del menu eliminada"), HttpStatus.OK);
    }

}
