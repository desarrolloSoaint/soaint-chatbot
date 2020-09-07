package com.soaint.controller;

import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcQuiz;
import com.soaint.service.AcQuizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
@Api(tags = "QUIZ")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class AcQuizController {


    @Autowired
    AcQuizService acQuizService;

    //REGISTRO DE ENCUESTA
    @PostMapping("/send")
    @ApiOperation(value = "Registro de respuestas del quiz para clientes", notes = "Registro de respuestas del quiz")
    public ResponseEntity<?> saveQuiz(@Valid @RequestBody AcQuiz acQuiz, BindingResult bindingResult){
        acQuizService.saveQuiz(acQuiz);
        return new ResponseEntity(new Mensaje("Encuesta registrada."), HttpStatus.CREATED);
    }

    //Get todos los Registros de la encuesta realizada
    @GetMapping("/find-all")
    @ApiOperation(value = "Data de Registro de respuestas del quiz para clientes", notes = "Todos las encuestas")
    public List<AcQuiz> findAll(){ return acQuizService.obtenerTodos(); }

    //Get un Registro de la encuesta realizada
    @GetMapping("/find/{id}")
    @ApiOperation(value = "Busca Registro de respuestas del quiz por Id", notes = "Devuelve el registro de quiz por ID")
    public ResponseEntity<AcQuiz> getOne(@PathVariable Integer id){
        AcQuiz quiz = acQuizService.obtenerPorId(id).get();
        return new ResponseEntity<AcQuiz>(quiz, HttpStatus.OK);
    }

}
