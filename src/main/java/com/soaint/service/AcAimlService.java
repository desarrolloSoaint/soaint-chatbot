package com.soaint.service;

import com.soaint.entity.AcAiml;
import com.soaint.entity.AcDataUsers;
import com.soaint.entity.Users;
import com.soaint.repository.AcAimlRepository;
import com.soaint.repository.AcDataUsersRepository;
import com.soaint.repository.UserRepository;
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

@Service
@Transactional
public class AcAimlService {

    @Autowired
    AcAimlRepository acAimlRepository;

    private String upload_folder = ".//src//main//resources//bots//super//aiml//";
    private final static String DIRECTORIO_UPLOAD = "src/main/resources/bots/super/aiml";

    //lista todos los datos
    public List<AcAiml> obtenerTodos(){
        List<AcAiml> lista = acAimlRepository.findAll();
        return lista;
    }

    //Verificar si existe ese ID que se consulta.
    public Optional<AcAiml> obtenerPorId(long id){
        return acAimlRepository.findById(id);
    }

    //Comprobar si existe o no un nombre determinado.
    public boolean existePorId(long id){
        return acAimlRepository.existsById(id);
    }

    //Comprobar si existe o no un aiml.
    public Optional<AcAiml> getByAiml(String aiml){
        return acAimlRepository.findByAiml(aiml);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByAiml(String aiml){
        return acAimlRepository.existsByAiml(aiml);
    }

    //registra
    public void save(AcAiml acAiml){
        acAimlRepository.save(acAiml);
    }

    //Elimina
    public void borrar(long id){
        acAimlRepository.deleteById(id);

    }

    //Cuenta todos los registros de archivos aiml
    public long contadorAiml(){
        long lista =  acAimlRepository.count();
        return lista;
    }

    public Resource cargar(String nameAiml) throws MalformedURLException {

        Path rutaArchivo = getPath(nameAiml);
        Resource recurso = new UrlResource(rutaArchivo.toUri());

        /*if(!recurso.exists() && !recurso.isReadable()){

            rutaArchivo = Paths.get("src/main/resources/images/avatar").resolve("SONIAT.png").toAbsolutePath();
            recurso = new UrlResource(rutaArchivo.toUri());
        }*/

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

    public Path getPath(String nameAiml){
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nameAiml).toAbsolutePath();
    }

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarFileAiml(String nameAiml) {

        if (nameAiml != null && nameAiml.length() > 0) {
            Path rutaAimlAnterior = Paths.get("src/main/resources/bots/super/aiml").resolve(nameAiml).toAbsolutePath();
            File archivoAimlAnterior = rutaAimlAnterior.toFile();
            if (archivoAimlAnterior.exists() && archivoAimlAnterior.canRead()) {
                archivoAimlAnterior.delete();
                return true;
            }
        }
        return false;
    }


}
