package com.soaint.service;

import com.soaint.entity.Rol;
import com.soaint.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    //Obtiene todos los roles de la base de datos Ascendente por tabla rol_name
    public List<Rol> getAll(){
        List<Rol> lista = rolRepository.findAll(Sort.by(Sort.Direction.ASC, "rolName"));
        return lista;
    }

    //Verificar si existe ese ID que se consulta.
    public Optional<Rol> getById(int id){
        return rolRepository.findById(id);
    }

    //Comprobar si existe un rol determinado.
    public boolean existById(int id){
        return rolRepository.existsById(id);
    }

    public Optional<Rol> getByRolName(String rolName){
        return rolRepository.findByRolName(rolName);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByRolName(String rolName){
        return rolRepository.existsByRolName(rolName);
    }

    //Crear rol
    public void save(Rol rol){
        rolRepository.save(rol);
    }

    //Eliminar rol
    public void delete(int id){
        rolRepository.deleteById(id);
    }

    //Cuenta todos los registros del Rol
    public long contadorRol(){
        long lista =  rolRepository.count();
        return lista;
    }

}
