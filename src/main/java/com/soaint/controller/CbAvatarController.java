package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcAiml;
import com.soaint.entity.CbAvatar;
import com.soaint.repository.CbAvatarRepository;
import com.soaint.service.CbAvatarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/avatar")
@Api(tags = "LOOK AND FEEL")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class CbAvatarController {

    //Formato de archivos validos (png)
    private static final List<String> formatoValido = Arrays.asList("image/png");

    @Autowired
    private CbAvatarRepository cbAvatarRepository;

    @Autowired
    private CbAvatarService cbAvatarService;

    //Get registro Avatar de forma Ascendente por la tabla avatar
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve Registro Avatares forma Ascendente por Registro la Tabla avatar", notes = "Lista de avatares")
    public List<CbAvatar> findAll(){

        return cbAvatarRepository.findAll(Sort.by(Sort.Direction.ASC, "avatar"));
    }

    //Get por Id el registro del Avatar
    @GetMapping("/find-avatar/{id}")
    @ApiOperation(value = "Buscar Registro del Avatar por Id", notes = "Buscar Avatar")
    public ResponseEntity<CbAvatar> getOne(@PathVariable Long id){
        if(!cbAvatarService.cbAvatarId(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);
        CbAvatar cbAvatar = cbAvatarService.cbAvatarPorId(id).get();
        return new ResponseEntity<CbAvatar>(cbAvatar, HttpStatus.OK);
    }

    //Get para Search(Busqueda) avatar estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Registro del Avatar Ascendente y Descendente por avatar", notes = "Busqueda de Avatares Registrados")
    public List<CbAvatar> findAll(
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
        List<CbAvatar> list = cbAvatarRepository.findAll((Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(search)) {

                try {
                    CbAvatar dao = mapper.readValue(search, CbAvatar.class);

                    if (!StringUtils.isEmpty(dao.getAvatar())) {
                        if (!StringUtils.isEmpty(sensitive)) {
                            if (!!sensitive) {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("avatar")), "%" + dao.getAvatar().toLowerCase() + "%")));
                            } else {
                                predicates.add(criteriaBuilder.and(criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("avatar")), dao.getAvatar().toLowerCase() + "%")));
                            }
                        } else {

                            predicates.add(criteriaBuilder.and(criteriaBuilder
                                    .like(criteriaBuilder.lower(root.get("avatar")), dao.getAvatar().toLowerCase() + "%")));
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

    //Get Contador cantidad avatar Registrados
    @GetMapping("/count-avatar")
    @ApiOperation(value = "Cantidad de Registro del Avatar", notes = "Cuenta la cantidad de registro de LOOK AND FEEL Avatar")
    public long countAll(){
        return cbAvatarService.contadorAvatar();
    }

    //save Avatar
    @PostMapping("/create-avatar")
    @ApiOperation(value = "Crear Nuevo Avatar", notes = "Crear Avatar" )
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) throws IOException {

        if(cbAvatarService.existsByAvatar(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El avatar ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();

        try {
            //Valida en un array los tipos de formatos permitidos
            if(formatoValido.contains(tipoFormato)) {
                //Guarda el archivo en la carpeta /images/avatar
                cbAvatarService.saveFile(file);

                //Guarda la ruta en la base de datos
                String archivo = file.getOriginalFilename();
                archivo = archivo.replace(" ", "");

                CbAvatar cbAvatar = new CbAvatar();
                cbAvatar.setAvatar(archivo);
                cbAvatarService.guardarAvatar(cbAvatar);

                return new ResponseEntity(new Mensaje("Avatar registrado exitosamente"), HttpStatus.OK);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    // update Avatar
    @PutMapping("/update-avatar/{id}")
    @ApiOperation(value = "Actualizar Avatar por Id", notes = "Actualizar Avatar")
    public ResponseEntity<?> update(@PathVariable("id") Long id, MultipartFile file) throws IOException {
        if(!cbAvatarService.cbAvatarId(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);
        if(cbAvatarService.existsByAvatar(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El avatar ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();

        try {
            //Valida en un array los tipos de formatos permitidos
            if(formatoValido.contains(tipoFormato)) {

                //Guarda la ruta en la base de datos
                String archivo = file.getOriginalFilename();
                archivo = archivo.replace(" ", "");

                //Elimina el archivo del avatar por el id
                CbAvatar avatarUpdate = cbAvatarService.cbAvatarPorId(id).get();
                String nameAvatar = avatarUpdate.getAvatar();
                cbAvatarService.eliminarFileAvatar(nameAvatar);

                //Guarda el archivo en la carpeta /images/avatar
                cbAvatarService.saveFile(file);

                avatarUpdate.setAvatar(archivo);
                cbAvatarService.guardarAvatar(avatarUpdate);

                return new ResponseEntity(new Mensaje("Avatar actualizado exitosamente"), HttpStatus.CREATED);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    //delete Avatar
    @DeleteMapping("/delete-avatar/{id}")
    @ApiOperation(value = "Eliminar Avatar por Id", notes = "Eliminar Avatar")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!cbAvatarService.cbAvatarId(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);

        //Elimina el archivo del avatar por el id
        CbAvatar avatarDelete = cbAvatarService.cbAvatarPorId(id).get();
        String nameAvatar = avatarDelete.getAvatar();
        cbAvatarService.eliminarFileAvatar(nameAvatar);

        cbAvatarService.borrarAvatar(id);
        return new ResponseEntity(new Mensaje("Avatar eliminado Exitosamente"), HttpStatus.OK);
    }

    //Ver Avatar
    @GetMapping("/uploads/img/{nombreFoto:.+}")
    @ApiOperation(value = "Ver Avatar Creado con su Respectiva Ruta", notes = "Ver avatar")
    public ResponseEntity<Resource> verAvatar(@PathVariable String nombreFoto){

        Resource recurso = null;

        try {
            recurso = cbAvatarService.cargar(nombreFoto);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return new ResponseEntity(recurso, HttpStatus.OK);
    }

}
