package org.cucina.engine.server.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.workflow.LoadTransitionInfoEvent;
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
@Component
public class LoadTransitionsInfoHandlerTest {
    private LoadTransitionsInfoHandler handler;
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
        handler = new LoadTransitionsInfoHandler(processSupportService);
    }

    /**
     *
     */
    @Test
    public void testGetValue() {
        LoadTransitionInfoEvent event = new LoadTransitionInfoEvent();

        event.setWorkflowId("workflowId");

        Collection<Map<String, String>> value = new ArrayList<Map<String, String>>();

        when(processSupportService.loadTransitionInfo("workflowId")).thenReturn(value);

        ValueEvent result = handler.getValue(event);

        assertEquals(value, result.getValue());
    }
}
