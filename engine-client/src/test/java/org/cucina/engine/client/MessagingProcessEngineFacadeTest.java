package org.cucina.engine.client;

import org.cucina.conversation.TransactionHandler;
import org.cucina.engine.server.definition.HistoryRecordDto;
import org.cucina.engine.server.event.BulkTransitionEvent;
import org.cucina.engine.server.event.SingleTransitionEvent;
import org.cucina.engine.server.event.StartWorkflowEvent;
import org.cucina.engine.server.event.ValueEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;


/**
 * @author vlevine
 */
public class MessagingProcessEngineFacadeTest {
	@Mock
	private MessageChannel asyncChannel;
	@Mock
	private MessageChannel requestChannel;
	private MessagingProcessEngineFacade facade;
	private Object requestPayload;
	@Mock
	private SubscribableChannel replyChannel;
	@Mock
	private TransactionHandler transactionHandler;
	private ValueEvent reply;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		facade = new MessagingProcessEngineFacade();
		facade.setRequestChannel(requestChannel);
		facade.setReplyChannel(replyChannel);
		reply = new ValueEvent(this);
		doAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation)
					throws Throwable {
				Message<?> m = (Message<?>) invocation.getArguments()[0];

				requestPayload = m.getPayload();

				Message<ValueEvent> message = MessageBuilder.withPayload(reply).build();
				MessageChannel replyChannel = (MessageChannel) m.getHeaders().getReplyChannel();

				replyChannel.send(message);

				return true;
			}
		}).when(requestChannel).send(any(Message.class), anyLong());
	}

	/**
	 *
	 */
	@Test
	public void testListTransitions() {
		Collection<String> source = new ArrayList<String>();

		source.add("a");
		source.add("b");

		reply.setValue(source);

		Collection<Serializable> ids = Collections.<Serializable>singleton(100L);
		Collection<String> result = facade.listTransitions(ids, "applicationType");

		assertNotNull("Result is null", result);
		assertEquals(source, result);
	}

	/**
	 *
	 */
	@Test
	public void testListWorkflowProperties() {
		Collection<Map<String, Object>> source = new ArrayList<Map<String, Object>>();

		source.add(Collections.<String, Object>singletonMap("key", "value"));
		reply.setValue(source);

		Collection<Serializable> ids = Collections.<Serializable>singleton(100L);
		Collection<Map<String, Object>> result = facade.listWorkflowProperties(ids,
				"applicationType");

		assertNotNull("Result is null", result);
		assertEquals(source, result);
	}

	/**
	 *
	 */
	@Test
	public void testLoadTransitionInfo() {
		Collection<Map<String, String>> source = new ArrayList<Map<String, String>>();

		source.add(Collections.singletonMap("key", "value"));
		reply.setValue(source);

		Collection<Map<String, String>> result = facade.loadTransitionInfo("workflowId");

		assertNotNull("Result is null", result);
		assertEquals(source, result);
	}

	/**
	 *
	 */
	@Test
	public void testNoTxBulkTransition() {
		ReflectionTestUtils.setField(facade, "transactionHandler", transactionHandler);

		Map<Serializable, Integer> entities = new HashMap<Serializable, Integer>();

		entities.put(3L, 20);
		facade.bulkTransition(entities, "applicationType", "transitionId", "comment", "approvedAs",
				"assignedTo", null, "reason", null);
		assertTrue(requestPayload instanceof BulkTransitionEvent);
		verify(transactionHandler).registerTxHandler("applicationType", 3L);
	}

	/**
	 *
	 */
	@Test
	public void testNoTxMakeTransition() {
		ReflectionTestUtils.setField(facade, "transactionHandler", transactionHandler);
		facade.makeTransition("entityType", 2L, "transitionId", "comment", "approvedAs",
				"assignedTo", null, null);
		assertTrue(requestPayload instanceof SingleTransitionEvent);
		verify(transactionHandler).registerTxHandler("entityType", 2L);
	}

	/**
	 *
	 */
	@Test
	public void testNoTxStartWorkflow() {
		ReflectionTestUtils.setField(facade, "transactionHandler", transactionHandler);
		reply.setValue(true);
		assertTrue(facade.startWorkflow("entityType", 1L, null));
		assertTrue(requestPayload instanceof StartWorkflowEvent);

		StartWorkflowEvent event = (StartWorkflowEvent) requestPayload;

		assertEquals("entityType", event.getType());
		assertEquals(1L, event.getId());
		verify(transactionHandler).registerTxHandler("entityType", 1L);
	}

	/**
	 *
	 */
	@Test
	public void testObtainHistory() {
		List<HistoryRecordDto> source = new ArrayList<HistoryRecordDto>();

		source.add(new HistoryRecordDto());
		reply.setValue(source);

		List<HistoryRecordDto> result = facade.obtainHistory(1L, "hoho");

		assertNotNull("Result is null", result);
		assertEquals(source, result);
	}

	/**
	 *
	 */
	@Test
	public void testObtainHistorySummary() {
		List<Map<String, Object>> source = new ArrayList<Map<String, Object>>();

		source.add(Collections.<String, Object>singletonMap("key", "value"));
		reply.setValue(source);

		List<Map<String, Object>> result = facade.obtainHistorySummary(1L, "applicationType");

		assertNotNull("Result is null", result);
		assertEquals(source, result);
	}
}
