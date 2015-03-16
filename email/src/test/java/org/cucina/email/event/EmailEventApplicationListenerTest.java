package org.cucina.email.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import org.cucina.email.event.EmailEvent;
import org.cucina.email.event.EmailEventApplicationListener;
import org.cucina.email.event.EmailWithAttachmentDto;
import org.cucina.email.service.EmailService;
import org.cucina.email.service.EmailUser;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EmailEventApplicationListener}
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.5 $
 */
public class EmailEventApplicationListenerTest {
    private EmailEventApplicationListener els;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        els = new EmailEventApplicationListener();
    }

    /**
     * Test for processEvent
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testProcessEvent() {
        Collection tos = new HashSet();
        EmailUser u1 = mock(EmailUser.class);

        tos.add(u1);

        EmailUser u2 = mock(EmailUser.class);

        when(u2.getLocale()).thenReturn(Locale.SIMPLIFIED_CHINESE);
        tos.add(u2);

        Collection ccs = new HashSet();
        Collection bccs = new HashSet();
        Map<String, Object> parameters = new HashMap<String, Object>();
        Collection<DataSource> attachments = new HashSet<DataSource>();

        EmailWithAttachmentDto descriptor = new EmailWithAttachmentDto();

        descriptor.setToUsers(tos);
        descriptor.setCcUsers(ccs);
        descriptor.setBccUsers(bccs);
        descriptor.setParameters(parameters);
        descriptor.setAttachments(attachments);
        descriptor.setMessageKey("template");

        EmailService ces = mock(EmailService.class);

        els.setEmailService(ces);
        els.processEvent(new EmailEvent(descriptor));

        verify(ces)
            .sendMessages((String) eq(null), (String) eq(null), any(Collection.class), eq(ccs),
            eq(bccs), eq("template"), eq(parameters), eq(attachments));
    }
}
