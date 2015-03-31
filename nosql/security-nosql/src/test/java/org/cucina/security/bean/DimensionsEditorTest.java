package org.cucina.security.bean;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.cucina.security.model.Dimension;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DimensionsEditorTest {
    private static final String TEST = "[{\"id\":1,\"propertyName\":\"branch\",\"domainObjectId\": 55,\"domainObjectType\":\"Branch\"}]";
    private DimensionsEditor editor;
    @Mock
    private ObjectMapper om;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        editor = new DimensionsEditor();
        ReflectionTestUtils.setField(editor, "objectMapper", om);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSetAsTextString()
        throws Exception {
        Collection<Dimension> value = new HashSet<Dimension>();

        value.add(new Dimension());
        when(om.readValue(TEST, Collection.class)).thenReturn(value);
        editor.setAsText(TEST);

        @SuppressWarnings("unchecked")
        Collection<Dimension> dimensions = (Collection<Dimension>) editor.getValue();

        assertTrue("Should have 1 dimension", dimensions.size() == 1);
    }
}
