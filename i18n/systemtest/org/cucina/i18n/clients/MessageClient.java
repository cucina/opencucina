package org.cucina.i18n.clients;

import org.cucina.i18n.api.MessageDto;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class MessageClient {
	private static final String MS_ACCESS_URL = "http://localhost:8080/message";

	/**
	 * This one to simulate
	 * <code>curl -i -H "Locale:ko_KR" http://localhost:8080/message/1</code>
	 */
	@Test
	public void testLocaleHeader() {
		RestTemplate restTemplate = new RestTemplate();

		MessageDto dto = new MessageDto();

		dto.setApplication("client");
		dto.setCode("codeX");
		dto.setText("Code Korea");

		HttpHeaders headers = new HttpHeaders();

		headers.set("Locale", Locale.KOREA.toString());

		HttpEntity<MessageDto> entity = new HttpEntity<MessageDto>(dto, headers);

		ResponseEntity<Long> rid = restTemplate.postForEntity(MS_ACCESS_URL,
				entity, Long.class);

		System.err.println(rid);

		Long id = rid.getBody();

		assertNotNull("id is null", id);

		HttpEntity<MessageDto> lentity = new HttpEntity<MessageDto>(headers);
		ResponseEntity<MessageDto> me = restTemplate.exchange(MS_ACCESS_URL
				+ "/{id}", HttpMethod.GET, lentity, MessageDto.class, id);

		System.err.println(me);

		MessageDto body = me.getBody();
		assertEquals(dto.getApplication(), body.getApplication());
		assertEquals(dto.getCode(), body.getCode());
		assertEquals(dto.getText(), body.getText());
		assertEquals(Locale.KOREA, body.getLocale());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSimple() {
		RestTemplate restTemplate = new RestTemplate();

		MessageDto dto = new MessageDto();

		dto.setApplication("client");
		dto.setCode("codeX");
		dto.setLocale(Locale.UK);
		dto.setText("Code UK");

		ResponseEntity<Long> rid = restTemplate.postForEntity(MS_ACCESS_URL,
				dto, Long.class);

		System.err.println(rid);

		Long id = rid.getBody();

		assertNotNull("id is null", id);

		MessageDto m = restTemplate
				.getForObject(MS_ACCESS_URL + "/{id}", MessageDto.class,
						Collections.singletonMap("id", rid.getBody()));

		System.err.println(m);
	}
}
