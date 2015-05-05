package org.cucina.conversation;

import java.io.Serializable;

import java.util.Arrays;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.cucina.conversation.events.CommitSuccessEvent;
import org.cucina.conversation.events.CompensateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
 */
public class TransactionHandlerImpl
    implements TransactionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionHandlerImpl.class);
    private MessageChannel asyncChannel;

    /**
     * Creates a new TransactionHandlerImpl object.
     *
     * @param asyncChannel .
     */
    public TransactionHandlerImpl(MessageChannel asyncChannel) {
        this.asyncChannel = asyncChannel;
    }

    /**
     *
     *
     * @see org.cucina.engine.client.service.TransactionHandler#registerTxHandler
     * (java.lang.String, java.io.Serializable)
     */
    @Override
    public void registerTxHandler(final String entityType, final Serializable... ids) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        handleStatus(status, entityType, ids);
                    }
                });
        }
    }

    private void handleStatus(int status, String type, Serializable... ids) {
        Message<?> callmess;

        if (TransactionSynchronization.STATUS_COMMITTED == status) {
            // TODO make CommitSuccess handle type/ids combo
            callmess = MessageBuilder.withPayload(new CommitSuccessEvent(ids))
                                     .setHeader(Operative.DESTINATION_NAME, "server.queue").build();
        } else {
            CompensateEvent compensateEvent = new CompensateEvent(status);

            compensateEvent.setIds(ids);
            compensateEvent.setType(type);
            callmess = MessageBuilder.withPayload(compensateEvent)
                                     .setHeader(Operative.DESTINATION_NAME, "server.queue").build();
            LOG.debug("Compensating for " + type + ":" + Arrays.toString(ids));
        }

        asyncChannel.send(callmess);
    }
}
