package com.soaint.service;

import com.soaint.entity.AcDataUsers;
import com.soaint.entity.CbColor;
import com.soaint.entity.Country;
import com.soaint.repository.AcDataUsersRepository;
import com.soaint.repository.CbColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CbColorService {

    @Autowired
    CbColorRepository cbColorRepository;

    public List<CbColor> obtenerColor(){
        List<CbColor> data = cbColorRepository.findAll(Sort.by(Sort.Direction.ASC, "color"));
        return data;
    }

    public Optional<CbColor> colorPorId(Long id){
        return cbColorRepository.findById(id);
    }

    public boolean colorId(Long id) {
        return cbColorRepository.existsById(id);
    }

    public void guardarColor(CbColor cbColor){
        cbColorRepository.save(cbColor);
    }

    public void crearColor(CbColor cbColor){
        cbColorRepository.save(cbColor);
    }

    public void borrarColor(Long id){
        cbColorRepository.deleteById(id);
    }

    //Cuenta todos los registros de Colores
    public long contadorColor(){
        long lista =  cbColorRepository.count();
        return lista;
    }
}
