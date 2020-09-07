package com.soaint.controller;

import com.soaint.DTO.*;
import com.soaint.entity.*;
import com.soaint.repository.CbColorRepository;
import com.soaint.service.CbColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/color")
@Api(tags = "LOOK AND FEEL")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CbColorController {

    @Autowired
    private CbColorRepository cbColorRepository;

    @Autowired
    private CbColorService cbColorService;

    //Get a toda la data de los Colores
    @GetMapping("/find-all")
    @ApiOperation(value = "Devuelve toda la Data de los Colores", notes = "Devuelve toda la data de los Colores del sistema")
    public List<CbColor> findAll(){

        return cbColorService.obtenerColor();
    }

    //Get por Id la data de los Colores
    @GetMapping("/find-all/{id}")
    @ApiOperation(value = "Buscar  Registro de un Color por Id", notes = "Devuelve los datos relativos de un Color")
    public ResponseEntity<CbColor> getOne(@PathVariable Long id){
        if(!cbColorService.colorId(id))
            return new ResponseEntity(new Mensaje("No existe ese Color"), HttpStatus.NOT_FOUND);
        CbColor cbColor = cbColorService.colorPorId(id).get();
        return new ResponseEntity<CbColor>(cbColor, HttpStatus.OK);
    }

    //Get Cantidad Colores
    @GetMapping("/count-colors")
    @ApiOperation(value = "Devuelve Cantidad de Colores", notes = "Cuenta la cantidad de Colores Registrados")
    public long countAll() {
        return cbColorService.contadorColor();
    }

    //Save Colores
    @PostMapping("/create-color")
    @ApiOperation(value = "Crear Nuevo Color", notes = "Crear Color")
    public ResponseEntity<?> create(@Valid @RequestBody CbColorDto cbColorDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos erroneos"), HttpStatus.BAD_REQUEST);
        CbColor cbColor = new CbColor(
                cbColorDto.getColor());
        cbColorService.crearColor(cbColor);
        return new ResponseEntity(new Mensaje("Color creado exitosamente"), HttpStatus.CREATED);
    }

    //Update Colores
    @PutMapping("/update-color/{id}")
    @ApiOperation(value = "Actualiza Colores por Id", notes = "Actualiza los colores que se corresponda con el id.")
    public ResponseEntity<?> update(@Valid @RequestBody CbColorDto cbColorDto, @PathVariable("id") Long id, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos errados"), HttpStatus.BAD_REQUEST);
        if(!cbColorService.colorId(id))
            return new ResponseEntity(new Mensaje("No existe el Color"), HttpStatus.NOT_FOUND);
        CbColor cbColorUpdate = cbColorService.colorPorId(id).get();
        cbColorUpdate.setColor(cbColorDto.getColor());
        cbColorService.guardarColor(cbColorUpdate);
        return new ResponseEntity(new Mensaje("Color actualizado"), HttpStatus.CREATED);
    }

    //Delete Color
    @DeleteMapping("/delete-color/{id}")
    @ApiOperation(value = "Elimina Color por Id", notes = "Elimina Color que corresponda con el id.")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!cbColorService.colorId(id))
            return new ResponseEntity(new Mensaje("No existe ese Color"), HttpStatus.NOT_FOUND);
        cbColorService.borrarColor(id);
        return new ResponseEntity(new Mensaje("Color eliminado"), HttpStatus.OK);
    }
}
