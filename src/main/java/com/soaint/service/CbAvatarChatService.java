package com.soaint.service;

import com.soaint.entity.AcMenu;
import com.soaint.entity.CbAvatar;
import com.soaint.entity.CbAvatarChat;
import com.soaint.repository.CbAvatarChatRepository;
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
public class CbAvatarChatService {

    @Autowired
    CbAvatarChatRepository cbAvatarChatRepository;

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

    //Eliminar archivo con el nombre por parametro
    public boolean eliminarFileChatAvatar(String nameChatAvatar) {

        if (nameChatAvatar != null && nameChatAvatar.length() > 0) {
            Path rutaChatAvatarAnterior = Paths.get("uploads/avatar").resolve(nameChatAvatar).toAbsolutePath();
            File archivoChatAvatarAnterior = rutaChatAvatarAnterior.toFile();
            if (archivoChatAvatarAnterior.exists() && archivoChatAvatarAnterior.canRead()) {
                archivoChatAvatarAnterior.delete();
                return true;
            }
        }
        return false;
    }

    public void guardarAvatar(CbAvatarChat cbAvatarChat){
        cbAvatarChatRepository.save(cbAvatarChat);
    }

    public Optional<CbAvatarChat> cbAvatarChatPorId(Long id){
        return cbAvatarChatRepository.findById(id);
    }

    public boolean cbAvatarChatId(Long id) {
        return cbAvatarChatRepository.existsById(id);
    }

    public void borrarAvatarChat(Long id){
        cbAvatarChatRepository.deleteById(id);
    }

    //Cuenta todos los registros de LOOK AND FEEL Avatar
    public long contadorAvatar(){
        long lista =  cbAvatarChatRepository.count();
        return lista;
    }

    public boolean dataChat(Long id) {
        return cbAvatarChatRepository.existsById(id);
    }

    public Optional<CbAvatarChat> chatPorId(Long id){
        return cbAvatarChatRepository.findById(id);
    }

    public void guardarChat(CbAvatarChat cbAvatarChat){
        cbAvatarChatRepository.save(cbAvatarChat);
    }

}
