package com.soaint.service;

import com.soaint.entity.State;
import com.soaint.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class StateService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    StateRepository stateRepository;

    public void guardarState(State state){
        stateRepository.save(state);
    }

    public Optional<State> statePorId(Long id){
        return stateRepository.findById(id);
    }

    public boolean stateId(Long id) {
        return stateRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<State> getByName(String name){
        return stateRepository.findByName(name);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByName(String name){
        return stateRepository.existsByName(name);
    }

    public void borrarState(Long id){
        stateRepository.deleteById(id);
    }

    //Cuenta todos los registros de List State
    public long contadorState(){
        long lista =  stateRepository.count();
        return lista;
    }

}
