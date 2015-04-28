package org.cucina.engine.client.service;

import java.util.HashMap;

import org.springframework.messaging.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simply waits on the callback channel until it stops reveiving.
 *
 * @author vlevine
  */
public class PassiveOperative
    extends AbstractOperative {
    private static final Logger LOG = LoggerFactory.getLogger(PassiveOperative.class);

    /**
     *
     *
     * @param request .
     *
     * @return .
     */
    @Override
    public Message<?> process(Message<?> request) {
        try {
            loopProcess(new HashMap<String, Object>());
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignoring", e);
            }
        }

        return null;
    }
}
