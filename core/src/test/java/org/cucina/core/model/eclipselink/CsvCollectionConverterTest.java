
package org.cucina.core.model.eclipselink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CsvCollectionConverterTest {
    @Mock
    private AbstractDirectMapping mapping;
    private CsvCollectionConverter converter;
    @Mock
    private Session session;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        converter = new CsvCollectionConverter();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testConvertDataValueToObjectValue() {
        Object result = converter.convertDataValueToObjectValue("a, b,c", session);

        assertTrue("Not an instanceof Collection", result instanceof Collection<?>);

        @SuppressWarnings("unchecked")
        Collection<String> sr = (Collection<String>) result;

        assertEquals(3, sr.size());
        assertTrue("No a", sr.contains("a"));
        assertTrue("No b", sr.contains("b"));
        assertTrue("No c", sr.contains("c"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testConvertDataValueToObjectValueNull() {
        assertNull("Shouldn't have converted anything",
            converter.convertDataValueToObjectValue(null, session));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testConvertObjectValueToDataValue() {
        Collection<String> coll = new ArrayList<String>();

        coll.add("a");
        coll.add("b");
        coll.add("a");

        Object result = converter.convertObjectValueToDataValue(coll, session);

        assertNotNull("result is null", result);

        String sr = (String) result;

        assertEquals("a,b,a", sr);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testConvertObjectValueToDataValueNotCollection() {
        assertEquals("Shouldn't have converted", "Not a Collection",
            converter.convertObjectValueToDataValue(new Object(), session));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testConvertObjectValueToDataValueNull() {
        assertNull("Shouldn't have converted anything",
            converter.convertObjectValueToDataValue(null, session));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testInitialize() {
        when(mapping.isAbstractDirectMapping()).thenReturn(true);

        converter.initialize(mapping, session);
        verify(mapping).setFieldClassification(String.class);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsMutable() {
        assertTrue(converter.isMutable());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void toStringNull() {
        assertNull(CsvCollectionConverter.toString(null));
    }
}
