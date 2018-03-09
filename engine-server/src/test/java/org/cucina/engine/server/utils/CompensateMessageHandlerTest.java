package org.cucina.engine.server.utils;

import org.cucina.conversation.events.CompensateEvent;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.server.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompensateMessageHandlerTest {
	private CompensateMessageHandler handler;
	@Mock
	private TokenRepository tokenRepository;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new CompensateMessageHandler(tokenRepository);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testOnApplicationEventExisting() {
		CompensateEvent event = new CompensateEvent("haha");

		event.setIds(100L);
		event.setType("Foo");

		ProcessToken token = mock(ProcessToken.class);
		HistoryRecord hr = new HistoryRecord();

		List<HistoryRecord> histories = new ArrayList<HistoryRecord>();

		histories.add(hr);

		when(token.getHistories()).thenReturn(histories);

		Collection<ProcessToken> tokens = Collections.singleton(token);

		when(tokenRepository.findByApplicationTypeAndIds(eq("Foo"), any(Serializable[].class)))
				.thenReturn(tokens);

		Message<CompensateEvent> message = mock(Message.class);

		when(message.getPayload()).thenReturn(event);
		handler.handleMessage(message);
		verify(tokenRepository).save(token);
		assertTrue("Histories should be empty", histories.isEmpty());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testOnApplicationEventNew() {
		CompensateEvent event = new CompensateEvent("haha");

		event.setIds(100L);
		event.setType("Foo");

		ProcessToken token = mock(ProcessToken.class);
		Foo foo = new Foo();

		doReturn(foo).when(token).getDomainObject();

		Collection<ProcessToken> tokens = Collections.singleton(token);

		when(tokenRepository.findByApplicationTypeAndIds(eq("Foo"), any(Serializable[].class)))
				.thenReturn(tokens);

		Message<CompensateEvent> message = mock(Message.class);

		when(message.getPayload()).thenReturn(event);
		handler.handleMessage(message);
		verify(tokenRepository).deleteDeep(token);
	}
}
