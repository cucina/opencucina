package org.cucina.loader;

import java.util.Collections;

import org.cucina.core.spring.integration.MessagePublisher;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class FileLoaderContainerHandler
    implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FileLoaderContainerHandler.class);
    private MessagePublisher messagePublisher;
    private Processor processor;

    /**
     * Creates a new FileLoaderContainerHandler object.
     *
     * @param processor JAVADOC.
     */
    public FileLoaderContainerHandler(Processor processor, MessagePublisher messagePublisher) {
        Assert.notNull(processor, "processor is null");
        this.processor = processor;
        Assert.notNull(messagePublisher, "messagePublisher is null");
        this.messagePublisher = messagePublisher;
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
        FileLoaderContainer container = (FileLoaderContainer) message.getPayload();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Arrived:" + container.getClass() + " : " + container);
        }

        processor.process(container);

        messagePublisher.publish(container.getUuid(),
            Collections.<String, Object>singletonMap(
                IntegrationMessageHeaderAccessor.CORRELATION_ID, this.toString()));
    }
}
