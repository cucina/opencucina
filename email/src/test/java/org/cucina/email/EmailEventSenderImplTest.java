package org.cucina.email;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataSource;

import org.cucina.email.service.EmailDescriptor;
import org.cucina.email.service.EmailEvent;
import org.cucina.email.service.EmailEventSenderImpl;
import org.cucina.email.service.EmailService;
import org.cucina.email.service.EmailUser;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link EmailEventSenderImpl}
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.5 $
 */
public class EmailEventSenderImplTest {
    private EmailEventSenderImpl els;

    /**
     * Sets up test
     */
    @Before
    public void setUp() {
        els = new EmailEventSenderImpl();
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
        Map<Object, Object> parameters = new HashMap<Object, Object>();
        Collection<DataSource> attachments = new HashSet<DataSource>();

        EmailDescriptor descriptor = new EmailDescriptor();

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
            .sendMessages(eq("template"), any(Collection.class), eq(ccs), eq(bccs), eq(parameters),
            eq(attachments));
    }
}
