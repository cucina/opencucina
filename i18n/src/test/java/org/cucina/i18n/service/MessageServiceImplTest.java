package org.cucina.i18n.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.cucina.i18n.MessageDto;
import org.cucina.i18n.model.I18nMessage;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


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
        service = new MessageServiceImpl(i18nService, messageRepository, conversionService);
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
        service.loadById(11L);
        verify(messageRepository).findById(11L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLoadMessage() {
        when(messageRepository.getDefaultLocale()).thenReturn(Locale.PRC);

        Collection<Message> messages = new ArrayList<Message>();
        Message message = mock(Message.class);

        when(message.getBaseName()).thenReturn("basename");

        I18nMessage imessc = mock(I18nMessage.class);

        when(imessc.getMessageTx()).thenReturn("PRC");
        when(message.getMessage(Locale.PRC.toString())).thenReturn(imessc);

        messages.add(message);
        when(messageRepository.findByBasenamesAndCode(Arrays.asList("basename"), "code"))
            .thenReturn(messages);
        assertEquals("PRC", service.loadMessage("code", Locale.UK, "basename"));

        I18nMessage imesse = mock(I18nMessage.class);

        when(imesse.getMessageTx()).thenReturn("enC");
        when(message.getMessage(Locale.ENGLISH.toString())).thenReturn(imesse);
        assertEquals("enC", service.loadMessage("code", Locale.UK, "basename"));

        I18nMessage imessg = mock(I18nMessage.class);

        when(imessg.getMessageTx()).thenReturn("en_GBu");
        when(message.getMessage(Locale.UK.toString())).thenReturn(imessg);
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
        when(i18nService.getLocale()).thenReturn(Locale.ITALY);
        service.saveMessage(messageDto);
        verify(messageRepository).save(null, Locale.ITALY.toString(), "code", "text");
    }
}
