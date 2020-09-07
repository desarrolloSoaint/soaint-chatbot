package com.soaint.controller;

import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcAiml;
import com.soaint.repository.AcAimlRepository;
import com.soaint.service.AcAimlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fileaiml")
@Api(tags = "CHATBOT")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AcAimlController {

    //Formato de archivos validos (aiml)
    private static final List<String> formatoValido = Arrays.asList("application/octet-stream");

    @Autowired
    private AcAimlRepository acAimlRepository;

    @Autowired
    private AcAimlService acAimlService;

    //get Aiml de forma ascendente
    @GetMapping("/find-all")
    @ApiOperation(value = "Buscar todos los registros aiml", notes = "Lista de registros")
    public List<AcAiml> findAll(){
        return acAimlService.obtenerTodos();
    }

    //get Aiml de forma ascendente
    @GetMapping("/find-all-order")
    @ApiOperation(value = "Buscar aiml ascendentes por nombres", notes = "Lista de aiml por nombre ascendentes")
    public List<AcAiml> findAllOrder(){
        return acAimlRepository.findAll(Sort.by(Sort.Direction.ASC, "aiml"));
    }

    //Obtener Aiml by id
    @GetMapping("/find-aiml/{id}")
    @ApiOperation(value = "Buscar aiml por id", notes = "Buscar aiml por id")
    public ResponseEntity<AcAiml> getOne(@PathVariable Long id){
        if(!acAimlService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe ese aiml"), HttpStatus.NOT_FOUND);
        AcAiml acAiml = acAimlService.obtenerPorId(id).get();
        return new ResponseEntity<AcAiml>(acAiml, HttpStatus.OK);
    }

    //Obtener cantidad de archivos aiml
    @GetMapping("/count-aiml")
    @ApiOperation(value = "Cantidad de registro de aiml", notes = "Cuenta la cantidad de registro de archivos aiml")
    public long countAll(){
        return acAimlService.contadorAiml();
    }

    //Guardar archivo aiml
    @PostMapping("/create-aiml")
    @ApiOperation(value = "Crear archivo aiml", notes = "Crear archivo aiml" )
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) throws IOException {

        if(acAimlService.existsByAiml(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El archivo aiml ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();

        try {
            //Valida en un array los tipos de formatos permitidos
            if(formatoValido.contains(tipoFormato)) {
                //Guarda el archivo en la carpeta de destino /aiml
                acAimlService.saveFile(file);

                //Guarda la ruta en la base de datos
                String archivo = file.getOriginalFilename();
                archivo = archivo.replace(" ", "");

                Date fechaCreate = new Date();

                AcAiml acAiml = new AcAiml();
                acAiml.setAiml(archivo);
                acAiml.setCreatedAt(fechaCreate);
                acAimlService.save(acAiml);

                return new ResponseEntity(new Mensaje("Archivo aiml registrado exitosamente"), HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo ("+tipoFormato+") no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    // Actualizar archivo aiml
    @PutMapping("/update-aiml/{id}")
    @ApiOperation(value = "Actualizar archivo aiml", notes = "Actualizar archivo aiml")
    public ResponseEntity<?> update(@PathVariable("id") Long id, MultipartFile file) throws IOException {
        if(!acAimlService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el aiml con ese id"), HttpStatus.NOT_FOUND);
        if(acAimlService.existsByAiml(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El archivo aiml ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();

        try {
            if(formatoValido.contains(tipoFormato)) {

                //Guarda la ruta en la base de datos
                String archivo = file.getOriginalFilename();
                archivo = archivo.replace(" ", "");

                Date fechaUpdate = new Date();

                AcAiml aimlUpdate = acAimlService.obtenerPorId(id).get();

                //Obtiene el nombre del archivo en base de datos
                String nameAiml = aimlUpdate.getAiml();
                //Elimina el archivo anterior de la carpeta
                acAimlService.eliminarFileAiml(nameAiml);

                //Guarda el archivo en la carpeta /aiml
                acAimlService.saveFile(file);

                aimlUpdate.setAiml(archivo);
                aimlUpdate.setUpdatedAt(fechaUpdate);
                acAimlService.save(aimlUpdate);

                return new ResponseEntity(new Mensaje("Archivo Aiml actualizado exitosamente"), HttpStatus.CREATED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo ("+tipoFormato+") no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    //Eliminar archivo aiml
    @DeleteMapping("/delete-aiml/{id}")
    @ApiOperation(value = "Eliminar aiml", notes = "Eliminar aiml")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!acAimlService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el aiml con ese id"), HttpStatus.NOT_FOUND);

        AcAiml aimlDelete = acAimlService.obtenerPorId(id).get();
        String nameAiml = aimlDelete.getAiml();
        acAimlService.eliminarFileAiml(nameAiml);

        acAimlService.borrar(id);
        return new ResponseEntity(new Mensaje("Archivo aiml eliminado exitosamente"), HttpStatus.OK);
    }

    @GetMapping("/filename/{nameAiml:.+}")
    @ApiOperation(value = "Obtener ruta del archivo aiml", notes = "Obtener la ruta del archivo aiml para trabajarlo en el front")
    public ResponseEntity<Resource> verArchivoAiml(@PathVariable String nameAiml){

        Resource recurso = null;

        try {
            recurso = acAimlService.cargar(nameAiml);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return new ResponseEntity(recurso, HttpStatus.OK);
    }

}
