package org.cucina.engine.server.utils;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.TokenRepository;
import org.cucina.engine.server.event.CompensateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompensateMessageHandler
    implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CompensateMessageHandler.class);
    private TokenRepository tokenRepository;

    /**
     * Creates a new CompensateMessageHandler object.
     *
     * @param tokenRepository
     *            JAVADOC.
     */
    public CompensateMessageHandler(TokenRepository tokenRepository) {
        Assert.notNull(tokenRepository, "tokenRepository is null");
        this.tokenRepository = tokenRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message
     *            JAVADOC.
     *
     * @throws MessagingException
     *             JAVADOC.
     */
    @Override
    @Transactional
    public void handleMessage(Message<?> message)
        throws MessagingException {
        CompensateEvent event = (CompensateEvent) message.getPayload();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Compensating for " + event);
        }

        Long[] ids = event.getIds();

        Collection<ProcessToken> tokens = tokenRepository.findByApplicationTypeAndIds(event.getType(),
                ids);

        if (CollectionUtils.isNotEmpty(tokens)) {
            for (ProcessToken token : tokens) {
                List<HistoryRecord> records = token.getHistories();

                if (CollectionUtils.isEmpty(records)) {
                    tokenRepository.deleteDeep(token);
                } else {
                    records.remove(0);
                    tokenRepository.save(token);
                }
            }
        }
    }
}
