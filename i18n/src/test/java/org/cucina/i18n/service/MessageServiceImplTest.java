package org.cucina.i18n.service;

import org.cucina.i18n.api.MessageDto;
import org.cucina.i18n.model.I18nMessage;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessageServiceImplTest {
	@Mock
	private ConversionService conversionService;
	@Mock
	private I18nService i18nService;
	@Mock
	private MessageRepository messageRepository;
	private MessageServiceImpl service;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new MessageServiceImpl(messageRepository, conversionService, i18nService);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLoadAll() {
		Collection<Message> mess = new ArrayList<Message>();
		Message message = new Message();

		mess.add(message);
		when(messageRepository.findAll()).thenReturn(mess);

		Collection<MessageDto> mdtos = new ArrayList<MessageDto>();
		MessageDto dto = new MessageDto();

		mdtos.add(dto);
		when(conversionService.convert(message, Collection.class)).thenReturn(mdtos);

		Collection<MessageDto> result = service.loadAll();

		assertTrue(result.contains(dto));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLoadAllPage() {
		Collection<Message> mess = new ArrayList<Message>();
		Message message = new Message();

		mess.add(message);

		Pageable pageable = new PageRequest(0, 100);

		when(messageRepository.findAll(pageable)).thenReturn(mess);

		Collection<MessageDto> mdtos = new ArrayList<MessageDto>();
		MessageDto dto = new MessageDto();

		mdtos.add(dto);
		when(conversionService.convert(message, Collection.class)).thenReturn(mdtos);

		Collection<MessageDto> result = service.loadAll(0, 100);

		assertTrue(result.contains(dto));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLoadById() {
		Message message = mock(Message.class);

		when(messageRepository.findById(11L)).thenReturn(message);

		Collection<MessageDto> dtos = new ArrayList<MessageDto>();
		MessageDto dto1 = new MessageDto();

		dto1.setLocale(Locale.ENGLISH);
		dto1.setText("eng");
		dtos.add(dto1);

		MessageDto dto2 = new MessageDto();

		dto2.setLocale(Locale.UK);
		dto2.setText("uk");
		dtos.add(dto2);
		when(conversionService.convert(message, Collection.class)).thenReturn(dtos);

		MessageDto dto = service.loadById(11L, Locale.US);

		assertEquals(dto1, dto);
		dto = service.loadById(11L, Locale.UK);
		assertEquals(dto2, dto);
		dto = service.loadById(11L, null);
		assertNull(dto);
		when(i18nService.getDefaultLocale()).thenReturn(Locale.ENGLISH);
		dto = service.loadById(11L, Locale.FRANCE);
		assertEquals(dto1, dto);
		dto = service.loadById(11L, Locale.ENGLISH);
		assertEquals(dto1, dto);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLoadMessage() {
		when(i18nService.getDefaultLocale()).thenReturn(Locale.PRC);

		Message message = mock(Message.class);

		when(message.getBaseName()).thenReturn("basename");

		I18nMessage imessc = mock(I18nMessage.class);

		when(imessc.getMessageTx()).thenReturn("PRC");
		when(message.getMessage(Locale.PRC.toString())).thenReturn(imessc);

		when(messageRepository.findByBasenameAndCode("basename", "code")).thenReturn(message);

		Collection<MessageDto> dtos = new ArrayList<MessageDto>();

		when(conversionService.convert(message, Collection.class)).thenReturn(dtos);

		MessageDto dto1 = new MessageDto();

		dto1.setLocale(Locale.PRC);
		dto1.setText("PRC");
		dtos.add(dto1);

		assertEquals("PRC", service.loadMessage("code", Locale.UK, "basename"));

		MessageDto dto2 = new MessageDto();

		dto2.setLocale(Locale.ENGLISH);
		dto2.setText("enC");
		dtos.add(dto2);

		assertEquals("enC", service.loadMessage("code", Locale.UK, "basename"));

		MessageDto dto3 = new MessageDto();

		dto3.setLocale(Locale.UK);
		dto3.setText("en_GBu");
		dtos.add(dto3);

		assertEquals("en_GBu", service.loadMessage("code", Locale.UK, "basename"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSaveMessage() {
		MessageDto messageDto = new MessageDto();

		messageDto.setCode("code");
		messageDto.setText("text");
		messageDto.setLocale(Locale.ITALY);
		service.saveMessage(messageDto);
		verify(messageRepository).save(null, Locale.ITALY.toString(), "code", "text");
	}
}
