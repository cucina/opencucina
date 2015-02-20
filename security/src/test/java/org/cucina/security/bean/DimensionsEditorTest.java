
package org.cucina.security.bean;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.security.model.Dimension;
import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DimensionsEditorTest {
    private DimensionsEditor editor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        Marshaller marshaller = new JacksonMarshaller(null, null);

        editor = new DimensionsEditor(marshaller);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSetAsTextString() {
        editor.setAsText(
            "[{\"id\":1,\"propertyName\":\"branch\",\"domainObjectId\": 55,\"domainObjectType\":\"Branch\"}]");

        @SuppressWarnings("unchecked")
        Collection<Dimension> dimensions = (Collection<Dimension>) editor.getValue();

        assertTrue("Should have 1 dimension", dimensions.size() == 1);
    }
}
