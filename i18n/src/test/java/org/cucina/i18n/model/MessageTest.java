package org.cucina.i18n.model;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.i18n.repository.MessageRepository;
import org.cucina.i18n.service.I18nService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


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
		when(i18nService.getDefaultLocale()).thenReturn(Locale.JAPANESE);
		when(instanceFactory.getBean(MutableI18nMessage.TYPE)).thenReturn(new MutableI18nMessage());
		when(bf.getBean(I18nService.I18N_SERVICE_ID)).thenReturn(i18nService);
		when(bf.getBean(MessageRepository.MESSAGE_REPOSITORY_ID)).thenReturn(messageRepository);
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
