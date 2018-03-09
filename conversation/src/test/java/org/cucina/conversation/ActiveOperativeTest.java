package org.cucina.conversation;

import org.cucina.conversation.events.CallbackEvent;
import org.cucina.conversation.events.CommitEvent;
import org.cucina.conversation.events.ConversationEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * @author vlevine
 */
public class ActiveOperativeTest {
	private ActiveOperative operative;
	@Mock
	private EventHandler<ConversationEvent> eventHandler;
	@Mock
	private Message<?> request;
	@Mock
	private MessageChannel callbackReplyChannel;
	@Mock
	private MessageChannel requestChannel;
	@Mock
	private PollableChannel callbackChannel;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		operative = new ActiveOperative(requestChannel);
		operative.setCallbackReplyChannel(callbackReplyChannel);
		operative.setEventHandler(eventHandler);
		operative.setCallbackChannel(callbackChannel);
	}

	/**
	 *
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void testProcess() {
		Map<String, Object> hs = new HashMap<String, Object>();

		hs.put(Operative.CONVERSATION_ID, "calue");

		MessageHeaders headers = new MessageHeaders(hs);

		when(request.getHeaders()).thenReturn(headers);

		Message reply = mock(Message.class);
		MessageHeaders replyHeaders = mock(MessageHeaders.class);

		when(reply.getHeaders()).thenReturn(replyHeaders);

		CallbackEvent ce = new CallbackEvent();
		CommitEvent coe = new CommitEvent();

		when(reply.getPayload()).thenReturn(ce).thenReturn(coe);
		when(callbackChannel.receive(5000)).thenReturn(reply);

		ConversationEvent ee = mock(ConversationEvent.class);

		when(eventHandler.handleEvent(ce)).thenReturn(ee);
		operative.process(request);
		verify(requestChannel).send(request);

		ArgumentCaptor<Message> mac = ArgumentCaptor.forClass(Message.class);

		verify(callbackReplyChannel).send(mac.capture());

		Message callmess = mac.getValue();

		assertEquals(ee, callmess.getPayload());
		assertEquals(headers.get(Operative.CONVERSATION_ID),
				callmess.getHeaders().get(Operative.CONVERSATION_ID));
	}

	/**
	 *
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void testProcessWithReplyChannel() {
		Map<String, Object> hs = new HashMap<String, Object>();

		hs.put(Operative.CONVERSATION_ID, "calue");

		MessageHeaders headers = new MessageHeaders(hs);

		when(request.getHeaders()).thenReturn(headers);

		Message reply = mock(Message.class);
		MessageHeaders replyHeaders = mock(MessageHeaders.class);
		MessageChannel tempChannel = mock(MessageChannel.class);

		when(replyHeaders.get(MessageHeaders.REPLY_CHANNEL)).thenReturn(tempChannel);
		when(reply.getHeaders()).thenReturn(replyHeaders);

		CallbackEvent ce = new CallbackEvent();
		CommitEvent coe = new CommitEvent();

		when(reply.getPayload()).thenReturn(ce).thenReturn(coe);
		when(callbackChannel.receive(5000)).thenReturn(reply);

		ConversationEvent ee = mock(ConversationEvent.class);

		when(eventHandler.handleEvent(ce)).thenReturn(ee);
		operative.process(request);
		verify(requestChannel).send(request);

		ArgumentCaptor<Message> mac = ArgumentCaptor.forClass(Message.class);

		verify(tempChannel).send(mac.capture());

		Message callmess = mac.getValue();

		assertEquals(ee, callmess.getPayload());
		assertEquals(headers.get(Operative.CONVERSATION_ID),
				callmess.getHeaders().get(Operative.CONVERSATION_ID));
	}
}
