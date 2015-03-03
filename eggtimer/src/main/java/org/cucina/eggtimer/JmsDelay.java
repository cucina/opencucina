package org.cucina.eggtimer;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Component
public class JmsDelay {
    private static final Logger LOG = LoggerFactory.getLogger(JmsDelay.class);
    @Autowired
    private ApplicationContext context;

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     */
    @JmsListener(destination = "cucina.eggtimer.delay")
    public void processMessage(Message message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received:" + message);
        }

        Destination replyTo;
        String body;

        try {
            replyTo = message.getJMSReplyTo();

            if (replyTo == null) {
                LOG.warn("No replyTo were set");

                return;
            }

            body = ((TextMessage) message).getText();
        } catch (JMSException e) {
            LOG.error("Oops", e);

            return;
        }

        LOG.info("Body " + body);

        try {
            long delay = Long.parseLong(body);

            Thread.sleep(delay);
        } catch (NumberFormatException e) {
            LOG.error("Oops", e);
        } catch (InterruptedException e) {
            LOG.error("Oops", e);
        }

        MessageCreator messageCreator = new MessageCreator() {
                @Override
                public Message createMessage(Session session)
                    throws JMSException {
                    return session.createTextMessage("after delay");
                }
            };

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        jmsTemplate.send(replyTo, messageCreator);
    }
}
