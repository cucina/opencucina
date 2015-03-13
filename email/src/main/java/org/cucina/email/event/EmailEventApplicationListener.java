package org.cucina.email.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.email.service.AbstractEmailHandler;
import org.cucina.email.service.EmailService;
import org.cucina.email.service.EmailUser;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of ApplicationListener to handle events sent within the Spring
 * application.
 *
 */
public class EmailEventApplicationListener extends AbstractEmailHandler
    implements  ApplicationListener<EmailEvent> {
    private static Logger LOG = LoggerFactory.getLogger(EmailEventApplicationListener.class);
    private EmailService emailService;

    /**
     * JAVADOC.
     *
     * @param emailConstructor
     *            JAVADOC.
     */
    @Required
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event
     *            JAVADOC.
     */
    @Override
    @Transactional
    public void onApplicationEvent(EmailEvent event) {
        try {
            processEvent(event);
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
        }
    }

    /**
     * @param event
     *            JAVADOC.
     * @see org.cucina.email.spring.org.cucina.email.meringue.email.EmailEventSender#processEvent(org.cucina.email.spring.meringue.email.EmailEvent)
     */
    public void processEvent(EmailEvent event) {
        EmailWithAttachmentDto dto = event.getEmailDescriptor();

        emailService.sendMessages(dto.getSubject(), dto.getFrom(),  wrapUsers(dto.getToUsers()),
            wrapUsers(dto.getCcUsers()), wrapUsers(dto.getBccUsers()),dto.getMessageKey(),
            dto.getParameters(), dto.getAttachments());
    }

    private Collection<EmailUser> wrapUsers(Collection<EmailUser> users) {
        Collection<EmailUser> wrappedUsers = new HashSet<EmailUser>();

        if (CollectionUtils.isNotEmpty(users)) {
            for (Object obj : users) {
                if (obj instanceof EmailUser) {
                    EmailUser user = (EmailUser) obj;

                    if (user.getLocale() == null) {
                        // for some reasons the locale was not set for the user,
                        // set system default
                        Locale locale = Locale.getDefault();

                        wrappedUsers.add(new EmailUserWrapper(user, locale));
                    } else {
                        wrappedUsers.add(user);
                    }
                } else {
                    LOG.warn("Unexpected non User of type [" + obj.getClass().getSimpleName() +
                        "], ignoring");
                }
            }
        }

        return wrappedUsers;
    }
}
