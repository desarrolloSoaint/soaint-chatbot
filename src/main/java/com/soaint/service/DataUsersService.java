package com.soaint.service;

import com.soaint.entity.AcDataUsers;
import com.soaint.repository.AcDataUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

//Realizará operaciones contra una base de datos, @Service, @Transactional
@Service
@Transactional
public class DataUsersService {

    //@Autowired para instanciar una interfaz tipo UserRepository en el caso de que se necesite.
    @Autowired
    AcDataUsersRepository acDataUsersRepository;

    private String upload_folder = ".//uploads//users//";
    private final static String DIRECTORIO_UPLOAD = "uploads/users";

    public Resource cargar(String nombreFoto) throws MalformedURLException {

        Path rutaArchivo = getPath(nombreFoto);
        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if(!recurso.exists() && !recurso.isReadable()){
            rutaArchivo = Paths.get("src/main/resources/images/avatar").resolve("user.png").toAbsolutePath();
            recurso = new UrlResource(rutaArchivo.toUri());
        }

        return recurso;
    }

    //Guardar archivo
    public void saveFile(MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            //Obtener los bytes del archivo
            byte[] bytes = file.getBytes();
            //Obtener la ruta del archivo
            Path path = Paths.get(upload_folder + file.getOriginalFilename());
            //Guarda el archivo en la carpeta
            Files.write(path,bytes);
        }
    }

    public Path getPath(String nombreFoto){
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
    }

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarFileImgUser(String nameImgUser) {

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

    public List<AcDataUsers> obtenerDataUsers(){
        List<AcDataUsers> data = acDataUsersRepository.findAll();
        return data;
    }

    public Optional<AcDataUsers> dataPorId(Integer id){
        return acDataUsersRepository.findById(id);
    }

    public boolean dataId(Integer id) {
        return acDataUsersRepository.existsById(id);
    }

    public void guardarData(AcDataUsers acDataUsers){
        acDataUsersRepository.save(acDataUsers);
    }

    public void crearData(AcDataUsers acDataUsers){
        acDataUsersRepository.save(acDataUsers);
    }

    public void borrarData(Integer id){
        acDataUsersRepository.deleteById(id);
    }

}
