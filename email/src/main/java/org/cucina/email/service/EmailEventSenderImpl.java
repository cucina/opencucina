package org.cucina.email.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;


/**
 * Crossover point between generating emails and sending them.
 *
 */
public class EmailEventSenderImpl
    implements EmailEventSender, ApplicationListener<EmailEvent> {
    private static Logger LOG = LoggerFactory.getLogger(EmailEventSenderImpl.class);
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
     * @see org.cucina.email.service.org.cucina.email.meringue.email.EmailEventSender#processEvent(org.cucina.email.service.meringue.email.EmailEvent)
     */
    public void processEvent(EmailEvent event) {
        EmailDescriptor descriptor = event.getEmailDescriptor();

        emailService.sendMessages(descriptor.getMessageKey(), wrapUsers(descriptor.getToUsers()),
            wrapUsers(descriptor.getCcUsers()), wrapUsers(descriptor.getBccUsers()),
            descriptor.getParameters(), descriptor.getAttachments());
    }

    private Collection<EmailUser> wrapUsers(Collection<Object> users) {
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
