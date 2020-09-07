package com.soaint.service;

import com.soaint.entity.Country;
import com.soaint.entity.State;
import com.soaint.repository.CountryRepository;
import com.soaint.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class CountryService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    CountryRepository countryRepository;

    @Autowired
    StateRepository stateRepository;

    public void guardarCountry(Country country){
        countryRepository.save(country);
    }

    public Optional<Country> countryPorId(Long id){
        return countryRepository.findById(id);
    }

    public boolean countryId(Long id) {
        return countryRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<Country> getByName(String name){
        return countryRepository.findByName(name);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByName(String name){
        return countryRepository.existsByName(name);
    }

    public void borrarCountry(Long id){

        long registros = stateRepository.count();
        List<State> States = stateRepository.findAll();

        for (int i = 0; i < registros; i++){
            if (States.get(i).getCountry_id().getId() == id){
                stateRepository.deleteById(States.get(i).getId());
            }
        }
        countryRepository.deleteById(id);
    }

    public boolean dataCountry(Long id) {
        return countryRepository.existsById(id);
    }

    //Cuenta todos los registros de List Country
    public long contadorCountry(){
        long lista =  countryRepository.count();
        return lista;
    }

}
