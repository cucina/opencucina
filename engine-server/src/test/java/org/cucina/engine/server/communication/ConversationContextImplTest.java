package org.cucina.engine.server.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.cucina.conversation.Operative;
import org.cucina.core.service.ContextService;


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
        when(contextService.get(Operative.CONVERSATION_ID)).thenReturn(null);

        String conversationId = context.getConversationId(true);

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
        when(contextService.get(Operative.CONVERSATION_ID)).thenReturn("convId");
        assertEquals("incorrect id", "convId", context.getConversationId(false));
    }

    /**
     * Tests that null returned if conversation id doesn't exist.
     */
    @Test
    public void testGetConversationIdNotExists() {
        assertNull("Shouldn't barf if null", context.getConversationId(false));
    }
}
