package com.soaint.controller;

import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcAimlIf;
import com.soaint.repository.AcAimlIfRepository;
import com.soaint.service.AcAimlIfService;
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
@RequestMapping("/api/v1/fileaimlif")
@Api(tags = "CHATBOT")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AcAimlIfController {

    //Formato de archivos validos (aiml.csv)
    private static final List<String> formatoValido = Arrays.asList("application/vnd.ms-excel");

    @Autowired
    private AcAimlIfRepository acAimlIfRepository;

    @Autowired
    private AcAimlIfService acAimlIfService;

    //obtener lista de AimlIf
    @GetMapping("/find-all")
    @ApiOperation(value = "Buscar todos los registros AimlIf", notes = "Lista de registros AimlIf")
    public List<AcAimlIf> findAll(){
        return acAimlIfService.obtenerTodos();
    }

    //Obtener lista de AimlIf de forma ascendente
    @GetMapping("/find-all-order")
    @ApiOperation(value = "Buscar AimlIf ascendentes por nombres", notes = "Lista de aimlIf por nombre ascendentes")
    public List<AcAimlIf> findAllOrder(){
        return acAimlIfRepository.findAll(Sort.by(Sort.Direction.ASC, "aimlIf"));
    }

    //Obtener AimlIf by id
    @GetMapping("/find-aimlif/{id}")
    @ApiOperation(value = "Buscar aimlIf por id", notes = "Buscar aimlIf por id")
    public ResponseEntity<AcAimlIf> getOne(@PathVariable Long id){
        if(!acAimlIfService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe ese aiml"), HttpStatus.NOT_FOUND);
        AcAimlIf acAimlIf = acAimlIfService.obtenerPorId(id).get();
        return new ResponseEntity<AcAimlIf>(acAimlIf, HttpStatus.OK);
    }

    //Obtener cantidad de archivos aiml
    @GetMapping("/count-aimlif")
    @ApiOperation(value = "Cantidad de registro de aimlIf", notes = "Cuenta la cantidad de registro de archivos aimlIf")
    public long countAll(){
        return acAimlIfService.contadorAimlIf();
    }

    //Guardar archivo aimlif
    @PostMapping("/create-aimlif")
    @ApiOperation(value = "Crear archivo aimlif", notes = "Crear archivo aimlif" )
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) throws IOException {

        if(acAimlIfService.existsByAimlIf(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El archivo aimlIf ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();
            try {
                //Valida en un array los tipos de formatos permitidos
                if(formatoValido.contains(tipoFormato)) {

                    //Guarda el archivo en la carpeta de destino /aiml
                    acAimlIfService.saveFile(file);

                    //Guarda la ruta en la base de datos
                    String archivo = file.getOriginalFilename();
                    archivo = archivo.replace(" ", "");

                    Date fechaCreate = new Date();

                    AcAimlIf acAimlIf = new AcAimlIf();
                    acAimlIf.setAimlIf(archivo);
                    acAimlIf.setCreatedAt(fechaCreate);
                    acAimlIfService.save(acAimlIf);

                    return new ResponseEntity(new Mensaje("Archivo aimlIf registrado exitosamente"), HttpStatus.OK);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity(new Mensaje("Extensión de archivo ("+tipoFormato+") no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    // Actualizar archivo aiml
    @PutMapping("/update-aimlif/{id}")
    @ApiOperation(value = "Actualizar archivo aimlIf", notes = "Actualizar archivo aimlIf")
    public ResponseEntity<?> update(@PathVariable("id") Long id, MultipartFile file) throws IOException {
        if(!acAimlIfService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el aimlIf con ese id"), HttpStatus.NOT_FOUND);
        if(acAimlIfService.existsByAimlIf(file.getOriginalFilename()))
            return new ResponseEntity(new Mensaje("El archivo aimlIf ya existe con ese nombre."), HttpStatus.BAD_REQUEST);
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

                Date fechaUpdate = new Date();

                AcAimlIf aimlIfUpdate = acAimlIfService.obtenerPorId(id).get();

                //Obtiene el nombre del archivo en base de datos
                String nameAiml = aimlIfUpdate.getAimlIf();
                //Elimina el archivo anterior de la carpeta
                acAimlIfService.eliminarFileAimlIf(nameAiml);

                //Guarda el archivo en la carpeta /aiml
                acAimlIfService.saveFile(file);

                aimlIfUpdate.setAimlIf(archivo);
                aimlIfUpdate.setUpdatedAt(fechaUpdate);
                acAimlIfService.save(aimlIfUpdate);

                return new ResponseEntity(new Mensaje("Archivo aimlIf actualizado exitosamente"), HttpStatus.CREATED);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo ("+tipoFormato+") no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    //Eliminar archivo aiml
    @DeleteMapping("/delete-aimlif/{id}")
    @ApiOperation(value = "Eliminar aimlif", notes = "Eliminar aimlif")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!acAimlIfService.existePorId(id))
            return new ResponseEntity(new Mensaje("No existe el aimlIf con ese id"), HttpStatus.NOT_FOUND);

        AcAimlIf aimlIfDelete = acAimlIfService.obtenerPorId(id).get();
        String nameAimlIf = aimlIfDelete.getAimlIf();
        acAimlIfService.eliminarFileAimlIf(nameAimlIf);

        acAimlIfService.borrar(id);
        return new ResponseEntity(new Mensaje("Archivo aimlIf eliminado exitosamente"), HttpStatus.OK);
    }

    @GetMapping("/filename/{nameAimlIf:.+}")
    @ApiOperation(value = "Obtener ruta del archivo aimlIf", notes = "Obtener la ruta del archivo aimlIf para trabajarlo en el front")
    public ResponseEntity<Resource> verArchivoAimlIf(@PathVariable String nameAimlIf){

        Resource recurso = null;

        try {
            recurso = acAimlIfService.cargar(nameAimlIf);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return new ResponseEntity(recurso, HttpStatus.OK);
    }

}
