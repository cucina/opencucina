package org.cucina.engine.server.handlers;

import java.util.HashMap;

import org.cucina.conversation.events.CommitEvent;
import org.cucina.conversation.events.ConversationEvent;

import org.cucina.engine.definition.Token;
import org.cucina.engine.server.event.StartWorkflowEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.service.ProcessSupportService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class StartWorkflowHandlerTest {
    @Mock
    private ProcessSupportService processSupportService;
    private StartWorkflowHandler handler;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new StartWorkflowHandler(processSupportService);
    }

    /**
     *
     */
    @Test
    public void testAct() {
        StartWorkflowEvent event = new StartWorkflowEvent();

        event.setType("type");
        event.setWorkflow("workflow");
        event.setId("id");
        event.setApplicationName("applicationName");
        event.setParameters(new HashMap<String, Object>());

        Token token = mock(Token.class);

        when(processSupportService.startWorkflow(any(), any(), any())).thenReturn(token);

        ConversationEvent result = handler.act(event);

        assertTrue(result instanceof CommitEvent);

        ArgumentCaptor<EntityDescriptor> acd = ArgumentCaptor.forClass(EntityDescriptor.class);

        verify(processSupportService)
            .startWorkflow(acd.capture(), eq(event.getWorkflow()), eq(event.getParameters()));

        EntityDescriptor ed = acd.getValue();

        assertEquals("type", ed.getApplicationType());
        assertEquals("applicationName", ed.getApplicationName());
        assertEquals("id", ed.getRemoteId());
    }
}
