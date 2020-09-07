package com.soaint.service;

import com.soaint.entity.Currency;
import com.soaint.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class CurrencyService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    CurrencyRepository currencyRepository;

    public void guardarCurrency(Currency currency){
        currencyRepository.save(currency);
    }

    public Optional<Currency> currencyPorId(Long id){
        return currencyRepository.findById(id);
    }

    public boolean currencyId(Long id) {
        return currencyRepository.existsById(id);
    }

    //Comprobar si existe.
    public Optional<Currency> getByMoney(String money){
        return currencyRepository.findByMoney(money);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByMoney(String money){
        return currencyRepository.existsByMoney(money);
    }

    public void borrarCurrency(Long id){
        currencyRepository.deleteById(id);
    }

    //Cuenta todos los registros de List Currency
    public long contadorCurrency(){
        long lista =  currencyRepository.count();
        return lista;
    }

}
