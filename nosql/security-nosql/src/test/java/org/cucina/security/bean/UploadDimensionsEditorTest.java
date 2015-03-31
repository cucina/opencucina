package org.cucina.security.bean;

import java.math.BigInteger;

import java.util.Collection;

import org.cucina.security.model.Dimension;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UploadDimensionsEditorTest {
    private UploadDimensionsEditor dimensionsEditor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        dimensionsEditor = new UploadDimensionsEditor();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testErrorCases() {
        dimensionsEditor.setAsText(null);
        assertNotNull(dimensionsEditor.getValue());

        String propertyName = "prop";
        String type = "type";

        dimensionsEditor.setAsText(propertyName);

        Collection<Dimension> dim = (Collection<Dimension>) dimensionsEditor.getValue();

        assertNotNull(dim);
        assertTrue(dim.isEmpty());

        dimensionsEditor.setAsText(propertyName + "=" + type);
        dim = (Collection<Dimension>) dimensionsEditor.getValue();
        assertNotNull(dim);
        assertTrue(dim.isEmpty());

        dimensionsEditor.setAsText(propertyName + "=" + type + "(b)");
        dim = (Collection<Dimension>) dimensionsEditor.getValue();
        assertNotNull(dim);
        assertTrue(dim.isEmpty());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSunnyDay() {
        String propertyName = "prop";
        String type = "type";

        dimensionsEditor.setAsText(propertyName + "=" + type + "(1)," + propertyName + "=" + type +
            "(2");

        @SuppressWarnings("unchecked")
        Collection<Dimension> dims = (Collection<Dimension>) dimensionsEditor.getValue();

        assertNotNull(dims);
        assertEquals(2, dims.size());

        for (Dimension di : dims) {
            assertEquals(di.getPropertyName(), propertyName);
            assertEquals(di.getDomainObjectType(), type);
            assertTrue((di.getDomainObjectId() == BigInteger.valueOf(1L)) ||
                (di.getDomainObjectId() == BigInteger.valueOf(2L)));
        }
    }
}
