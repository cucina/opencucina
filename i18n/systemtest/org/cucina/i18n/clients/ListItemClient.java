package org.cucina.i18n.clients;

import org.cucina.i18n.api.ListItemDto;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;


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

		ListItemDto listNode = new ListItemDto();

		listNode.setType("reason");
		listNode.setApplication("application");
		listNode.setCode("ho");
		listNode.setDefaultValue(false);
		listNode.setLocale(Locale.UK);
		listNode.setText("Haha");
		listNode.setType("cause");

		ResponseEntity<Long> rid = restTemplate.postForEntity(LN_ACCESS_URL, listNode, Long.class);

		System.err.println(rid);

		ListItemDto ln = restTemplate.getForObject(LN_ACCESS_URL + "/{id}", ListItemDto.class,
				rid.getBody());

		System.err.println(ln);
	}
}
