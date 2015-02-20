
package org.cucina.core.model.eclipselink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CsvMapConverterTest {
    private CsvMapConverter handler = new CsvMapConverter();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void isMutable() {
        assertTrue("Should be mutable", handler.isMutable());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void initialize() {
        AbstractDirectMapping mapping = mock(AbstractDirectMapping.class);

        when(mapping.isAbstractDirectMapping()).thenReturn(true);

        handler.initialize(mapping, null);

        verify(mapping).setFieldClassification(String.class);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void initializeNotAbstract() {
        DatabaseMapping mapping = mock(DatabaseMapping.class);

        when(mapping.isAbstractDirectMapping()).thenReturn(false);

        handler.initialize(mapping, null);
    }

    /**
     * When passes in non map returns String indicating that.
     */
    @Test
    public void testToDataValueNotMap() {
        assertEquals("Not a map", "Not a Map",
            handler.convertObjectValueToDataValue(new Object(), null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToDatasetValue()
        throws Exception {
        Map<String, String> bean = new LinkedHashMap<String, String>();

        bean.put("a", "1");
        bean.put("b", "2");

        assertEquals("Incorrect value", "a=1,b=2", handler.convertObjectValueToDataValue(bean, null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToDatasetValueEmpty()
        throws Exception {
        assertNull("Incorrect value",
            handler.convertObjectValueToDataValue(new HashMap<String, String>(), null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToDatasetValueNull()
        throws Exception {
        assertNull("Incorrect value", handler.convertObjectValueToDataValue(null, null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToObjectValue()
        throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> value = (Map<String, String>) handler.convertDataValueToObjectValue("a=1,b=2",
                null);

        assertNotNull("Should have generated value", value);
        assertEquals("Key not present or incorrect mapped value", "1", value.get("a"));
        assertEquals("Key not present or incorrect mapped value", "2", value.get("b"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testToObjectValueNull()
        throws Exception {
        assertNull("Shouldn't have generated value",
            handler.convertDataValueToObjectValue(null, null));
    }
}
