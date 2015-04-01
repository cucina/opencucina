package org.cucina.i18n.clients;

import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.cucina.i18n.api.ListNodeDto;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class ListItemClient {
    private static final String LN_ACCESS_URL = "http://localhost:8080/listNode";

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testSingle() {
        RestTemplate restTemplate = new RestTemplate();

        ListNodeDto listNode = new ListNodeDto();

        listNode.setType("reason");
        listNode.setApplication("application");
        listNode.setCode("ho");
        listNode.setDefaultValue(false);
        listNode.setLocale(Locale.UK);
        listNode.setText("Haha");
        listNode.setType("cause");

        ResponseEntity<Long> rid = restTemplate.postForEntity(LN_ACCESS_URL, listNode, Long.class);

        System.err.println(rid);

        ListNodeDto ln = restTemplate.getForObject(LN_ACCESS_URL + "/{id}", ListNodeDto.class,
                rid.getBody());

        System.err.println(ln);
    }
}
