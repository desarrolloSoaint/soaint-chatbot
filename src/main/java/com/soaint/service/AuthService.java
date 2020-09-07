/*package com.soaint.service;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Arrays;

@Service
public class AuthService {

    @Value("${security.oauth2.token_url}")
    private String TOKEN_URL;

    /**
     * Service is used for Auth the user
     * @return OAuth2 object
     */
    /*public String login() {
        // ********************* Get Token *******************************
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();

        // According OAuth documentation we need to send the client id and secret key in the header for authentication
        String credentials = "soaint-client" + ":" + "$2a$04$e/c1/RfsWuThaWFCrcCuJeoyvwCV0URN/6Pn9ZFlrtIWaU/vj/BfG";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        String access_token_url = TOKEN_URL;

        LinkedMultiValueMap map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "admin");
        map.add("password", "password");
        HttpEntity request = new HttpEntity<MultiValueMap<String, String>>(map,headers);

        //access_token_url += "?username=admin" + "&password=password"  + "&grant_type=password";

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(response.getBody());
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        String token = node.path("access_token").asText();
        //String ttl = node.path("expires_in").asText();

        //System.out.println("Access Token Response ---------" + response.getBody());
        // ********************* Get Token *******************************
        return token;
    }

}*/