package org.cucina.engine.client.service;

import org.cucina.conversation.Operative;
import org.cucina.engine.server.event.RegistrationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;


/**
 * @author vlevine
 */
public class ApplicationRegistrator
		implements ApplicationListener<ContextRefreshedEvent> {
	private MessageChannel asyncChannel;
	private String applicationName;
	private String myQueue;

	/**
	 * Creates a new ApplicationRegistrator object.
	 *
	 * @param applicationName .
	 * @param myQueue         .
	 * @param asyncChannel    .
	 */
	public ApplicationRegistrator(String applicationName, String myQueue,
								  MessageChannel asyncChannel) {
		this.myQueue = myQueue;
		this.applicationName = applicationName;
		Assert.notNull(asyncChannel, "asyncChannel is null");
		this.asyncChannel = asyncChannel;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		Message<?> message = MessageBuilder.withPayload(new RegistrationEvent(applicationName,
				applicationName, "jms://" + myQueue))
				.setHeader(Operative.DESTINATION_NAME, "server.queue")
				.build();

		asyncChannel.send(message);
	}
}
