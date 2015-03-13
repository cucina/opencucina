package org.cucina.i18n;

import java.text.MessageFormat;

import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.i18n.service.MessageService;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessageControllerTest {
    private MessageController controller;
    @Mock
    private MessageService messageService;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new MessageController();
        ReflectionTestUtils.setField(controller, "messageService", messageService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAllMessages() {
        controller.allMessages();
        verify(messageService).loadAll();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAllMessagesPage() {
        controller.allMessages(0, 100);
        verify(messageService).loadAll(0, 100);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetMessage() {
        when(messageService.loadMessage("code", Locale.ENGLISH, "applicationName"))
            .thenReturn("value");

        MessageFormat mf = controller.getMessage("code", Locale.ENGLISH, "applicationName");

        assertEquals("value", mf.toPattern());
        assertEquals(Locale.ENGLISH, mf.getLocale());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMessageDetails() {
        controller.messageDetails(11L);
        verify(messageService).loadById(11L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveMessage() {
        MessageDto messageDto = new MessageDto();

        controller.saveMessage(messageDto);
        verify(messageService).saveMessage(messageDto);
    }
}
