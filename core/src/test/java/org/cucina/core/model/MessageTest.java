package org.cucina.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.cucina.core.InstanceFactory;
import org.cucina.core.repository.MessageRepository;
import org.cucina.core.service.I18nService;
import org.cucina.core.spring.SingletonBeanFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessageTest {
    @Mock
    private BeanFactory bf;
    @Mock
    private I18nService i18nService;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private MessageRepository messageRepository;

    /**
    * JAVADOC Method Level Comments
    */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(i18nService.getLocale()).thenReturn(Locale.ENGLISH);
        when(messageRepository.getDefaultLocale()).thenReturn(Locale.JAPANESE);
        when(instanceFactory.getBean(MutableI18nMessage.TYPE)).thenReturn(new MutableI18nMessage());
        when(bf.getBean(SingletonBeanFactory.I18N_SERVICE_ID)).thenReturn(i18nService);
        when(bf.getBean(SingletonBeanFactory.MESSAGE_REPOSITORY_ID)).thenReturn(messageRepository);
        when(bf.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID)).thenReturn(instanceFactory);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCompareTo() {
        Message message = new Message();

        message.setMessageCd("messageCd");
        message.setMessageTx("messageTx", Locale.ENGLISH.toString());

        Message o = new Message();

        assertFalse(message.compareTo(o) == 0);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEquals() {
        Message message = new Message();

        message.setMessageCd("messageCd");
        message.setMessageTx("messageTx", Locale.ENGLISH.toString());

        Message o = new Message();

        assertFalse(message.equals(o));
        message.setId(1L);
        o.setId(2L);
        assertFalse(message.equals(o));
        o.setId(1L);
        assertTrue(message.equals(o));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsMessages() {
        Message message = new Message();

        message.setMessageCd("messageCd");
        message.setMessageTx("messageTx", Locale.ENGLISH.toString());

        Message o = new Message();

        assertFalse(message.equalsMessages(o));
        o.setMessageTx("messageTx", Locale.ENGLISH.toString());
        assertTrue(message.equalsMessages(o));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testRemoveMessage() {
        Message message = new Message();

        assertFalse(message.removeMessage(Locale.CANADA.toString()));
        message.setMessageTx("messageTx", Locale.CANADA.toString());
        assertTrue(message.removeMessage(Locale.CANADA.toString()));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToString() {
        Message message = new Message();

        message.setMessageCd("messageCd");
        message.setMessageTx("messageTx", Locale.ENGLISH.toString());
        assertEquals("messageTx", message.toString());
    }
}
