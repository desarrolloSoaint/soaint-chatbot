package com.soaint.service;

import com.soaint.entity.AcAiml;
import com.soaint.entity.AcDataUsers;
import com.soaint.entity.Users;
import com.soaint.repository.AcDataUsersRepository;
import com.soaint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AcDataUsersRepository acDataUsersRepository;

    @Autowired
    DataUsersService dataUsersService;

    public List<Users> obtenerTodos(){
        List<Users> lista = userRepository.findAll();
        return lista;
    }

    //Verificar si existe ese ID que se consulta.
    public Optional<Users> obtenerPorId(int id){
        return userRepository.findById(id);
    }

    //Comprobar si existe o no un nombre determinado.
    public boolean existePorId(int id){
        return userRepository.existsById(id);
    }

    //Comprobar si existe o no un email determinado.
    public Optional<Users> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    //registra
    public void save(Users users){
        userRepository.save(users);
    }

    //Elimina
    public void borrar(int id){

        long registros = acDataUsersRepository.count();
        List<AcDataUsers> DataUsers = acDataUsersRepository.findAll();

        for (int i = 0; i < registros; i++){
            if (DataUsers.get(i).getId_user().getId() == id){

                //Se obtiene el id del dataUser que corresponde al usuario
                AcDataUsers idImgUser = DataUsers.get(i);
                //Obtiene el campo de la imagen del dataUser que corresponde al usuario
                String nameImgUser = idImgUser.getImage_user();
                //Elimina la imagen del usuario
                eliminarImgUser(nameImgUser);

                //Elimina la data del usuario en la tabla (DATAUSER)
                acDataUsersRepository.deleteById(DataUsers.get(i).getId());
            }
        }

        userRepository.deleteById(id);
    }

    //Cuenta todos los registros de user
    public long contadorUser(){
        long lista =  userRepository.count();
        return lista;
    }

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarImgUser(String nameImgUser) {

        if (nameImgUser != null && nameImgUser.length() > 0) {
            Path rutaImgUserAnterior = Paths.get("uploads/users").resolve(nameImgUser).toAbsolutePath();
            File archivoImgUserAnterior = rutaImgUserAnterior.toFile();
            if (archivoImgUserAnterior.exists() && archivoImgUserAnterior.canRead()) {
                archivoImgUserAnterior.delete();
                return true;
            }
        }
        return false;
    }

    //Cuenta Registro Users Ultima Semana
    public long UserCountLastWeek(){
        Long semanal= userRepository.UsersCountLastWeek();
        return semanal;
    }

    //Cuenta Registro Users Ultimo Dia
    public long UserCountLastDay(){
        Long dia= userRepository.UsersCountLastDay();
        return dia;
    }
}
