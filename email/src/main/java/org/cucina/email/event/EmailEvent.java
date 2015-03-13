
package org.cucina.email.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;


/**
 * Spring ApplicationEvent which contains {@link EmailWithAttachmentDto}.
 *
 * @author $author$
 * @version $Revision$
 * TODO convert to use spring integration
 */
public class EmailEvent
    extends ApplicationEvent {
    private static final long serialVersionUID = 10L;
    private EmailWithAttachmentDto emailDescriptor;

    /**
     * Creates a new EmailEvent object.
     *
     * @param emailDescriptor
     *            EmailDescriptor.
     */
    public EmailEvent(EmailWithAttachmentDto emailDescriptor) {
        super(emailDescriptor);
        this.emailDescriptor = emailDescriptor;
    }

    /**
     * Get emailDescriptor.
     *
     * @return emailDescriptor EmailDescriptor.
     */
    public EmailWithAttachmentDto getEmailDescriptor() {
        return emailDescriptor;
    }

    /**
     * toString implementation which uses {@link ToStringBuilder}.
     *
     * @return toString.
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
