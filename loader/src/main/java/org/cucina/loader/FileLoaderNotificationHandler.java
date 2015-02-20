
package org.cucina.loader;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */

// TODO - publish applicationEvent or to a channel?
// TODO convert to integration splitter
public class FileLoaderNotificationHandler
    implements MessageHandler, ApplicationEventPublisherAware {
    private static final Logger LOG = LoggerFactory.getLogger(FileLoaderNotificationHandler.class);
    private ApplicationEventPublisher publisher;

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationEventPublisher
     *            JAVADOC.
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @throws MessagingException JAVADOC.
     */
    @Override
    public void handleMessage(Message<?> message)
        throws MessagingException {
        Object obj = message.getPayload();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Execute with " + obj);
        }

        if (obj instanceof Collection) {
            Collection<?> coll = (Collection<?>) obj;

            for (Object object : coll) {
                processItem(object);
            }
        } else {
            processItem(obj);
        }
    }

    private void processItem(Object obj) {
        if (obj instanceof UUID) {
            publisher.publishEvent(new FileLoaderAcknowledgementEvent((UUID) obj));
        } else if (obj instanceof String) {
            publisher.publishEvent(new FileLoaderAcknowledgementEvent(UUID.fromString((String) obj)));
        } else {
            LOG.warn("Not a UUID:" + obj.getClass());
        }
    }
}
