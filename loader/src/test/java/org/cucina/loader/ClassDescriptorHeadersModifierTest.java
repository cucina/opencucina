
package org.cucina.loader;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ClassDescriptorHeadersModifierTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testModifyHeaders() {
        ClassDescriptorHeadersModifier modifier = new ClassDescriptorHeadersModifier();

        String[] headers = modifier.modifyHeaders(new String[] { "nick", "cost" }, A.class);

        assertEquals("name", headers[0]);
        assertEquals("value", headers[1]);
    }

    public static class A {
        @LoaderColumnLookup(propertyAlias = "nick")
        private String name;
        @LoaderColumnLookup(propertyAlias = "cost")
        private String value;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
