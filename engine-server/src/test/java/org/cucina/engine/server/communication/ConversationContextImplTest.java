package org.cucina.engine.server.communication;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.cucina.core.service.ContextService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ConversationContextImplTest {
    @Mock
    private ContextService contextService;
    private ConversationContextImpl context;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        context = new ConversationContextImpl(contextService);
    }

    /**
     * Tests that conversation id is created if none is returned from
     * contextService and .
     */
    @Test
    public void testCreateConversationId() {
        when(contextService.get(ConversationContextImpl.CONVERSATION_ID)).thenReturn(null);

        String conversationId = context.startConversation();

        assertNotNull("Should have generated new id", conversationId);
        assertEquals("incorrect id", 0,
            conversationId.indexOf(ConversationContextImpl.CONVERSATION_ID_PREFIX));
    }

    /**
     * Tests that conversation id is returned from contextService when it
     * exists.
     */
    @Test
    public void testGetConversationIdExists() {
        when(contextService.get(ConversationContextImpl.CONVERSATION_ID)).thenReturn("convId");
        assertEquals("incorrect id", "convId", context.getConversationId());
    }

    /**
     * Tests that null returned if conversation id doesn't exist.
     */
    @Test
    public void testGetConversationIdNotExists() {
        assertNull("Shouldn't barf if null", context.getConversationId());
    }

    /**
     * Tests that we have runtime exception if conversation already started
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStartConversationAlreadyExists() {
        TransactionSynchronizationManager.initSynchronization();
        when(contextService.get(ConversationContextImpl.CONVERSATION_ID)).thenReturn("convId");

        context.startConversation();
    }
}
