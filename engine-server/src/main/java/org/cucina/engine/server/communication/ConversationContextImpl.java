package org.cucina.engine.server.communication;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.core.service.ContextService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component("conversationContext")
public class ConversationContextImpl
    implements ConversationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ConversationContextImpl.class);

    /** cucina-. */
    public static final String CONVERSATION_ID_PREFIX = "cucina-";
    private static final Random random = new Random();
    private ContextService contextService;

    /**
     * Creates a new ConversationContextImpl object.
     *
     * @param contextService
     *            JAVADOC.
     * @param clientRegistry
     *            JAVADOC.
     */
    @Autowired
    public ConversationContextImpl(ContextService contextService) {
        Assert.notNull(contextService, "contextService is null");
        this.contextService = contextService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     */
    @Override
    public void setConversationId(String conversationId) {
        contextService.put(CONVERSATION_ID, conversationId);
    }

    /**
    * Get conversationId
    *
    * @return JAVADOC.
    */
    @Override
    public String getConversationId(boolean startNew) {
        String conversationId = contextService.get(CONVERSATION_ID);

        if (startNew && StringUtils.isEmpty(conversationId)) {
            conversationId = startConversation();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Conversation id is [" + conversationId + "]");
        }

        return conversationId;
    }

    private String startConversation() {
        String conversationId = CONVERSATION_ID_PREFIX + random.nextInt(Integer.MAX_VALUE);

        this.setConversationId(conversationId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("New conversation started with id [" + conversationId + "]");
        }

        return conversationId;
    }
}
