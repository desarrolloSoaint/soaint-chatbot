package com.soaint.service;

import com.soaint.entity.AcAimlIf;
import com.soaint.repository.AcAimlIfRepository;
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
public class AcAimlIfService {

    @Autowired
    AcAimlIfRepository acAimlIfRepository;

    private String upload_folder = ".//src//main//resources//bots//super//aimlif//";
    private final static String DIRECTORIO_UPLOAD = "src/main/resources/bots/super/aimlif";

    //lista todos los datos
    public List<AcAimlIf> obtenerTodos(){
        List<AcAimlIf> lista = acAimlIfRepository.findAll();
        return lista;
    }

    //Verificar si existe ese ID que se consulta.
    public Optional<AcAimlIf> obtenerPorId(long id){
        return acAimlIfRepository.findById(id);
    }

    //Comprobar si existe o no un nombre determinado.
    public boolean existePorId(long id){
        return acAimlIfRepository.existsById(id);
    }

    //Comprobar si existe o no un aiml.
    public Optional<AcAimlIf> getByAimlIf(String aimlIf){
        return acAimlIfRepository.findByAimlIf(aimlIf);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByAimlIf(String aimlIf){
        return acAimlIfRepository.existsByAimlIf(aimlIf);
    }

    //registra
    public void save(AcAimlIf acAimlIf){
        acAimlIfRepository.save(acAimlIf);
    }

    //Elimina
    public void borrar(long id){
        acAimlIfRepository.deleteById(id);

    }

    //Cuenta todos los registros de archivos aiml
    public long contadorAimlIf(){
        long lista =  acAimlIfRepository.count();
        return lista;
    }

    //Obtener la ruta del archivo para mostrarlo en el front
    public Resource cargar(String nameAimlIf) throws MalformedURLException {

        Path rutaArchivo = getPath(nameAimlIf);
        Resource recurso = new UrlResource(rutaArchivo.toUri());

        /*if(!recurso.exists() && !recurso.isReadable()){

            rutaArchivo = Paths.get("src/main/resources/images/avatar").resolve("SONIAT.png").toAbsolutePath();
            recurso = new UrlResource(rutaArchivo.toUri());
        }*/

        return recurso;
    }

    //Guardar archivo
    public void saveFile(MultipartFile file) throws IOException {

        if(!file.isEmpty()) {
                //Obtener los bytes del archivo
                byte[] bytes = file.getBytes();
                //Obtener la ruta del archivo
                Path path = Paths.get(upload_folder + file.getOriginalFilename());
                //Guarda el archivo en la carpeta
                Files.write(path, bytes);
        }
    }

    public Path getPath(String nameAimlIf){
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nameAimlIf).toAbsolutePath();
    }

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarFileAimlIf(String nameAimlIf) {

        if (nameAimlIf != null && nameAimlIf.length() > 0) {
            Path rutaAimlIfAnterior = Paths.get("src/main/resources/bots/super/aimlif").resolve(nameAimlIf).toAbsolutePath();
            File archivoAimlIfAnterior = rutaAimlIfAnterior.toFile();
            if (archivoAimlIfAnterior.exists() && archivoAimlIfAnterior.canRead()) {
                archivoAimlIfAnterior.delete();
                return true;
            }
        }
        return false;
    }


}
