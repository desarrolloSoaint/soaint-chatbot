package com.soaint.service;

import com.soaint.entity.AcMenu;
import com.soaint.repository.AcMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class MenuService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    AcMenuRepository acMenuRepository;

    public List<AcMenu> obtenerDataMenu(){
        List<AcMenu> dataMenu = acMenuRepository.findAll();
        return dataMenu;
    }

    public Optional<AcMenu> menuPorId(Long id){
        return acMenuRepository.findById(id);
    }

    public boolean dataMenu(Long id) {
        return acMenuRepository.existsById(id);
    }

    public void guardarMenu(AcMenu acMenu){
        acMenuRepository.save(acMenu);
    }

    public void borrarMenu(Long id){
        acMenuRepository.deleteById(id);
    }

}
