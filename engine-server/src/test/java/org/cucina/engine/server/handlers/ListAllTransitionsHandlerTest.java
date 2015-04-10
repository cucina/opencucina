package org.cucina.engine.server.handlers;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.workflow.ListAllTransitionsEvent;
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
public class ListAllTransitionsHandlerTest {
    private ListAllTransitionsHandler handler;
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
        handler = new ListAllTransitionsHandler(processSupportService);
    }

    /**
     *
     */
    @Test
    public void testGetValue() {
        ListAllTransitionsEvent event = new ListAllTransitionsEvent();

        event.setApplicationName("applicationName");

        Collection<Serializable> ids = new ArrayList<Serializable>();

        event.setIds(ids);

        Map<Serializable, Collection<String>> value = new HashMap<Serializable, Collection<String>>();

        when(processSupportService.listAllTransitions(ids, "applicationType")).thenReturn(value);

        ValueEvent result = handler.getValue(event);

        assertEquals(value, result.getValue());
    }
}
