package org.cucina.engine.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cucina.engine.service.ProcessSupportService;
import org.cucina.engine.testassist.Foo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 * Tests that UnrestrictedTransitionsAppender functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UnrestrictedTransitionsAppenderTest {
    private UnrestrictedTransitionsAppender appender;
    @Mock
    private ProcessSupportService workflowSupportService;

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        appender = new UnrestrictedTransitionsAppender(workflowSupportService);
    }

    /**
     * Tests transitions are appended.
     */
    @Test
    public void testAppendingTransitions() {
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

        Collection<String> transitions1 = new HashSet<String>();

        transitions1.add("mike");
        transitions1.add("mike2");

        Collection<String> transitions2 = new HashSet<String>();

        transitions2.add("fred");

        Map<Serializable, Collection<String>> transitionsById = new HashMap<Serializable, Collection<String>>();

        transitionsById.put(1L, transitions1);
        transitionsById.put(2L, transitions2);

        when(workflowSupportService.listAllTransitions(ids, Foo.TYPE)).thenReturn(transitionsById);

        appender.doModify(Foo.TYPE, results);

        assertEquals("incorrect transitions", transitions1,
            result1.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
        assertEquals("incorrect transitions", transitions2,
            result2.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
        assertNull("Shouldn't have transitions",
            result3.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
    }

    /**
     * Tests appending transitions when there are no results provided
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAppendingTransitionsNoResults() {
        Collection<Map<String, Object>> results = new HashSet<Map<String, Object>>();

        appender.doModify(Foo.TYPE, results);

        verify(workflowSupportService, never()).listAllTransitions(anyCollection(), anyString());
    }

    /**
     * Tests that no transitions are appended when no transitions for results
     */
    @Test
    public void testAppendingTransitionsNoTransitions() {
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

        when(workflowSupportService.listAllTransitions(ids, Foo.TYPE))
            .thenReturn(new HashMap<Serializable, Collection<String>>());

        appender.doModify(Foo.TYPE, results);

        assertNull("Shouldn't have transitions",
            result1.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
        assertNull("Shouldn't have transitions",
            result2.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
        assertNull("Shouldn't have transitions",
            result3.get(UnrestrictedTransitionsAppender.PROPERTY_NAME));
    }
}
