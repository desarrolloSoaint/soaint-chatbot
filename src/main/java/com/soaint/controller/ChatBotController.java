package com.soaint.controller;

import com.soaint.entity.AcClients;
import com.soaint.entity.AcClientsPrivate;
import com.soaint.entity.CbHistorial;
import com.soaint.repository.CbHistorialRepository;
import com.soaint.service.ChatBotService;
//import com.soaint.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@RestController
@RequestMapping("/api/v1/bot")
@Api(tags = "CHATBOT")
public class ChatBotController {

    private static final Logger logger = LoggerFactory.getLogger(ChatBotController.class);

    @Autowired
    private ChatBotService chatBotService;

    @Autowired
    private CbHistorialRepository cbHistorialRepository;

    /*@Autowired
    private AuthService authService;

    @PostMapping(value = "/login", produces = { MediaType.TEXT_PLAIN_VALUE, MediaType.ALL_VALUE }, consumes= MediaType.ALL_VALUE)
    @ApiOperation(value = "Generar token", notes = "Peticion del Token mediante credenciales")
    public String login() {
        String token = authService.login();
        return token;
    }*/

    //Get Conversacion con Soniat Modo Publico
    @GetMapping(value = "/botQuestionPublic")
    @ApiOperation(value = "Conversacion con el Chat en Modo Publico", notes = "Aqui se recibe el mensaje por el usuario publico obteniendo su respuesta a traves de los AIML")
    public Object botPublico(
            @ApiParam(value = "Escribe el mensaje que se le enviara al Chat" , required = true)@RequestParam String question,
            @ApiParam(value = "id_client_public" , required = false) AcClients client) {

        Date dateQuestion = new Date();
        String x = chatBotService.chatbotService(question);
        Date dateResponse = new Date();

        CbHistorial chat = new CbHistorial();

        chat.setId_client(client);
        chat.setQuestion_client(question);
        chat.setResponse_soniat(x);
        chat.setTime_question_client(dateQuestion);
        chat.setTime_response_soniat(dateResponse);
        chatBotService.guardarHistorial(chat);

        return chat;
    }

    //Get Conversacion con Soniat Modo Privado
    @GetMapping(value = "/botQuestionPrivate")
    @ApiOperation(value = "Conversacion con el Chat en Modo Privado", notes = "Aqui se recibe el mensaje por el usuario privado obteniendo su respuesta a traves de los AIML")
    public Object botPrivate(
            @ApiParam(value = "Escribe el mensaje que se le enviara al Chat" , required = true)@RequestParam String question,
            @ApiParam(value = "id_client_public" , required = false) AcClientsPrivate client) {

        Date dateQuestion = new Date();
        String x = chatBotService.chatbotService(question);
        Date dateResponse = new Date();

        CbHistorial chat = new CbHistorial();

        chat.setId_client_private(client);
        chat.setQuestion_client(question);
        chat.setResponse_soniat(x);
        chat.setTime_question_client(dateQuestion);
        chat.setTime_response_soniat(dateResponse);
        chatBotService.guardarHistorial(chat);

        return chat;
    }

    //Get Traer todas las conversaciones de Soniat
    @GetMapping("/historial")
    @ApiOperation(value = "Devuelve el Registro de Conversaciones del Chat", notes = "traer todos los registros con las conversaciones de soniat")
    public List<CbHistorial> findAll(){ return chatBotService.obtenerTodos(); }

    //Get Traer cantidad de conversaciones de Soniat
    @GetMapping("/count-question")
    @ApiOperation(value = "Devuelve la Cantidad de Preguntas y Respuestas del Chat", notes = "Cuenta las preguntas y respuestas hechas en el chabot en general")
    public long countAll(){ return chatBotService.contadorQuestion(); }

    //Get Traer todas las conversaciones de Soniat
    @GetMapping("/time-response/{id}")
    @ApiOperation(value = "Devuelve Tiempo de Respuesta que Tarda Responder el Chatbot", notes = "Muestra el tiempo de respuesta que tarda el chatbot en responder")
    public Long timeResponse(@PathVariable Integer id){ return chatBotService.timeResponse(id);}

    //Get Traer Hora Conversaciones Dia
    @GetMapping("/time-one-day")
    @ApiOperation(value = "Devuelve Hora con mas Conversaciones en un Dia", notes = "Indica la hora en el cual se realizaron mas conversaciones con el chatbot durante el dia")
    public Integer HrMayorDay(){ return chatBotService.timeOneDay(); }

    //Get Traer Hora Conversaciones Semana
    @GetMapping("/time-one-week")
    @ApiOperation(value = "Hora con mas Conversaciones en la ultima semana", notes = "Indica la hora en el cual se realizaron mas conversaciones en la ultima semana")
    public Integer TimeForWeek(){ return chatBotService.TimeForWeek(); }

    //Get Traer Hora Conversaciones Mes
    @GetMapping("/time-one-month")
    @ApiOperation(value = "Devuelve Hora con mas Conversaciones del Mes Pasado", notes = "Indica la hora en el cual se realizaron mas conversaciones con el chatbot durante el dia")
    public Integer HrMayorMonth(){ return chatBotService.timeOneMonth(); }

    //Get Traer Cliente Publico Mas Frecuente Dia
    @GetMapping("/frec-client-public-today")
    @ApiOperation(value = "Devuelve Cliente Publico Mas Frecuente en el Dia", notes = "Muestra al cliente publico con mas conversaciones con el chat durante el dia")
    public AcClients ClientPublicFrecOneDay(){
        return chatBotService.ClientPublicFrecOneDay(); }

    //Get Traer Cliente Privado Mas Frecuente Dia
    @GetMapping("/frec-client-private-today")
    @ApiOperation(value = "Devuelve Cliente Privado Mas Frecuente en el Dia", notes = "Muestra al cliente privado con mas conversaciones con el chat durante el dia")
    public AcClientsPrivate ClientPrivateFrecOneDay(){
        return chatBotService.ClientPrivateFrecOneDay(); }

    //Get Traer Cliente Publico Mas Frecuente Semana
    @GetMapping("/frec-client-public-lastweek")
    @ApiOperation(value = "Devuelve Cliente Publico Mas Frecuente en la ultima semana", notes = "Muestra al cliente publico con mas conversaciones con el chat en la ultima semana")
    public AcClients ClientPublicFrecLastWeek(){
        return chatBotService.ClientPublicFrecLastWeek(); }

    //Get Traer Cliente Privado Mas Frecuente Semana
    @GetMapping("/frec-client-private-lastweek")
    @ApiOperation(value = "Devuelve Cliente Privado Mas Frecuente en la ultima semana", notes = "Muestra al cliente Privado con mas conversaciones con el chat en la ultima semana")
    public AcClientsPrivate ClientPrivateFrecLastWeek(){
        return chatBotService.ClientPrivateFrecLastWeek(); }

    //Get Traer Cliente Publico Mas Frecuente Mes
    @GetMapping("/frec-client-public-lastmonth")
    @ApiOperation(value = "Devuelve Cliente Publico Mas Frecuente  del Mes pasado", notes = "Muestra al cliente publico con mas conversaciones con el chat del mes pasado ")
    public AcClients ClientPublicFrecLastMonth(){
        return chatBotService.ClientPublicFrecLastMonth(); }

    //Get Traer Cliente Privado Mas Frecuente Mes
    @GetMapping("/frec-client-private-lastmonth")
    @ApiOperation(value = "Devuelve Cliente Privado Mas Frecuente  del Mes pasado", notes = "Muestra al cliente privado con mas conversaciones con el chat del mes pasado ")
    public AcClientsPrivate ClientPivateFrecLastMonth(){
        return chatBotService.ClientPrivateFrecLastMonth(); }

    //Get Traer Cantidad  Conversaciones por Time_Expired con Soniat
    @GetMapping("/count-time-expired")
    @ApiOperation(value = "Cantidad de Conversaciones terminadas por Tiempo Expirado", notes = "Indica la Cantidad de Conversaciones terminadas por tiempo expirado por el cliente")
    public Integer countExpired(){ return chatBotService.contadorExpired(); }

    //Get Traer Clientes Publicos Mas Frecuentes
    @GetMapping("/frec-client-public")
    @ApiOperation(value = "Devuelve los Clientes Publicos mas Frecuentes", notes = "Muestra mediante un Array [Correo, Cant de mensajes] realizadas por ese cliente publico")
    public ArrayList UsersFrec(){
        return chatBotService.UsersFrec(); }

    //Get Traer Clientes Privados Mas Frecuentes
    @GetMapping("/frec-client-private")
    @ApiOperation(value = "Devuelve los Clientes Privados mas Frecuentes", notes = "Muestra mediante un Array [Correo, Cant de mensajes] realizadas por ese cliente privado")
    public ArrayList UsersFrecPrivate(){
        return chatBotService.UsersFrecPrivate(); }

    //Get Traer Cantidad Respuestas con Link
    @GetMapping("/count-response-link")
    @ApiOperation(value = "Cantidad de Respuestas  con Redireccionamientos", notes = "Indica la Cantidad de Respuestas con Redireccionamiento")
    public Integer countLink (){
        return chatBotService.contadorLink(); }

    //Preguntas que las Respuestas no se conocen
    @GetMapping("/question-unknown")
    @ApiOperation(value = "Devuelve las preguntas que no se conocen", notes = "Devuelve las preguntas que no se conocen")
    public List questionUnknown(){
        return chatBotService.questionUnknown(); }

    //Trae las respuestas mas dada por el chat
    @GetMapping("/frec-response")
    @ApiOperation(value = "Trae los temas mas consultados al chat", notes = "Trae los temas mas consultados al chat")
    public ArrayList FrecResponse(){
        return chatBotService.FrecResponse(); }

    //Trae las preguntas mas hechas al chat
    @GetMapping("/frec-question")
    @ApiOperation(value = "Trae las preguntas mas consultadas al chat", notes = "Trae las preguntas mas consultadas al chat")
    public ArrayList FrecQuestion(){
        return chatBotService.FrecQuestion(); }

    //Trae el numero de preguntas y respuestas hechas al chat desde la ultima semana
    @GetMapping("/count-last-week")
    @ApiOperation(value = "Trae el numero de preguntas y respuestas hechas al chat desde la ultima semana", notes = "Trae el numero de preguntas y respuestas hechas al chat desde la ultima semana")
    public Long CountLastWeek(){
        return chatBotService.CountLastWeek(); }

    //Trae el numero de preguntas y respuestas hechas al chat del dia de hoy
    @GetMapping("/count-today")
    @ApiOperation(value = "Trae el numero de preguntas y respuestas hechas al chat del dia de hoy", notes = "Trae el numero de preguntas y respuestas hechas al chat del dia de hoy")
    public Long CountToday(){
        return chatBotService.CountToday(); }


}
