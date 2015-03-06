package org.cucina.cluster.spring;

import org.springframework.messaging.Message;

import org.cucina.cluster.event.ClusterControlEvent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ApplicationNameFilterTest {
    private static final String OTHER_NAME = "OTHER";
    private static final String NAME = "NAME";

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDifferentNames() {
        ApplicationNameFilter filter = new ApplicationNameFilter(NAME);

        @SuppressWarnings("rawtypes")
        Message message = mock(Message.class);
        ClusterControlEvent event = new ClusterTestEvent("source");

        event.setApplicationName(OTHER_NAME);
        when(message.getPayload()).thenReturn(event);
        assertFalse(filter.accept(message));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoName() {
        ApplicationNameFilter filter = new ApplicationNameFilter(NAME);

        @SuppressWarnings("rawtypes")
        Message message = mock(Message.class);
        ClusterControlEvent event = new ClusterTestEvent("source");

        event.setApplicationName(null);
        when(message.getPayload()).thenReturn(event);
        assertTrue(filter.accept(message));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNotClusterEvent() {
        ApplicationNameFilter filter = new ApplicationNameFilter(NAME);

        @SuppressWarnings("rawtypes")
        Message message = mock(Message.class);

        when(message.getPayload()).thenReturn(new Object());
        assertTrue(filter.accept(message));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSameNames() {
        ApplicationNameFilter filter = new ApplicationNameFilter(NAME);

        @SuppressWarnings("rawtypes")
        Message message = mock(Message.class);
        ClusterControlEvent event = new ClusterTestEvent("source");

        event.setApplicationName(NAME);
        when(message.getPayload()).thenReturn(event);
        assertTrue(filter.accept(message));
    }

    public static final class ClusterTestEvent
        extends ClusterControlEvent {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public ClusterTestEvent(String source) {
            super(source);
        }
    }
}
