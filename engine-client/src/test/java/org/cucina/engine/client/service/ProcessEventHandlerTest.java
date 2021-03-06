package org.cucina.engine.client.service;

import org.cucina.conversation.events.CallbackEvent;
import org.cucina.conversation.events.ConversationEvent;
import org.cucina.engine.client.Check;
import org.cucina.engine.client.Operation;
import org.cucina.engine.client.testassist.Foo;
import org.cucina.engine.server.definition.CheckDescriptorDto;
import org.cucina.engine.server.definition.OperationDescriptorDto;
import org.cucina.engine.server.event.ActionResultEvent;
import org.cucina.engine.server.event.BooleanEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessEventHandlerTest {
	private static final String APPLICATION_NAME = "an";
	@Mock
	private Check check;
	@Mock
	private ConversionService conversionService;
	@Mock
	private DomainFindingService domainFindingService;
	private Foo foo;
	@Mock
	private Operation action;
	private ProcessEventHandler handler;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new ProcessEventHandler(domainFindingService, conversionService);
		foo = new Foo();

		when(domainFindingService.find("Foo", 100L)).thenReturn(foo);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testHandleCheckEvent()
			throws Exception {
		CheckDescriptorDto element = new CheckDescriptorDto();

		element.setPath("path");
		element.setDomainId(100L);
		element.setDomainType("Foo");

		when(conversionService.convert(element, Check.class)).thenReturn(check);

		Map<String, Object> parameters = new HashMap<String, Object>();

		when(check.test(foo, parameters)).thenReturn(true);

		CallbackEvent event = new CallbackEvent(element, parameters, APPLICATION_NAME);
		ConversationEvent re = handler.handleEvent(event);

		assertTrue(((BooleanEvent) re).isResult());
		verify(check).test(foo, parameters);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testHandleOperationEvent()
			throws Exception {
		OperationDescriptorDto element = new OperationDescriptorDto();

		element.setPath("path");
		element.setDomainId(100L);
		element.setDomainType("Foo");

		when(conversionService.convert(element, Operation.class)).thenReturn(action);

		Map<String, Object> parameters = new HashMap<String, Object>();
		CallbackEvent event = new CallbackEvent(element, parameters, APPLICATION_NAME);
		ConversationEvent re = handler.handleEvent(event);

		verify(action).execute(foo, parameters);
		System.err.println(((ActionResultEvent) re).getSource());
	}
}
