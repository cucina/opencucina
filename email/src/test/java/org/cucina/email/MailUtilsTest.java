package org.cucina.email;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.cucina.email.service.MailUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision$
 */
public class MailUtilsTest {
    private static final String LINE_ENDING = "\n";
    private MimeMessage mimeMessage;

    /**
     * JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testInputIsNull()
        throws Exception {
        String ret = MailUtils.resolveSubjectAndBody(null, mimeMessage);

        assertNull("Should return null", ret);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSubjectAndBodyFunnyCase()
        throws Exception {
        String subject = " xxx ";
        String body = " yyyyy " + LINE_ENDING + "zzz" + LINE_ENDING + "y";

        String ret = MailUtils.resolveSubjectAndBody("   SuBject:" + subject + LINE_ENDING + body,
                mimeMessage);

        assertEquals(subject.trim(), mimeMessage.getSubject());
        assertEquals(body, ret);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSubjectAndBodySunnyDay()
        throws Exception {
        String subject = " xxx ";
        String body = " yyyyy " + LINE_ENDING + "zzz" + LINE_ENDING + "y";

        String ret = MailUtils.resolveSubjectAndBody("Subject:" + subject + LINE_ENDING + body,
                mimeMessage);

        assertEquals(subject.trim(), mimeMessage.getSubject());
        assertEquals(body, ret);
    }

    /**
     * JAVADOC.
     */
    @Test
    public void testSubjectAndBodyWhitespace()
        throws Exception {
        String subject = " xxx ";
        String body = " yyyyy " + LINE_ENDING + "zzz" + LINE_ENDING + "y";

        String ret = MailUtils.resolveSubjectAndBody("   Subject:" + subject + LINE_ENDING + body,
                mimeMessage);

        assertEquals(subject.trim(), mimeMessage.getSubject());
        assertEquals(body, ret);
        // change order
        ret = MailUtils.resolveSubjectAndBody("Subject:" + subject + LINE_ENDING + body, mimeMessage);

        assertEquals(subject.trim(), mimeMessage.getSubject());
        assertEquals(body, ret);
    }
}
