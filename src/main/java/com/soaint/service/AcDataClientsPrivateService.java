package com.soaint.service;

import com.soaint.entity.AcDataClientsPrivate;
import com.soaint.repository.AcDataClientsPrivateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AcDataClientsPrivateService {

    @Autowired
    AcDataClientsPrivateRepository acDataClientsPrivateRepository;


    //DEVUELVE LOS DATOS DE LOS CLIENTES PRIVADOS
    public List<AcDataClientsPrivate> obtenerDataClientsPrivate(){
        List<AcDataClientsPrivate> data = acDataClientsPrivateRepository.findAll();
        return data;
    }

    //consulta si existe el id en la base de datos
    public boolean dataId(Integer id) {
        return acDataClientsPrivateRepository.existsById(id);
    }

    public Optional<AcDataClientsPrivate> dataPorId(Integer id){
        return acDataClientsPrivateRepository.findById(id);
    }

    //CREA UN REGISTRO NUEVO DE LOS DATOS DE UN CLIENTE PRIVADO
    public void saveData(AcDataClientsPrivate acDataClientsPrivate){
        acDataClientsPrivateRepository.save(acDataClientsPrivate);
    }

    //ELIMINA LOS DATOS DE UN CLIENTE PRIVADO
    public void borrarData(Integer id){
        acDataClientsPrivateRepository.deleteById(id);
    }

}
