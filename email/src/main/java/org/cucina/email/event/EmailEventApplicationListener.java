package org.cucina.email.event;

import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

import org.cucina.email.api.EmailDto;
import org.cucina.email.api.EmailEvent;
import org.cucina.email.service.AbstractEmailHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of ApplicationListener to handle events sent within the Spring
 * application.
 *
 */
public class EmailEventApplicationListener
    extends AbstractEmailHandler
    implements ApplicationListener<EmailEvent> {
    private static Logger LOG = LoggerFactory.getLogger(EmailEventApplicationListener.class);

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
            EmailDto dto = event.getEmailDescriptor();

            sendEmail(dto);
        } catch (Exception e) {
            LOG.error("Unexpected error", e);
        }
    }
}
