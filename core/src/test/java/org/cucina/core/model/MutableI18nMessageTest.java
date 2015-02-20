
package org.cucina.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


/**
 * Test SpriteRtceEntity fucntions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MutableI18nMessageTest {
    private Message message1;
    private Message message2;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        message1 = new Message();
        message1.setId(12L);
        message2 = new Message();
        message2.setId(14L);
    }

    /**
     * Different types compared. Result: Unequal.
     */
    @Test
    public void testEqualsFalseType() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        assertFalse("Shouldn't be equals, different types", se1.equals(new Object()));
    }

    /**
     * null object. Result: Unequal.
     */
    @Test
    public void testEqualsNullObject() {
        MutableI18nMessage se = new MutableI18nMessage();

        assertFalse("Shouldn't be equals, same object", se.equals(null));
    }

    /**
     * Same message. Result: Equal.
     */
    @Test
    public void testEqualsSameMessage() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setMessage(message1);

        assertTrue("Should be equals, same message", se1.equals(se2));
    }

    /**
     * Same message and locale. Result: Equal.
     */
    @Test
    public void testEqualsSameMessageAndLocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());
        se2.setMessage(message1);

        assertTrue("Should be equals, same message and locale", se1.equals(se2));
    }

    /**
     * Same object. Result: Equal.
     */
    @Test
    public void testEqualsSameObject() {
        MutableI18nMessage se = new MutableI18nMessage();

        assertTrue("Should be equals, same object", se.equals(se));
    }

    /**
     * Same locale. Result: Equal.
     */
    @Test
    public void testEqualsSamelocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());

        assertTrue("Should be equals, same locale", se1.equals(se2));
    }

    /**
     * Locale the same. Result: Equal hashcode.
     */
    @Test
    public void testHashCodeLocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());

        assertEquals("should have repeatable hashcode", se1.hashCode(), se2.hashCode());
    }

    /**
     * Rtce ids are different and there is no id set. Result: Diff hashcode.
     */
    @Test
    public void testHashCodeLocaleDiff() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.FRENCH.toString());
        se2.setMessage(message1);

        assertNotSame("should have different hashcode", se1.hashCode(), se2.hashCode());
    }

    /**
     * Messages and locale are the same. Result: Equal hashcode.
     */
    @Test
    public void testHashCodeMessageAndLocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());
        se2.setMessage(message1);

        assertEquals("should have repeatable hashcode", se1.hashCode(), se2.hashCode());
    }

    /**
     * Messages are different and locale are the same. Result: Diff hashcode.
     */
    @Test
    public void testHashCodeMessageDiff() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());
        se2.setMessage(message2);

        assertNotSame("should have different hashcode", se1.hashCode(), se2.hashCode());
    }

    /**
     * Nothing set. Result: Same hashcode.
     */
    @Test
    public void testHashCodeNoIdOrRtceId() {
        MutableI18nMessage se1 = new MutableI18nMessage();
        MutableI18nMessage se2 = new MutableI18nMessage();

        assertEquals("should have repeatable hashcode", se1.hashCode(), se2.hashCode());
    }

    /**
     * Diff locale. Result: Not Equal.
     */
    @Test
    public void testNotEqualsDiffMessageSameLocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.ENGLISH.toString());
        se2.setMessage(message2);

        assertFalse("Should not be equals, different message", se1.equals(se2));
    }

    /**
     * Diff message. Result: Not Equal.
     */
    @Test
    public void testNotEqualsSameMessageDiffLocale() {
        MutableI18nMessage se1 = new MutableI18nMessage();

        se1.setLocaleCd(Locale.ENGLISH.toString());
        se1.setMessage(message1);

        MutableI18nMessage se2 = new MutableI18nMessage();

        se2.setLocaleCd(Locale.FRENCH.toString());
        se2.setMessage(message1);

        assertFalse("Should not be equals, different locale", se1.equals(se2));
    }
}
