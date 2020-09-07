package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.AcDataUsersDto;
import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcDataUsers;
import com.soaint.entity.CbAvatar;
import com.soaint.repository.AcDataUsersRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.soaint.service.DataUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "USERS")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class DataUsersController {

    //Formato de archivos validos (png)
    private static final List<String> formatoValido = Arrays.asList("image/png");

    @Autowired
    DataUsersService dataUsersService;

    @Autowired
    AcDataUsersRepository acDataUsersRepository;


    //Get a toda la data de los Usuarios
    @GetMapping("/find-data-users")
    @ApiOperation(value = "Devuelve toda la Data de los Usuarios", notes = "Devuelve toda la data de los usuarios del sistema")
    public List<AcDataUsers> findAll(){
        return dataUsersService.obtenerDataUsers();
    }

    //Get para Search(Busqueda) names, last_names y identification_card estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Data de los Usuarios Ascendente y Descendente por Arreglo Names, Last_Names y Identification_Card", notes = "Lista de Users")
    public List<AcDataUsers> findAll(
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

        List<AcDataUsers> list = acDataUsersRepository.findAll((Specification<AcDataUsers>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(search)) {
                try {
                    AcDataUsers dao = mapper.readValue(search, AcDataUsers.class);


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
    //Get por Id la data de los Usuarios
    @GetMapping("/find-data-users/{id}")
    @ApiOperation(value = "Buscar la Data Registrada de un Usuario por Id", notes = "Devuelve los datos relativos de un usuario")
    public ResponseEntity<AcDataUsers> getOne(@PathVariable Integer id){
        if(!dataUsersService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe ese usuario"), HttpStatus.NOT_FOUND);
        AcDataUsers acDataUsers = dataUsersService.dataPorId(id).get();
        return new ResponseEntity<AcDataUsers>(acDataUsers, HttpStatus.OK);
    }
    //Save Data Usuario
    @PostMapping("/create-data-user")
    @ApiOperation(value = "Crear Data del Usuario", notes = "Crear data de un nuevo usuario")
    public ResponseEntity<?> create(@Valid @RequestBody AcDataUsersDto acDataUsersDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos erroneos"), HttpStatus.BAD_REQUEST);
        AcDataUsers acDataUsers = new AcDataUsers(acDataUsersDto.getIdentification_card(),
                acDataUsersDto.getNames(),        acDataUsersDto.getLast_names(),
                acDataUsersDto.getBirth_date(),   acDataUsersDto.getId_gender(),
                acDataUsersDto.getMobile_phone(), acDataUsersDto.getLocal_telephone(),
                acDataUsersDto.getId_user(), acDataUsersDto.getId_country());
        dataUsersService.crearData(acDataUsers);
        return new ResponseEntity(new Mensaje("Data de usuario creada"), HttpStatus.CREATED);
    }

    //Update Data Usuario
    @PutMapping("/update-data-user/{id}")
    @ApiOperation(value = "Actualiza los Datos de un Usuario por Id", notes = "Actualiza los datos del usuario que se corresponda con el id. El usuario debe existir")
    public ResponseEntity<?> update(@Valid @RequestBody AcDataUsersDto acDataUsersDto, @PathVariable("id") Integer id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!dataUsersService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);
        AcDataUsers dataUserUpdate = dataUsersService.dataPorId(id).get();
        dataUserUpdate.setIdentification_card(acDataUsersDto.getIdentification_card());
        dataUserUpdate.setNames(acDataUsersDto.getNames());
        dataUserUpdate.setLast_names(acDataUsersDto.getLast_names());
        dataUserUpdate.setBirth_date(acDataUsersDto.getBirth_date());
        dataUserUpdate.setId_gender(acDataUsersDto.getId_gender());
        dataUserUpdate.setMobile_phone(acDataUsersDto.getMobile_phone());
        dataUserUpdate.setLocal_telephone(acDataUsersDto.getLocal_telephone());
        dataUserUpdate.setId_user(acDataUsersDto.getId_user());
        dataUserUpdate.setId_country(acDataUsersDto.getId_country());
        dataUsersService.guardarData(dataUserUpdate);
        return new ResponseEntity(new Mensaje("Data de usuario actualizada"), HttpStatus.CREATED);
    }

    //Delete Data Usuario
    @DeleteMapping("/delete-data-user/{id}")
    @ApiOperation(value = "Elimina la Data de un Usuario por Id", notes = "Elimina la data del usuario que corresponda con el id. El usuario debe existir")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if(!dataUsersService.dataId(id))
            return new ResponseEntity(new Mensaje("No existe data del usuario"), HttpStatus.NOT_FOUND);
        dataUsersService.borrarData(id);
        return new ResponseEntity(new Mensaje("Data del Usuario eliminada"), HttpStatus.OK);
    }

    //save imagen de usuario
    @PutMapping("/update-img-user/{id}")
    @ApiOperation(value = "Actualizar Imagen del Usuario Registrado", notes = "Actualizar la imagen del usuario" )
    public ResponseEntity<?> createImgUser(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws IOException {

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


                AcDataUsers dataUserFotoUpdate = dataUsersService.dataPorId(Math.toIntExact(id)).get();
                //Elimina el archivo de la imagen del usuario en la carpeta
                String nameImgUser = dataUserFotoUpdate.getImage_user();
                dataUsersService.eliminarFileImgUser(nameImgUser);

                //Guarda el archivo en la carpeta /images/avatar
                dataUsersService.saveFile(file);

                dataUserFotoUpdate.setImage_user(archivo);
                dataUsersService.guardarData(dataUserFotoUpdate);

                return new ResponseEntity(new Mensaje("Avatar registrado exitosamente"), HttpStatus.OK);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    //Get Ver Foto Usuario
    @GetMapping("/uploads/users/{nombreFoto:.+}")
    @ApiOperation(value = "Ver Foto del Usuario Registrado", notes = "Ver foto de usuario")
    public ResponseEntity<Resource> verAvatar(@PathVariable String nombreFoto){

        Resource recurso = null;

        try {
            recurso = dataUsersService.cargar(nombreFoto);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return new ResponseEntity(recurso, HttpStatus.OK);
    }

}
