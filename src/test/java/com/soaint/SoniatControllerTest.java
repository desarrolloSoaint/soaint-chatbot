package com.soaint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.soaint.controller.*;
import com.soaint.service.*;
import org.springframework.util.LinkedMultiValueMap;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureRestDocs
public class SoniatControllerTest {

    @Rule
    public JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation();

    @InjectMocks
    private ChatBotController controller = new ChatBotController();

    @Mock
    private ChatBotService service;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .apply(documentationConfiguration(this.jUnitRestDocumentation))
                .build();
    }

    @Test
    public void soniatChatBot() throws Exception {

        /*String mensaje = "Hola";
        String requestBody = "";

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("question", "Hola");

        Mockito.when(service.chatbotService(mensaje)).thenReturn(mensaje);

        mockMvc.perform(get("/api/v1/bot/botQuestion").params(requestParams)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(mensaje)))
               .andDo(document("Soniat"));*/
    }

}
