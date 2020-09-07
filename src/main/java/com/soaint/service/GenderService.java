package com.soaint.service;

import com.soaint.entity.Gender;
import com.soaint.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class GenderService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    GenderRepository genderRepository;

    public void guardarGender(Gender gender){
        genderRepository.save(gender);
    }

    public Optional<Gender> genderPorId(Long id){
        return genderRepository.findById(id);
    }

    public boolean genderId(Long id) {
        return genderRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<Gender> getByName(String name){
        return genderRepository.findByName(name);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByName(String name){
        return genderRepository.existsByName(name);
    }

    public void borrarGender(Long id){
        genderRepository.deleteById(id);
    }

    //Cuenta todos los registros de List Gender
    public long contadorGender(){
        long lista =  genderRepository.count();
        return lista;
    }

}
