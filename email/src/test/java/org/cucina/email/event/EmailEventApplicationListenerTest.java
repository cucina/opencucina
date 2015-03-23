package org.cucina.email.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.email.api.EmailDto;
import org.cucina.email.api.EmailEvent;
import org.cucina.email.service.EmailService;
import org.cucina.email.service.EmailUser;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;

import org.mockito.MockitoAnnotations;


/**
 * Tests for {@link EmailEventApplicationListener}
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.5 $
 */
public class EmailEventApplicationListenerTest {
    private EmailEventApplicationListener els;
    @Mock
    private EmailService ces;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        els = new EmailEventApplicationListener();
        ReflectionTestUtils.setField(els, "emailService", ces);
    }

    /**
     * Test for processEvent
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testProcessEvent() {
        Map<String, Object> parameters = new HashMap<String, Object>();

        EmailDto dto = new EmailDto();

        dto.setParameters(parameters);
        dto.setMessageKey("template");
        dto.setBcc("bcc");
        dto.setCc("cc");
        dto.setTo("to1, to2");
        dto.setLocale(Locale.CANADA.toString());

        els.onApplicationEvent(new EmailEvent(dto));

        ArgumentCaptor<Collection> tos = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Collection> ccs = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Collection> bccs = ArgumentCaptor.forClass(Collection.class);

        verify(ces)
            .sendMessages(eq(dto.getSubject()), eq(dto.getFrom()), tos.capture(), ccs.capture(),
            bccs.capture(), eq(dto.getMessageKey()), eq(dto.getParameters()),
            (Collection<DataSource>) eq(null));

        Collection<?> tou = tos.getValue();
        Iterator<?> it = tou.iterator();
        EmailUser u1 = (EmailUser) it.next();

        assertEquals("to1", u1.getEmail());
        assertEquals(Locale.CANADA, u1.getLocale());

        u1 = (EmailUser) it.next();

        assertEquals("to2", u1.getEmail());
        assertEquals(Locale.CANADA, u1.getLocale());

        tou = ccs.getValue();
        it = tou.iterator();
        u1 = (EmailUser) it.next();
        assertEquals("cc", u1.getEmail());
        assertEquals(Locale.CANADA, u1.getLocale());

        tou = bccs.getValue();
        it = tou.iterator();
        u1 = (EmailUser) it.next();
        assertEquals("bcc", u1.getEmail());
        assertEquals(Locale.CANADA, u1.getLocale());
    }
}
