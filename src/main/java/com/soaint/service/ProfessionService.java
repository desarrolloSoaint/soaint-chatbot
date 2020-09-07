package com.soaint.service;

import com.soaint.entity.Profession;
import com.soaint.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class ProfessionService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    ProfessionRepository professionRepository;

    public void guardarProfession(Profession profession){
        professionRepository.save(profession);
    }

    public Optional<Profession> professionPorId(Long id){
        return professionRepository.findById(id);
    }

    public boolean professionId(Long id) {
        return professionRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<Profession> getByName(String name){
        return professionRepository.findByName(name);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByName(String name){
        return professionRepository.existsByName(name);
    }

    public void borrarProfesion(Long id){
        professionRepository.deleteById(id);
    }

    //Cuenta todos los registros de List Currency
    public long contadorProfession(){
        long lista =  professionRepository.count();
        return lista;
    }

}
