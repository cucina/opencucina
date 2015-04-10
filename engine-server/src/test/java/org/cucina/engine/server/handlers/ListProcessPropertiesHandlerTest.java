package org.cucina.engine.server.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.cucina.engine.server.event.workflow.ListProcessPropertiesEvent;
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
public class ListProcessPropertiesHandlerTest {
    private ListProcessPropertiesHandler handler;
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
        handler = new ListProcessPropertiesHandler(processSupportService);
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
        ListProcessPropertiesEvent event = new ListProcessPropertiesEvent();

        event.setApplicationType("applicationType");
        event.setIds(Arrays.<Serializable>asList(1, 2, 3));

        Collection<Map<String, Object>> value = new ArrayList<Map<String, Object>>();

        when(processSupportService.listWorkflowProperties(event.getIds(), "applicationType")).thenReturn(value);

        ValueEvent result = handler.getValue(event);

        assertEquals(value, result.getValue());
    }
}
