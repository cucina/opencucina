package org.cucina.engine.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cucina.engine.service.ProcessSupportService;
import org.cucina.engine.testassist.Foo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;

import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Tests that WorkflowPropertiesAppender functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowPropertiesAppenderTest {
    private WorkflowPropertiesAppender appender;
    @Mock
    private ProcessSupportService workflowSupportService;

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        appender = new WorkflowPropertiesAppender(workflowSupportService);
    }

    /**
     * Tests that no workflow properties are appended when no workflow properties for id
     */
    @Test
    public void testAppendingNoProperties() {
        Map<String, Object> result1 = new HashMap<String, Object>();

        result1.put("id", 1L);

        Map<String, Object> result2 = new HashMap<String, Object>();

        result2.put("id", 2L);

        Map<String, Object> result3 = new HashMap<String, Object>();

        Collection<Map<String, Object>> results = new HashSet<Map<String, Object>>();

        results.add(result1);
        results.add(result2);
        results.add(result3);

        Collection<Serializable> ids = new HashSet<Serializable>();

        ids.add(1L);
        ids.add(2L);

        when(workflowSupportService.listWorkflowProperties(ids, Foo.TYPE))
            .thenReturn(new HashSet<Map<String, Object>>());

        appender.doModify(Foo.TYPE, results);

        assertEquals("Incorrect size, shouldn't have added props", 1, result1.size());
        assertEquals("Incorrect size, shouldn't have added props", 1, result2.size());
        assertEquals("Incorrect size, shouldn't have added props", 0, result3.size());
    }

    /**
     * Tests workflow properties are appended.
     */
    @Test
    public void testAppendingWorkflowProperties() {
        Map<String, Object> result1 = new HashMap<String, Object>();

        result1.put("id", 1L);

        Map<String, Object> result2 = new HashMap<String, Object>();

        result2.put("id", 2L);

        Map<String, Object> result3 = new HashMap<String, Object>();

        Collection<Map<String, Object>> results = new HashSet<Map<String, Object>>();

        results.add(result1);
        results.add(result2);
        results.add(result3);

        Collection<Serializable> ids = new HashSet<Serializable>();

        ids.add(1L);
        ids.add(2L);

        Map<String, Object> workflowProps1 = new HashMap<String, Object>();

        workflowProps1.put("id", 1L);
        workflowProps1.put("history", "blah");
        workflowProps1.put("assigner", "mikeyt");

        Map<String, Object> workflowProps2 = new HashMap<String, Object>();

        workflowProps2.put("id", 2L);
        workflowProps2.put("assigner", "fred");

        Collection<Map<String, Object>> workflowPropRows = new HashSet<Map<String, Object>>();

        workflowPropRows.add(workflowProps1);
        workflowPropRows.add(workflowProps2);

        when(workflowSupportService.listWorkflowProperties(ids, Foo.TYPE))
            .thenReturn(workflowPropRows);

        appender.doModify(Foo.TYPE, results);

        assertEquals("Incorrect size, should have added props", 3, result1.size());
        assertEquals("incorrect value", 1L, result1.get("id"));
        assertEquals("incorrect value", "blah", result1.get("history"));
        assertEquals("incorrect value", "mikeyt", result1.get("assigner"));

        assertEquals("Incorrect size, should have added props", 2, result2.size());
        assertEquals("incorrect value", 2L, result2.get("id"));
        assertEquals("incorrect value", "fred", result2.get("assigner"));

        assertEquals("Incorrect size, shouldn't have added props", 0, result3.size());
    }

    /**
     * Tests appending workflow properties when there are no results provided
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAppendingWorkflowPropertiesNoResults() {
        Collection<Map<String, Object>> results = new HashSet<Map<String, Object>>();

        appender.doModify(Foo.TYPE, results);

        verify(workflowSupportService, never()).listWorkflowProperties(anyCollection(), anyString());
    }
}
