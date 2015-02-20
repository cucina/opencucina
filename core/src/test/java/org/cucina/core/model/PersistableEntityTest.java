
package org.cucina.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Test SpriteEntity functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PersistableEntityTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsFalseId() {
        SpriteEntityClass se1 = new SpriteEntityClass();

        se1.setId(12L);

        SpriteEntityClass se2 = new SpriteEntityClass();

        se2.setId(14L);

        assertFalse("Shouldn't be equals, different id", se1.equals(se2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsFalseType() {
        SpriteEntityClass se1 = new SpriteEntityClass();

        assertFalse("Shouldn't be equals, different types", se1.equals(new Object()));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsNullObject() {
        SpriteEntityClass se = new SpriteEntityClass();

        assertFalse("Shouldn't be equals, null object", se.equals(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsSameId() {
        SpriteEntityClass se1 = new SpriteEntityClass();

        se1.setId(12L);

        SpriteEntityClass se2 = new SpriteEntityClass();

        se2.setId(12L);

        assertTrue("Should be equals, same id", se1.equals(se2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEqualsSameObject() {
        SpriteEntityClass se = new SpriteEntityClass();

        assertTrue("Should be equals, same object", se.equals(se));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHashCodeId() {
        SpriteEntityClass se1 = new SpriteEntityClass();

        se1.setId(12L);

        assertEquals("should have repeatable hashcode", se1.hashCode(), se1.hashCode());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHashCodeNoId() {
        SpriteEntityClass se1 = new SpriteEntityClass();

        assertEquals("should have repeatable hashcode", se1.hashCode(), se1.hashCode());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIdNull() {
        SpriteEntityClass new1 = new SpriteEntityClass();
        SpriteEntityClass new2 = new SpriteEntityClass();

        assertFalse("should be different", new1.equals(new2));
        assertNotSame("should be different", new1.hashCode(), new2.hashCode());
    }
}


class SpriteEntityClass
    extends PersistableEntity {
    /**  */
    private static final long serialVersionUID = 1L;
}
