package com.soaint.service;

import com.soaint.controller.ChatBotController;
import com.soaint.entity.AcClients;
import com.soaint.entity.AcClientsPrivate;
import com.soaint.entity.CbHistorial;
import com.soaint.repository.CbHistorialRepository;
import com.soaint.utils.Colors;
import org.alicebot.ab.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatBotService {

    @Autowired
    CbHistorialRepository cbHistorialRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatBotController.class);

    private static final boolean TRACE_MODE = false;
    static String botName = "super";
    private Object ArrayList;

    public String chatbotService(String answer){
        String response = "";

        try{
            String resourcePath = getResourcePath();

            // Todo: ************* Eliminar esto una vez probada en rest
            logger.info(Colors.ANSI_BLUE +  "Resource Path" + Colors.ANSI_RESET);
            logger.info(Colors.ANSI_BLUE + resourcePath + Colors.ANSI_RESET);
            // *******************

            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("super", resourcePath);

            logger.info("resourcePath: " + resourcePath);

            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine = "";

            //while(true){
                logger.info("TU: ");

                // se comentó ya que el valor lo entrega rest
                //textLine = IOUtils.readInputTextLine();

                textLine = answer;
                if((textLine == null ) || (textLine.length() < 1)){
                    textLine = MagicStrings.null_input;
                }
                if (textLine.equals("q")) {
                    System.exit(0);
                }else if (textLine.equals("eq")){
                    bot.writeQuit();
                    System.exit(0);
                }
                else {
                    String request = textLine;
                    if (MagicBooleans.trace_mode){
                        logger.info("STATE=" + request + ":THAT=" + (chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                    }
                    response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;")) {
                        response = response.replace("&lt;", "<");
                    }//while
                    while (response.contains("&gt;")){
                        response = response.replace("&gt;", ">");
                    }//while

                    // Transform ISO-8859-1 to UTF-8
                    //response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                    logger.info("Soniat: " + response);

                }//else
            //}//while(true)

        }catch (Exception e){
            e.printStackTrace();
        }

        Date dateResponse = new Date();
//        List [] data = {dateResponse,response};
        return response;

    }

    private String getResourcePath(){

        //TODO:
        //  With this code:    file:/C:/Desarrollo/Test/SoniatV2-1.0.0.jar!/BOOT-INF/classes!/
        //String resourcesPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        //resourcesPath = resourcesPath.substring(0,resourcesPath.length()-1);
        //resourcesPath = "jar:" + resourcesPath;

        //TODO:
        //  With this code:    C:\Desarrollo\Test\src\main\resources
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() -2);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";

        return resourcesPath;
    }

    //Guarda la conversacion
    public void guardarHistorial(CbHistorial cbHistorial){
        cbHistorialRepository.save(cbHistorial);
    }

    //Obtiene todos los registros de las conversaciones
    public List<CbHistorial> obtenerTodos(){ List<CbHistorial> lista = cbHistorialRepository.findAll();
        return lista;
    }

    //cuenta todas las preguntas y respuestas que da la base de datos
    public long contadorQuestion(){ long lista = cbHistorialRepository.count();
        return lista;
    }

    //Verificar si existe ese ID que se consulta.
    public Optional<CbHistorial> obtenerPorId(Integer id){
        return cbHistorialRepository.findById(id);
    }

    //Muestra la hora en la cual el chat tuvo mas conversiones durante el dia.
    public Integer timeOneDay(){ List<CbHistorial> lista = cbHistorialRepository.OrderASC();

        Long registros = cbHistorialRepository.count();
        Integer cont = 0;
        Integer finalTime = 0;
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        for(int i = 1; i < 25; i++){
            Integer Referencia = i;
            Integer contador = 0;
            for(int l = 0; l < lista.size(); l++){
                if (ultimo.getTime_question_client().getDate() == lista.get(l).getTime_question_client().getDate()){
                    if ( lista.get(l).getTime_question_client().getHours() == Referencia){

                            contador ++;

                            if (contador > cont){

                                cont = contador;
                                finalTime = Referencia;
                            }
                        }
                }
            }
        }


        return finalTime;
    }

    //Muestra la hora en la cual el chat tuvo mas conversiones durante el mes.
    public Integer timeOneMonth(){ List<CbHistorial> lista = cbHistorialRepository.OrderASC();

        Long registros = cbHistorialRepository.count();
        Integer cont = 0;
        Integer finalTime = 0;
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        int Month = ultimo.getTime_question_client().getMonth() - 1;
        int Year = ultimo.getTime_question_client().getYear();

        if (Month == -1){ Month = 11; Year = Year - 1; }

            for(int j = 1; j < 25; j++) {
                Integer HoraRef = j;
                Integer contador = 0;
                for (int l = 0; l < registros; l++) {
                    if (lista.get(l).getTime_question_client().getYear() == Year) {
                        if (lista.get(l).getTime_question_client().getMonth() == Month) {
                            if (lista.get(l).getTime_question_client().getHours() == HoraRef) {

                                contador++;

                                if (contador > cont) {

                                    cont = contador;
                                    finalTime = HoraRef;
                                }
                            }
                        }
                    }
                }
            }
        return finalTime;
    }

    //Muestra la hora en la cual el chat tuvo mas conversiones durante semana.
    public Integer TimeForWeek(){

        List<CbHistorial> lista = cbHistorialRepository.LastWeek();
        Integer cont = 0;
        Integer finalTime = 0;

        for(int j = 1; j < 25; j++) {
            Integer HoraRef = j;
            Integer contador = 0;
            for (int i = 0; i < lista.size(); i++) {
                if ( lista.get(i).getTime_question_client().getHours() == HoraRef){

                    contador ++;

                    if (contador > cont){

                        cont = contador;
                        finalTime = HoraRef;
                    }
                }
            }
        }

        return finalTime;
    }

    //Obtiene el tiempo de respuesta por parte del chat
    public Long timeResponse(Integer id){

        CbHistorial rango = obtenerPorId(id).get();

        Date question = rango.getTime_question_client();
        Date response = rango.getTime_response_soniat();
        Long difference = response.getTime() - question.getTime();


        return difference;

    }

    //Muestra los la cantidad de conversaciones que salieron por expiracion del tiempo.
    public Integer contadorExpired(){ List<CbHistorial> lista = cbHistorialRepository.findAll();

        Long registros = cbHistorialRepository.count();

            Integer contador = 0;
            for (int j = 0; j < registros; j++){
                if (lista.get(j).getExpired_time() != null){
                    contador ++;
                }
            }

        return contador;
    }

    //Muestra el cliente publico mas frencuente que usa el chatbot durante el dia actual.
    public AcClients ClientPublicFrecOneDay(){ List<CbHistorial> lista = cbHistorialRepository.OrderASC();

        Long registros = contadorQuestion();

        Integer cont = 0;

        AcClients client = new AcClients();
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        for(int l = 0; l < registros; l++){
            Integer contador = 0;
            if (ultimo.getTime_question_client().getDate() == lista.get(l).getTime_question_client().getDate()){
                for (int m = 0; m < registros; m++){
                    if (ultimo.getTime_question_client().getDate() == lista.get(m).getTime_question_client().getDate()){
                        if (lista.get(l).getId_client() == lista.get(m).getId_client()){
                            if(lista.get(l).getId_client() != null){

                                contador ++;

                                if (contador > cont){

                                    cont = contador;
                                    client = lista.get(l).getId_client();
                                 }
                            }
                        }
                    }
                }
            }
        }
        return client;
    }

    //Muestra el cliente publico mas frencuente que usa el chatbot durante el dia actual.
    public AcClientsPrivate ClientPrivateFrecOneDay(){ List<CbHistorial> lista = obtenerTodos();

        Long registros = contadorQuestion();

        Integer cont = 0;
        AcClientsPrivate client = new AcClientsPrivate();
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        for(int l = 0; l < registros; l++){
            Integer contador = 0;
            if (ultimo.getTime_question_client().getDate() == lista.get(l).getTime_question_client().getDate()){
                for (int m = 0; m < registros; m++){
                    if (ultimo.getTime_question_client().getDate() == lista.get(m).getTime_question_client().getDate()){
                        if (lista.get(l).getId_client_private() == lista.get(m).getId_client_private()){
                            if(lista.get(l).getId_client_private() != null){
                                contador ++;

                                if (contador > cont){

                                     cont = contador;
                                    client = lista.get(l).getId_client_private();
                                }
                            }
                        }
                    }
                }
            }
        }
        return client;
    }

    //Muestra el cliente publico mas frencuente que usa el chatbot durante la ultima semana.
    public AcClients ClientPublicFrecLastWeek(){ List<CbHistorial> lista = cbHistorialRepository.LastWeek();

        Integer cont = 0;
        AcClients client = new AcClients();

        for(int l = 0; l < lista.size(); l++){
            Integer contador = 0;
                for (int m = 0; m < lista.size(); m++){
                    if (lista.get(l).getId_client() == lista.get(m).getId_client()){
                        if(lista.get(l).getId_client() != null){

                            contador ++;

                            if (contador > cont){

                                cont = contador;
                                client = lista.get(l).getId_client();
                            }
                        }
                    }
                }
        }
        return client;
    }

    //Muestra el cliente privado mas frencuente que usa el chatbot durante la ultima semana.
    public AcClientsPrivate ClientPrivateFrecLastWeek(){ List<CbHistorial> lista = cbHistorialRepository.LastWeek();

        Integer cont = 0;
        AcClientsPrivate client = new AcClientsPrivate();

        for(int l = 0; l < lista.size(); l++){
            Integer contador = 0;
            for (int m = 0; m < lista.size(); m++){
                if (lista.get(l).getId_client_private() == lista.get(m).getId_client_private()){
                    if(lista.get(l).getId_client_private() != null){

                        contador ++;

                        if (contador > cont){

                            cont = contador;
                            client = lista.get(l).getId_client_private();
                        }
                    }
                }
            }
        }
        return client;
    }

    //Muestra el cliente publico mas frencuente que usa el chatbot durante el ultimo mes.
    public AcClients ClientPublicFrecLastMonth(){ List<CbHistorial> lista = cbHistorialRepository.OrderASC();

        Long registros = contadorQuestion();

        Integer cont = 0;
        AcClients client = new AcClients();
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        int Month = ultimo.getTime_question_client().getMonth() - 1;
        int Year = ultimo.getTime_question_client().getYear();

        for(int l = 0; l < registros; l++){
            Integer contador = 0;
            if (Year == lista.get(l).getTime_question_client().getYear()) {
                if (Month == lista.get(l).getTime_question_client().getMonth()) {
                    for (int m = 0; m < registros; m++) {
                        if (Year == lista.get(m).getTime_question_client().getYear()) {
                            if (Month == lista.get(m).getTime_question_client().getMonth()) {
                                if (lista.get(l).getId_client() == lista.get(m).getId_client()) {
                                    if(lista.get(l).getId_client() != null){
                                        contador++;

                                        if (contador > cont) {

                                            cont = contador;
                                            client = lista.get(l).getId_client();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return client;
    }

    //Muestra el cliente privado mas frencuente que usa el chatbot durante el ultimo mes.
    public AcClientsPrivate ClientPrivateFrecLastMonth(){ List<CbHistorial> lista = cbHistorialRepository.OrderASC();

        Long registros = contadorQuestion();

        Integer cont = 0;
        AcClientsPrivate client = new AcClientsPrivate();
        CbHistorial ultimo = lista.get(registros.intValue() - 1);

        int Month = ultimo.getTime_question_client().getMonth() - 1;
        int Year = ultimo.getTime_question_client().getYear();

        for(int l = 0; l < registros; l++){
            Integer contador = 0;
            if (Year == lista.get(l).getTime_question_client().getYear()) {
                if (Month == lista.get(l).getTime_question_client().getMonth()) {
                    for (int m = 0; m < registros; m++) {
                        if (Year == lista.get(m).getTime_question_client().getYear()) {
                            if (Month == lista.get(m).getTime_question_client().getMonth()) {
                                if (lista.get(l).getId_client_private() == lista.get(m).getId_client_private()) {
                                    if(lista.get(l).getId_client_private() != null){
                                        contador++;

                                        if (contador > cont) {

                                            cont = contador;
                                            client = lista.get(l).getId_client_private();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return client;
    }

    //Muestra los Usuarios mas frencuentes que usan el chatbot.
    public ArrayList UsersFrec(){
        ArrayList lista = cbHistorialRepository.UsersFrec();
        return lista;
   }

    //Muestra los Usuarios privados mas frencuentes que usan el chatbot.
    public ArrayList UsersFrecPrivate(){
        ArrayList lista = cbHistorialRepository.UsersFrecPrivate();
        return lista;
    }

    //Cuenta todas las  respuestas Soniat que den con Redireccionamientos
    public Integer contadorLink() {
        List<CbHistorial> lista = cbHistorialRepository.findAll();

        List<Long> count = new ArrayList<>();
        lista.stream().forEach(i -> {
            String match = "http?s|www";
            Pattern p = Pattern.compile(match);
            Matcher m = p.matcher(i.getResponse_soniat());
            Long c = 0L;
            if (m.find()) {
                c = c + 1;
                count.add(c);
            }
        });

        return count.size();

    }

    //Preguntas que las Respuestas no se conocen
    public List<CbHistorial> questionUnknown() {
        List<CbHistorial> lista = cbHistorialRepository.findAll();

        List<CbHistorial> questions = new ArrayList<>();
        lista.stream().forEach(i -> {
            String match = "comprendo";
            Pattern p = Pattern.compile(match);
            Matcher m = p.matcher(i.getResponse_soniat());

            if (m.find()) {
                questions.add(i);
            }
        });
        return questions;
    }

    //Muestra las respuestas mas consultados al chat
    public ArrayList FrecResponse(){
        ArrayList lista = cbHistorialRepository.FrecResponse();
        return lista;
    }

    //Muestra los temas mas consultados al chat
    public ArrayList FrecQuestion(){
        ArrayList lista = cbHistorialRepository.FrecQuestion();
        return lista;
    }

    //cuenta las preguntas y respuestas de la ultima semana
    public Long CountLastWeek(){
        Long lista = cbHistorialRepository.CountLastWeek();
        return lista;
    }

    //cuenta las preguntas y respuestas del dia de hoy
    public Long CountToday(){
        Long lista = cbHistorialRepository.CountToday();
        return lista;
    }




}
