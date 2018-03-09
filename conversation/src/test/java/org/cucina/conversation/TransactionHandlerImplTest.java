package org.cucina.conversation;

import org.cucina.conversation.events.CommitSuccessEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;


/**
 * @author vlevine
 */
public class TransactionHandlerImplTest {
	@Mock
	private MessageChannel asyncChannel;
	private TransactionHandlerImpl handler;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new TransactionHandlerImpl(asyncChannel);
	}

	/**
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testRegisterTxHandler() {
		TransactionSynchronizationManager.initSynchronization();
		handler.registerTxHandler("entityType", 1L);
		TransactionSynchronizationUtils.triggerAfterCompletion(TransactionSynchronization.STATUS_COMMITTED);

		ArgumentCaptor<Message> mac = ArgumentCaptor.forClass(Message.class);

		verify(asyncChannel).send(mac.capture());

		Message<?> m = mac.getValue();

		assertTrue(m.getPayload() instanceof CommitSuccessEvent);

		CommitSuccessEvent cse = (CommitSuccessEvent) m.getPayload();

		Object id = ((Object[]) cse.getSource())[0];

		assertEquals(1L, id);
	}
}
