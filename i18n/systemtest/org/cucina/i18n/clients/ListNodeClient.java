package org.cucina.i18n.clients;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.model.Message;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class ListNodeClient {
    private static final String LN_ACCESS_URL = "http://localhost:8080/listNode";
    

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testSingle() {
        RestTemplate restTemplate = new RestTemplate();
        
        ListNode listNode = new ListNode();

        listNode.setType("reason");

        Message label = new Message();

        listNode.setLabel(label);

        ResponseEntity<Long> rid = restTemplate.postForEntity(LN_ACCESS_URL, listNode, Long.class);

        System.err.println(rid);

        ListNode ln = restTemplate.getForObject(LN_ACCESS_URL, ListNode.class,
                Collections.singletonMap("id", rid.getBody()));

        System.err.println(ln);
    }
}
