package com.soaint.controller;

import com.soaint.DTO.Mensaje;
import com.soaint.entity.AcMenu;
import com.soaint.entity.CbAvatar;
import com.soaint.entity.CbAvatarChat;
import com.soaint.entity.Rol;
import com.soaint.repository.CbAvatarChatRepository;
import com.soaint.service.CbAvatarChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/avatar")
@Api(tags = "LOOK AND FEEL")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
public class CbAvatarChatController {

    //Formato de archivos validos (png)
    private static final List<String> formatoValido = Arrays.asList("image/png");

    @Autowired
    private CbAvatarChatRepository cbAvatarChatRepository;

    @Autowired
    private CbAvatarChatService cbAvatarChatService;

    //Get registro Avatar  por la tabla avatar_chat
    @GetMapping("/find-all-chat")
    @ApiOperation(value = "Devuelve Registro Avatar del Chat en la tabla avatar_chat", notes = "AVATAR DEL CHAT")
    public List<CbAvatarChat> findAll(){

        return cbAvatarChatRepository.findAll();
    }

    //Get por Id el registro del Avatar
    @GetMapping("/find-avatar-chat/{id}")
    @ApiOperation(value = "Buscar Registro del Avatar chat por Id", notes = "Buscar Avatar chat")
    public ResponseEntity<CbAvatarChat> getOne(@PathVariable Long id){
        if(!cbAvatarChatService.cbAvatarChatId(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);
        CbAvatarChat cbAvatarChat = cbAvatarChatService.cbAvatarChatPorId(id).get();
        return new ResponseEntity<CbAvatarChat>(cbAvatarChat, HttpStatus.OK);
    }

    //Get Contador cantidad avatar registrados
    @GetMapping("/count-avatar-chat")
    @ApiOperation(value = "Cantidad Registros de Avatar del Chat", notes = "Cuenta la cantidad Avatar del chat")
    public long countAll(){
        return cbAvatarChatService.contadorAvatar();
    }

    //Save Avatar
    @PostMapping("/create-avatar-chat")
    @ApiOperation(value = "Crear Nuevo Avatar Chat", notes = "Crear Avatar Chat" )
    public ResponseEntity<?> create(@RequestParam("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
        }

        //Obtiene el tipo de formato del archivo ingresado
        String tipoFormato = file.getContentType();

        try {
            //Valida en un array los tipos de formatos permitidos
            if(formatoValido.contains(tipoFormato)) {
                //Guarda el archivo en la carpeta /images/avatar
                cbAvatarChatService.saveFile(file);

                //Guarda la ruta en la base de datos
                String archivo = file.getOriginalFilename();
                archivo = archivo.replace(" ", "");

                CbAvatarChat cbAvatarChat = new CbAvatarChat();
                cbAvatarChat.setAvatarChat(archivo);
                cbAvatarChatService.guardarAvatar(cbAvatarChat);

                return new ResponseEntity(new Mensaje("Avatar registrado exitosamente"), HttpStatus.OK);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(new Mensaje("Extensión de archivo no valida, por favor ingrese la correcta."), HttpStatus.NOT_FOUND);

    }

    // update Avatar
    @PutMapping("/update-avatar-chat/{id}")
    @ApiOperation(value = "Actualizar Avatar Chat por Id", notes = "Actualizar Avatar Chat")
    public ResponseEntity<?> update(@RequestBody CbAvatarChat cbAvatarChat, @PathVariable("id") Long id){
        if(!cbAvatarChatService.dataChat(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);
        CbAvatarChat avatarChatUpdate = cbAvatarChatService.chatPorId(id).get();
        avatarChatUpdate.setAvatarChat(cbAvatarChat.getAvatarChat());
        cbAvatarChatService.guardarChat(avatarChatUpdate);
        return new ResponseEntity(new Mensaje("Avatar actualizado"), HttpStatus.CREATED);
    }

//    @PutMapping("/update-avatar-chat/{id}")
//    @ApiOperation(value = "Actualizar Avatar Chat", notes = "Actualizar Avatar Chat")
//    public ResponseEntity<?> update(@PathVariable("id") Long id, MultipartFile file) throws IOException {
//        if(!cbAvatarChatService.cbAvatarChatId(id))
//            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);
//        if (file == null || file.isEmpty()) {
//            return new ResponseEntity(new Mensaje("Por favor seleccione un archivo"), HttpStatus.NOT_FOUND);
//        }
//
//        try {
//            //Guarda el archivo en la carpeta /images/avatar
//            cbAvatarChatService.saveFile(file);
//
//            //Guarda la ruta en la base de datos
//            String archivo = file.getOriginalFilename();
//            archivo =  archivo.replace(" ", "");
//
//            CbAvatarChat avatarUpdate = cbAvatarChatService.cbAvatarChatPorId(id).get();
//
//            avatarUpdate.setAvatarChat(archivo);
//            cbAvatarChatService.guardarAvatar(avatarUpdate);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new ResponseEntity(new Mensaje("Avatar actualizado exitosamente"), HttpStatus.CREATED);
//    }

    //delete Avatar
    @DeleteMapping("/delete-avatar-chat/{id}")
    @ApiOperation(value = "Eliminar Avatar Chat por Id", notes = "Eliminar Avatar Chat")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!cbAvatarChatService.cbAvatarChatId(id))
            return new ResponseEntity(new Mensaje("No existe ese Avatar"), HttpStatus.NOT_FOUND);

        //Elimina el archivo del avatar por el id
        CbAvatarChat avatarChatDelete = cbAvatarChatService.chatPorId(id).get();
        String nameChatAvatar = avatarChatDelete.getAvatarChat();
        cbAvatarChatService.eliminarFileChatAvatar(nameChatAvatar);

        cbAvatarChatService.borrarAvatarChat(id);
        return new ResponseEntity(new Mensaje("Avatar eliminado Exitosamente"), HttpStatus.OK);
    }

    //Ver Avatar por Ruta
    @GetMapping("/uploads/img-chat/{nombreFoto:.+}")
    @ApiOperation(value = "Ver Avatar Chat Registrado", notes = "Ver avatar")
    public ResponseEntity<Resource> verAvatar(@PathVariable String nombreFoto){

        Resource recurso = null;

        try {
            recurso = cbAvatarChatService.cargar(nombreFoto);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return new ResponseEntity(recurso, HttpStatus.OK);
    }

}
