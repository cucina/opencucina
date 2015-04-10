package org.cucina.engine.server.handlers;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.workflow.ListTransitionsEvent;
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
public class ListTransitionsHandlerTest {
    private ListTransitionsHandler handler;
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
        handler = new ListTransitionsHandler(processSupportService);
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
        ListTransitionsEvent event = new ListTransitionsEvent();

        event.setApplicationType("applicationType");
        event.setIds(Arrays.<Serializable>asList(1, 2, 3));

        Collection<String> value = Arrays.asList("a", "b");

        when(processSupportService.listTransitions(event.getIds(), "applicationType"))
            .thenReturn(value);

        ValueEvent result = handler.getValue(event);

        assertEquals(value, result.getValue());
    }
}
