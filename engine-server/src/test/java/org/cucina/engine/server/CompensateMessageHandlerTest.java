package org.cucina.engine.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.messaging.Message;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.server.event.CompensateEvent;
import org.cucina.engine.server.testassist.Foo;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

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

        WorkflowToken token = mock(WorkflowToken.class);
        HistoryRecord hr = new HistoryRecord();

        List<HistoryRecord> histories = new ArrayList<HistoryRecord>();

        histories.add(hr);

        when(token.getHistories()).thenReturn(histories);

        Collection<WorkflowToken> tokens = Collections.singleton(token);

        when(tokenRepository.loadTokens(eq("Foo"), any(Long[].class))).thenReturn(tokens);

        Message<CompensateEvent> message = mock(Message.class);

        when(message.getPayload()).thenReturn(event);
        handler.handleMessage(message);
        verify(tokenRepository).update(token);
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

        WorkflowToken token = mock(WorkflowToken.class);
        Foo foo = new Foo();

        when(token.getDomainObject()).thenReturn(foo);

        Collection<WorkflowToken> tokens = Collections.singleton(token);

        when(tokenRepository.loadTokens(eq("Foo"), any(Long[].class))).thenReturn(tokens);

        Message<CompensateEvent> message = mock(Message.class);

        when(message.getPayload()).thenReturn(event);
        handler.handleMessage(message);
        verify(tokenRepository).deleteDeep(token);
    }
}