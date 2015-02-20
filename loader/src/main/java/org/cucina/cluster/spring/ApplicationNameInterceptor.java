package org.cucina.cluster.spring;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.util.Assert;

import org.cucina.cluster.event.ClusterControlEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ApplicationNameInterceptor
    extends ChannelInterceptorAdapter {
    private String applicationName;

    /**
     * Creates a new ApplicationNameClusterEventPopulator object.
     */
    public ApplicationNameInterceptor(String applicationName) {
        Assert.notNull(applicationName, "applicationName (application.name) is null");
        Assert.hasLength(applicationName, "applicationName (application.name) is empty");
        this.applicationName = applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     * @param channel JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        Object payloadObject = message.getPayload();

        if (payloadObject instanceof ClusterControlEvent) {
            ClusterControlEvent event = (ClusterControlEvent) payloadObject;

            event.setApplicationName(applicationName);
        }

        return super.preSend(message, channel);
    }
}
