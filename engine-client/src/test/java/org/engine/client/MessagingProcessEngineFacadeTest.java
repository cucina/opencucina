package org.engine.client;

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

import org.cucina.engine.server.communication.HistoryRecordDto;
import org.cucina.engine.server.event.RegistrationEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;
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
    @Mock
    private MessageChannel asyncChannel;
    @Mock
    private MessageChannel requestChannel;
    private MessagingProcessEngineFacade facade;
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
        facade = new MessagingProcessEngineFacade("applicationName", "myQueue", asyncChannel);
        facade.setRequestChannel(requestChannel);
        facade.setReplyChannel(replyChannel);
        reply = new ValueEvent(this);
        doAnswer(new Answer<Boolean>() {
                @Override
                public Boolean answer(InvocationOnMock invocation)
                    throws Throwable {
                    Message<ValueEvent> message = MessageBuilder.withPayload(reply).build();
                    MessageChannel replyChannel = (MessageChannel) ((Message<?>) invocation
                                                                    .getArguments()[0]).getHeaders()
                                                                    .getReplyChannel();

                    replyChannel.send(message);

                    return true;
                }
            }).when(requestChannel).send(any(Message.class), anyLong());
    }

    /**
     *
     */
    @Test
    public void testBulkTransition() {
        Map<Long, Integer> entities = new HashMap<Long, Integer>();

        entities.put(3L, 20);
        facade.bulkTransition(entities, "applicationType", "transitionId", "comment", "approvedAs",
            "assignedTo", null, "reason", null);
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

        Collection<Long> ids = Collections.singleton(100L);
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

        Collection<Long> ids = Collections.singleton(100L);
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
    public void testMakeTransition() {
        facade.makeTransition("applicationType", 2L, "transitionId", "comment", "approvedAs",
            "assignedTo", null, null);
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
        assertEquals("applicationName", event.getApplicationName());
    }

    /**
     *
     */
    @Test
    public void testStartWorkflow() {
        reply.setValue(true);
        assertTrue(facade.startWorkflow("entityType", 1L, null));
    }
}
