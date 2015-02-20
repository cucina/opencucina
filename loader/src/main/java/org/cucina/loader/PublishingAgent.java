
package org.cucina.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.spring.integration.MessagePublisher;
import org.cucina.loader.agent.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PublishingAgent
    implements Agent, ApplicationListener<FileLoaderAcknowledgementEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(PublishingAgent.class);
    private Collection<String> loadedTypes;
    private Collection<UUID> requests = Collections.synchronizedCollection(new ArrayList<UUID>());
    private FileLoader fileLoader;
    private MessagePublisher messagePublisher;
    private Object monitor = new Object();

    /**
     * Creates a new QueueingExecutor object.
     *
     * @param fileLoader JAVADOC.
     */
    public PublishingAgent(FileLoader fileLoader, MessagePublisher messagePublisher) {
        Assert.notNull(fileLoader, "fileLoader is null");
        this.fileLoader = fileLoader;
        Assert.notNull(messagePublisher, "messagePublisher is null");
        this.messagePublisher = messagePublisher;
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void execute() {
        FileLoaderContainer flc;

        loadedTypes = new HashSet<String>();

        long count = 0;

        while ((flc = fileLoader.next()) != null) {
            UUID uuid = UUID.randomUUID();

            flc.setUuid(uuid);
            requests.add(uuid);
            messagePublisher.publish(flc);
            count++;
            loadedTypes.add(flc.getApplicationType());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Completed loading file with " + count + " items of types '" + loadedTypes +
                "'");
        }

        long previous = count;

        while (!requests.isEmpty()) {
            synchronized (monitor) {
                try {
                    monitor.wait(10000);

                    if (previous == requests.size()) {
                        // TODO perhaps have few attempts?
                        LOG.warn("Had waited for more than 10 secs for types '" + loadedTypes +
                            "'");

                        break;
                    }
                } catch (InterruptedException e) {
                    LOG.error("Oops", e);

                    break;
                }
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     */
    @Override
    public void onApplicationEvent(FileLoaderAcknowledgementEvent event) {
        if (requests.remove(event.getSource())) {
            synchronized (monitor) {
                monitor.notify();
            }
        }
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("loadedTypes", loadedTypes).toString();
    }
}
