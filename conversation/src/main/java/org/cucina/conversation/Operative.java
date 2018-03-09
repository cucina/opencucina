package org.cucina.conversation;

import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;


/**
 * @author vlevine
 */
public interface Operative {
	String CONVERSATION_ID = "_conversationId_";
	String DESTINATION_NAME = "_destinationName_";

	/**
	 * @return .
	 */
	PollableChannel getCallbackChannel();

	/**
	 * @param channel .
	 */
	void setCallbackChannel(PollableChannel channel);

	/**
	 * @param request .
	 * @return .
	 */
	Message<?> process(Message<?> request);
}
