package org.cucina.loader.agent;

import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Tests that PublishingExecutor functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PublishingAgentTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void requiresEvent() {
		new PublishingAgent(null);
	}

	/**
	 * Tests that event is published on execution
	 */
	@Test
	public void sendsEvent() {
		ApplicationEvent event = new ApplicationEvent(this) {
			private static final long serialVersionUID = 1L;
		};

		ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);

		PublishingAgent publishingExecutor = new PublishingAgent(event);

		publishingExecutor.setApplicationEventPublisher(applicationEventPublisher);

		publishingExecutor.execute();
		verify(applicationEventPublisher).publishEvent(event);
	}
}
