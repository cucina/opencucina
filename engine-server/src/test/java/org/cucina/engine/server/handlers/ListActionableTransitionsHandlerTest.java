package org.cucina.engine.server.handlers;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.engine.definition.Transition;
import org.cucina.engine.server.event.workflow.ListActionableTransitionsEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;
import org.cucina.engine.service.ProcessSupportService;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
 */
public class ListActionableTransitionsHandlerTest {
    private ListActionableTransitionsHandler handler;
    @Mock
    private ProcessSupportService processSupportService;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new ListActionableTransitionsHandler(processSupportService);
    }

    /**
     *
     *
     * @param event .
     *
     * @return .
     */
    @Test
    public void testGetValue() {
        ListActionableTransitionsEvent event = new ListActionableTransitionsEvent();

        event.setWorkflowId("workflowId");

        Collection<Transition> value = new ArrayList<Transition>();

        when(processSupportService.listActionableTransitions("workflowId")).thenReturn(value);

        ValueEvent result = handler.getValue(event);

        assertEquals(value, result.getValue());
    }
}
