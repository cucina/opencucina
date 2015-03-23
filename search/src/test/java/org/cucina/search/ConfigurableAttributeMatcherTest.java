package org.cucina.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ConfigurableAttributeMatcherTest {
    private ConfigurableAttributeMatcher matcher;

    /**
     * Setup a new instance.
     */
    @Before
    public void setup() {
        matcher = new ConfigurableAttributeMatcher();
    }

    /**
     * Test that if there's a restriction value but no attribute value then
     * it cannot match.
     */
    @Test
    public void testNoAttributeValue() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        Map<String, Object> restrictions = new HashMap<String, Object>();

        attributes.put("A", "hello");

        restrictions.put("A", "hello");
        restrictions.put("B", new ArrayList<String>(Arrays.asList("B")));
        restrictions.putAll(attributes);

        AttributeRestrictor restrictor = mock(AttributeRestrictor.class);

        matcher.setAttributeRestrictors(Arrays.asList(restrictor));

        assertFalse("Wrong value returned", matcher.match(attributes, restrictions));
    }

    /**
     * Test that when there are restrictions but no attributes then we do NOT when a
     * match.
     */
    @Test
    public void testNoAttributes() {
        Map<String, Object> restrictions = new HashMap<String, Object>();

        restrictions.put("A", "hello");
        restrictions.put("B", new ArrayList<String>(Arrays.asList("B")));

        AttributeRestrictor restrictor = mock(AttributeRestrictor.class);

        matcher.setAttributeRestrictors(Arrays.asList(restrictor));

        assertFalse("Wrong value returned", matcher.match(null, restrictions));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoRestrictions() {
        Map<String, Object> attributes = new HashMap<String, Object>();

        attributes.put("A", "hello");

        AttributeRestrictor restrictor = mock(AttributeRestrictor.class);

        matcher.setAttributeRestrictors(Arrays.asList(restrictor));

        assertTrue("Wrong value returned", matcher.match(attributes, null));
        verifyZeroInteractions(restrictor);
    }

    /**
     * Test that we return false if any of the restrictions fail.
     */
    @Test
    public void testRainyDay() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        Map<String, Object> restrictions = new HashMap<String, Object>();

        attributes.put("A", "hello");
        attributes.put("B", new ArrayList<String>(Arrays.asList("B")));
        restrictions.putAll(attributes);

        AttributeRestrictor restrictor = mock(AttributeRestrictor.class);

        when(restrictor.supports(String.class)).thenReturn(false);
        when(restrictor.supports(ArrayList.class)).thenReturn(true);
        when(restrictor.test(attributes.get("B"), restrictions.get("B"))).thenReturn(false);
        matcher.setAttributeRestrictors(Arrays.asList(restrictor));

        assertFalse("Wrong value returned", matcher.match(attributes, restrictions));
    }

    /**
     * Test that we return true if all restrictions are matched.
     */
    @Test
    public void testSunnyDay() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        Map<String, Object> restrictions = new HashMap<String, Object>();

        attributes.put("A", "hello");
        attributes.put("B", new ArrayList<String>(Arrays.asList("B")));
        restrictions.putAll(attributes);

        AttributeRestrictor restrictor = mock(AttributeRestrictor.class);

        when(restrictor.supports(String.class)).thenReturn(false);
        when(restrictor.supports(ArrayList.class)).thenReturn(true);
        when(restrictor.test(attributes.get("B"), restrictions.get("B"))).thenReturn(true);
        matcher.setAttributeRestrictors(Arrays.asList(restrictor));

        assertTrue("Wrong value returned", matcher.match(attributes, restrictions));
    }
}
