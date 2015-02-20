
package org.cucina.security.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.cucina.core.InstanceFactory;
import org.cucina.security.model.Dimension;
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
public class UploadDimensionsEditorTest {
    @Mock
    private InstanceFactory instanceFactory;
    private UploadDimensionsEditor dimensionsEditor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        when(instanceFactory.getBean(Dimension.class.getSimpleName())).thenReturn(new Dimension());
        dimensionsEditor = new UploadDimensionsEditor(instanceFactory);
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
            assertTrue((di.getDomainObjectId() == 1L) || (di.getDomainObjectId() == 2L));
        }
    }
}
