package com.soaint.service;


import com.soaint.entity.*;
import com.soaint.repository.AcClientsPrivateRepository;
import com.soaint.repository.AcClientsRepository;
import com.soaint.repository.AcDataClientsPrivateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {

    @Autowired
    AcClientsRepository acClientsRepository;
    @Autowired
    AcClientsPrivateRepository acClientsPrivateRepository;
    @Autowired
    AcDataClientsPrivateRepository acDataClientsPrivateRepository;



    //SERVICIOS PARA LA TABLA AC_CLIENTSB (PUBLICOS)

    //Obtiene todos los clientes publicos
    public List<AcClients> obtenerTodos(){ List<AcClients> lista = acClientsRepository.findAll();
        return lista;
    }
    //Verificar si existe ese ID que se consulta.
    public Optional<AcClients> obtenerPorId(Integer id){
        return acClientsRepository.findById(id);
    }

    //Comprobar si existe o no un nombre determinado.
    public boolean existePorId(Integer id){
        return acClientsRepository.existsById(id);
    }

    //Booleano que dara respuesta al controller
    public boolean existePorEmail(String email){
        return acClientsRepository.existsByEmail(email);
    }

    //Email
    public Optional<AcClients> getByEmail(String email){
        return acClientsRepository.findByEmail(email);
    }

    //Guarda el nuevo cliente a registrar.
    public void guardar(AcClients acClients){ acClientsRepository.save(acClients);}

    //Elimina
    public void borrar(Integer id){
        acClientsRepository.deleteById(id);
    }

    //LISTA DE CORREOS DE CLIENTES PUBLICOS DIFERENTES
    public List<AcClients> ClientsPublic(){
        List<AcClients> lista = acClientsRepository.ClientsPublic();return lista; }

    //CANTIDAD DE REGISTROS DE CLIENTES PUBLICOS
    public Long ClientsPublicCount(){
        Long cantidad = acClientsRepository.ClientsPublicCount();return cantidad; }

    //Cuenta los registros de la ultima semana
    public Long ClientePublicCountLastWeek(){
        Long cantidadSemanal =  acClientsRepository.ClientsPublicCountLastWeek();
        return cantidadSemanal;
    }

    //Cuenta la cantidad de registros en el ultimo dia
    public Long ClientePublicCountLastDay(){
        Long cantidadDay =  acClientsRepository.ClientsPublicCountLastDay();
        return cantidadDay;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //SERVICIOS PARA CLIENTES PRIVADOS

    //Obtiene todos los clientes publicos
    public List<AcClientsPrivate> obtenerTodosPrivados(){ List<AcClientsPrivate> registros = acClientsPrivateRepository.findAll();
        return registros;
    }

    //Comprobar si existe o no un nombre determinado.
    public boolean existePorIdPrivate(Integer id){ return acClientsPrivateRepository.existsById(id); }

    //Verificar si existe ese ID que se consulta.
    public Optional<AcClientsPrivate> obtenerPorIdPrivate(Integer id){
        return acClientsPrivateRepository.findById(id);
    }

    //Booleano que dara respuesta al controller
    public boolean existePorEmailPrivado(String email){
        return acClientsPrivateRepository.existsByEmail(email);
    }

    //Email
    public Optional<AcClientsPrivate> getByEmailPrivado(String email){ return acClientsPrivateRepository.findByEmail(email); }

    //Registra nuevo cliente privado
    public void savePrivado(AcClientsPrivate acClientsPrivate){
        acClientsPrivateRepository.save(acClientsPrivate);
    }
    //Elimina cliente privado
    public void borrarPrivado(Integer id){

        long registros = acDataClientsPrivateRepository.count();
        List<AcDataClientsPrivate> Data = acDataClientsPrivateRepository.findAll();

        for (int i = 0; i < registros; i++){
            if (Data.get(i).getId_client_private().getId() == id){
                acDataClientsPrivateRepository.deleteById(Data.get(i).getId());
            }
        }
        acClientsPrivateRepository.deleteById(id);
    }

    //LISTA DE CORREOS DE CLIENTES PRIVADOS DIFERENTES
    public List<AcClientsPrivate> ClientsPrivate(){
        List<AcClientsPrivate> lista = acClientsPrivateRepository.ClientsPrivate();
        return lista; }

    //CANTIDAD DE REGISTROS DE CLIENTES PRIVADOS
    public Long ClientsPrivateCount(){
        Long cantidad = acClientsPrivateRepository.ClientsPrivateCount();return cantidad; }


    //Cuenta los registros de la ultima semana
    public Long ClientePrivadoCountLastWeek(){
        Long cantidadSemanal =  acClientsPrivateRepository.ClientsPrivateCountLastWeek();
        return cantidadSemanal;
    }

    //Cuenta la cantidad de registros en el ultimo dia
    public Long ClientePrivadoCountLastDay(){
        Long cantidadDay =  acClientsPrivateRepository.ClientsPrivateCountLastDay();
        return cantidadDay;
    }


}
