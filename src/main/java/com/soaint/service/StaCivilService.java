package com.soaint.service;

import com.soaint.entity.StaCivil;
import com.soaint.repository.StaCivilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class StaCivilService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    StaCivilRepository staCivilRepository;

    public void guardarStaCivil(StaCivil staCivil){
        staCivilRepository.save(staCivil);
    }

    public Optional<StaCivil> staCivilPorId(Long id){
        return staCivilRepository.findById(id);
    }

    public boolean staCivilId(Long id) {
        return staCivilRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<StaCivil> getByName(String name){
        return staCivilRepository.findByName(name);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByName(String name){
        return staCivilRepository.existsByName(name);
    }

    public void borrarStaCivil(Long id){
        staCivilRepository.deleteById(id);
    }

    //Cuenta todos los registros de List StateCivil
    public long contadorStaCivil(){
        long lista =  staCivilRepository.count();
        return lista;
    }

}
