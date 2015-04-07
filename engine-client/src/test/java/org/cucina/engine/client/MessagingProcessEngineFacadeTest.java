package org.cucina.engine.client;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import org.cucina.engine.server.communication.HistoryRecordDto;
import org.cucina.engine.server.event.CommitSuccessEvent;
import org.cucina.engine.server.event.CompensateEvent;
import org.cucina.engine.server.event.RegistrationEvent;
import org.cucina.engine.server.event.workflow.BulkTransitionEvent;
import org.cucina.engine.server.event.workflow.SingleTransitionEvent;
import org.cucina.engine.server.event.workflow.StartWorkflowEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.mockito.MockitoAnnotations;

import org.mockito.invocation.InvocationOnMock;

import org.mockito.stubbing.Answer;


/**
 *
 *
 * @author vlevine
  */
public class MessagingProcessEngineFacadeTest {
    private static final String APPLICATION_NAME = "applicationName";
    @Mock
    private MessageChannel asyncChannel;
    @Mock
    private MessageChannel requestChannel;
    private MessagingProcessEngineFacade facade;
    private Object requestPayload;
    @Mock
    private SubscribableChannel replyChannel;
    private ValueEvent reply;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        facade = new MessagingProcessEngineFacade(APPLICATION_NAME, "myQueue", asyncChannel);
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
    @After
    public void cleanup() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
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
        Map<Serializable, Integer> entities = new HashMap<Serializable, Integer>();

        entities.put(3L, 20);
        facade.bulkTransition(entities, "applicationType", "transitionId", "comment", "approvedAs",
            "assignedTo", null, "reason", null);
        assertTrue(requestPayload instanceof BulkTransitionEvent);
    }

    /**
     *
     */
    @Test
    public void testNoTxMakeTransition() {
        facade.makeTransition("entityType", 2L, "transitionId", "comment", "approvedAs",
            "assignedTo", null, null);
        assertTrue(requestPayload instanceof SingleTransitionEvent);
    }

    /**
     *
     */
    @Test
    public void testNoTxStartWorkflow() {
        reply.setValue(true);
        assertTrue(facade.startWorkflow("entityType", 1L, null));
        assertTrue(requestPayload instanceof StartWorkflowEvent);

        StartWorkflowEvent event = (StartWorkflowEvent) requestPayload;

        assertEquals("entityType", event.getType());
        assertEquals(1L, event.getId());
        assertEquals(APPLICATION_NAME, event.getApplicationName());
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

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testOnApplicationEvent() {
        facade.onApplicationEvent(new ContextRefreshedEvent(mock(ApplicationContext.class)));

        ArgumentCaptor<Message> messc = ArgumentCaptor.forClass(Message.class);

        verify(asyncChannel).send(messc.capture());

        Message<?> message = messc.getValue();
        RegistrationEvent event = (RegistrationEvent) message.getPayload();

        assertEquals("jms://myQueue", event.getDestinationName());
        assertEquals(APPLICATION_NAME, event.getApplicationName());
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testTxFailStartWorkflow() {
        TransactionSynchronizationManager.initSynchronization();
        reply.setValue(true);
        assertTrue(facade.startWorkflow("entityType", 1L, null));
        TransactionSynchronizationUtils.triggerAfterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);

        ArgumentCaptor<Message> mac = ArgumentCaptor.forClass(Message.class);

        verify(asyncChannel).send(mac.capture());

        Message<?> m = mac.getValue();

        assertTrue(m.getPayload() instanceof CompensateEvent);

        CompensateEvent cse = (CompensateEvent) m.getPayload();

        Object id = cse.getIds()[0];

        assertEquals(1L, id);
        assertEquals("entityType", cse.getType());
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testTxStartWorkflow() {
        TransactionSynchronizationManager.initSynchronization();
        reply.setValue(true);
        assertTrue(facade.startWorkflow("entityType", 1L, null));
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
