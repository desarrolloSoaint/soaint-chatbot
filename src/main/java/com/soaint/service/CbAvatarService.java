package com.soaint.service;

import com.soaint.entity.CbAvatar;
import com.soaint.repository.CbAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
@Transactional
public class CbAvatarService {

    @Autowired
    CbAvatarRepository cbAvatarRepository;

    private String upload_folder = ".//uploads//avatar//";
    private final static String DIRECTORIO_UPLOAD = "uploads/avatar";

    public Resource cargar(String nombreFoto) throws MalformedURLException{

        Path rutaArchivo = getPath(nombreFoto);
        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if(!recurso.exists() && !recurso.isReadable()){
            rutaArchivo = Paths.get("src/main/resources/images/avatar").resolve("SONIAT.png").toAbsolutePath();
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

    public void guardarAvatar(CbAvatar cbAvatar){
        cbAvatarRepository.save(cbAvatar);
    }

    public Optional<CbAvatar> cbAvatarPorId(Long id){
        return cbAvatarRepository.findById(id);
    }

    public boolean cbAvatarId(Long id) {
        return cbAvatarRepository.existsById(id);
    }

    public void borrarAvatar(Long id){
        cbAvatarRepository.deleteById(id);
    }

    //Comprobar si existe o no un avatar determinado.
    public Optional<CbAvatar> getByAvatar(String avatar){
        return cbAvatarRepository.findByAvatar(avatar);
    }

    //Booleano que dara respuesta al controller
    public boolean existsByAvatar(String avatar){
        return cbAvatarRepository.existsByAvatar(avatar);
    }

    //Cuenta todos los registros de LOOK AND FEEL Avatar
    public long contadorAvatar(){
        long lista =  cbAvatarRepository.count();
        return lista;
    }

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarFileAvatar(String nameAvatar) {

        if (nameAvatar != null && nameAvatar.length() > 0) {
            Path rutaAvatarAnterior = Paths.get("uploads/avatar").resolve(nameAvatar).toAbsolutePath();
            File archivoAvatarAnterior = rutaAvatarAnterior.toFile();
            if (archivoAvatarAnterior.exists() && archivoAvatarAnterior.canRead()) {
                archivoAvatarAnterior.delete();
                return true;
            }
        }
        return false;
    }
}
