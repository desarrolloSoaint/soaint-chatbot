package com.soaint.service;

import com.soaint.entity.AcClients;
import com.soaint.entity.AcQuiz;
import com.soaint.repository.AcQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcQuizService {

    @Autowired
    AcQuizRepository acQuizRepository;

    //Guarda la encuesta.
    public void saveQuiz(AcQuiz acQuiz){ acQuizRepository.save(acQuiz);}

    //Obtiene todas las encuestas de satisfaccion
    public List<AcQuiz> obtenerTodos(){ List<AcQuiz> lista = acQuizRepository.findAll();
        return lista;}

    //Verificar si existe ese ID que se consulta.
    public Optional<AcQuiz> obtenerPorId(Integer id){
        return acQuizRepository.findById(id);
    }
}
