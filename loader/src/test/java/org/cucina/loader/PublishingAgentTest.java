package org.cucina.loader;

import java.util.UUID;

import org.cucina.core.spring.integration.MessagePublisher;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PublishingAgentTest {
    @Mock
    private FileLoader fileLoader;
    @Mock
    private MessagePublisher messagePublisher;
    private Object monitor = new Object();
    private PublishingAgent executor;
    private boolean complete;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        executor = new PublishingAgent(fileLoader, messagePublisher);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecute()
        throws Exception {
        complete = false;

        FileLoaderContainer flc = mock(FileLoaderContainer.class);

        when(fileLoader.next()).thenReturn(flc).thenReturn(null);

        Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        executor.execute();

                        synchronized (monitor) {
                            monitor.notify();
                        }

                        complete = true;
                    }
                });

        t.start();
        Thread.sleep(300);
        System.err.println("thread is alive =" + t.isAlive());

        ArgumentCaptor<UUID> acuuid = ArgumentCaptor.forClass(UUID.class);

        verify(flc).setUuid(acuuid.capture());

        FileLoaderAcknowledgementEvent event = new FileLoaderAcknowledgementEvent(acuuid.getValue());

        executor.onApplicationEvent(event);

        synchronized (monitor) {
            monitor.wait(10000);
        }

        assertTrue(complete);
    }
}
