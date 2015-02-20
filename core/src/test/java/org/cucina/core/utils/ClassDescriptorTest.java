package org.cucina.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import javax.validation.groups.Default;

import org.cucina.core.model.Attachment;
import org.cucina.core.model.projection.ExternalProjectionColumn;
import org.cucina.core.model.projection.ExternalProjectionColumns;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.model.projection.ProjectionColumns;
import org.cucina.core.testassist.Foo;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClassDescriptorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetAdditionalAliases() {
        Map<String, String> result = ClassDescriptor.getAdditionalAliases(Annoited.class);

        assertNotNull("result is null", result);
        assertEquals("Incorrect number results", 0, result.size());

        result = ClassDescriptor.getAdditionalAliases(Annoited.class, TestGroup.class);
        assertNotNull("result is null", result);
        assertEquals("Incorrect number results", 1, result.size());
        assertEquals("attachment", result.get(Attachment.class.getSimpleName()));
    }

    /**
     * JAVADOC Method Level Comments
     */

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetPropertyTypeClassOfQString() {
        assertEquals("String", ClassDescriptor.getPropertyType(Foo.class, "name"));
        assertNull(ClassDescriptor.getPropertyType(Foo.class, "xxx"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsCollectionProperty() {
        assertFalse(ClassDescriptor.isCollectionProperty(Annoited.class, "value"));
        assertTrue(ClassDescriptor.isCollectionProperty(Annoited.class, "strings"));
        assertFalse(ClassDescriptor.isCollectionProperty(Annoited.class, "imaginary"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testPostProcess() {
        assertTrue(ClassDescriptor.isPostProcessProjections(Annoited.class));
        assertFalse(ClassDescriptor.isPostProcessProjections(NonAnnoited.class));
    }

    public interface TestGroup
        extends Default {
    }

    /**
     * JAVADOC for Class Level
     *
     * @author $Author: $
     * @version $Revision: $
     */
    @ExternalProjectionColumns(value =  {
        @ExternalProjectionColumn(columnName = "lastDate", property = "lastDate")
    }
    , fieldName = "attachment", clazz = Attachment.class, groups = TestGroup.class)
    @PostProcessProjections
    public class Annoited {
        private Collection<String> strings;
        private String name;
        private double value;
        private int number;

        /**
         * JAVADOC Method Level Comments
         *
         * @param name JAVADOC.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        @ProjectionColumns({@ProjectionColumn(columnName = "short name")
            , @ProjectionColumn(columnName = "bytes", property = "bytes")
        })
        public String getName() {
            return name;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @param number JAVADOC.
         */
        public void setNumber(int number) {
            this.number = number;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public int getNumber() {
            return number;
        }

        public void setStrings(Collection<String> strings) {
            this.strings = strings;
        }

        public Collection<String> getStrings() {
            return strings;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @param value JAVADOC.
         */
        public void setValue(double value) {
            this.value = value;
        }

        /**
         * JAVADOC Method Level Comments
         *
         * @return JAVADOC.
         */
        public double getValue() {
            return value;
        }
    }

    public class NonAnnoited {
    }
}
