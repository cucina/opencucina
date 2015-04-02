package org.cucina.security.client;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;

import org.cucina.security.api.UserDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;


/**
 *
 *
 * @author vlevine
  */
public class AuthClient {
    private static final String ACCESS_URL = "http://localhost:8080";

    /**
     * @throws IOException
     * @throws JsonProcessingException
     *
     */
    @Test
    public void testAuth()
        throws JsonProcessingException, IOException {
        RestTemplate restTemplate = new RestTemplate();

        /*UserDto userDto = new UserDto();

        userDto.setEmail("user@email.com");
        userDto.setPassword("password");
        userDto.setUsername("user");

        ResponseEntity<Long> rid = restTemplate.postForEntity(ACCESS_URL + "/user", userDto,
                Long.class);

        System.err.println(rid);*/
        Map<String, String> request = new HashMap<String, String>();

        request.put("username", "user");
        request.put("password", "password");

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> ent = new HttpEntity<String>("username=user&password=password", headers);

        ResponseEntity<String> ria = restTemplate.postForEntity(ACCESS_URL + "/authenticate", ent,
                String.class);

        if (ria.getStatusCode() == HttpStatus.OK) {
            String body = ria.getBody();

            System.err.println(body);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tree = objectMapper.readTree(body);

            assertEquals(true, tree.get("authenticated").asBoolean());
            assertEquals("user", tree.get("name").asText());
        } else {
            System.err.println(ria);
            fail();
        }
    }
}
