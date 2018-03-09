package org.cucina.engine.service;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ProcessSession;
import org.cucina.engine.ProcessSessionFactory;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.testassist.Foo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * @author vlevine
 */
public class DefaultProcessServiceTest {
	/**
	 *
	 */
	@Test
	public void testExecuteTransition() {
		Token token = mock(Token.class);

		when(token.getPlaceId()).thenReturn("haha");
		when(token.getProcessDefinitionId()).thenReturn("workflowDefinitionId");

		ProcessSessionFactory workflowSessionFactory = mock(ProcessSessionFactory.class);

		ProcessSession session = mock(ProcessSession.class);

		DefaultExecutionContext context = new DefaultExecutionContext(token, null, null, null);

		when(session.createExecutionContext(token, null)).thenReturn(context);
		session.signal(context, "hehe");
		when(workflowSessionFactory.openSession("workflowDefinitionId")).thenReturn(session);

		DefaultProcessService service = new DefaultProcessService(workflowSessionFactory);

		assertEquals("Should have returned token", token,
				service.executeTransition(token, "hehe", null));
	}

	/**
	 *
	 */
	@Test
	public void testListTransitions() {
		final Token token = mock(Token.class);

		when(token.getPlaceId()).thenReturn("haha");
		when(token.getProcessDefinitionId()).thenReturn("workflowDefinitionId");

		ProcessSessionFactory workflowSessionFactory = mock(ProcessSessionFactory.class);

		ProcessSession session = mock(ProcessSession.class);

		DefaultExecutionContext context = new DefaultExecutionContext(token, null, null, null);

		when(session.createExecutionContext(token, null)).thenReturn(context);
		when(workflowSessionFactory.openSession("workflowDefinitionId")).thenReturn(session);

		Collection<Transition> list = new ArrayList<Transition>();
		Transition t1 = new Transition();

		t1.setId("hello");
		list.add(t1);
		when(session.getAvailableTransitions(context)).thenReturn(list);

		DefaultProcessService service = new DefaultProcessService(workflowSessionFactory);

		assertTrue("Not containing hello", service.listTransitions(token, null).contains("hello"));
	}

	/**
	 *
	 */
	@Test
	public void testStartWorkflowObjectStringMapOfStringObject() {
		Foo foo = new Foo();
		Map<String, Object> params = new HashMap<String, Object>();
		Token token = mock(Token.class);

		doReturn(foo).when(token).getDomainObject();

		ProcessSession session = mock(ProcessSession.class);

		when(session.startProcessInstance(foo, null, params)).thenReturn(token);

		ProcessSessionFactory workflowSessionFactory = mock(ProcessSessionFactory.class);

		when(workflowSessionFactory.openSession(foo.getClass().getSimpleName())).thenReturn(session);

		DefaultProcessService service = new DefaultProcessService(workflowSessionFactory);

		assertEquals("Should have returned token", token,
				service.startProcess(foo, foo.getClass().getSimpleName(), null, params));
	}

	/**
	 *
	 */
	@Test
	public void testStartWorkflowObjectStringStringMapOfStringObject() {
		Foo foo = new Foo();
		Map<String, Object> params = new HashMap<String, Object>();
		Token token = mock(Token.class);

		doReturn(foo).when(token).getDomainObject();

		ProcessSession session = mock(ProcessSession.class);

		when(session.startProcessInstance(foo, null, params)).thenReturn(token);

		ProcessSessionFactory workflowSessionFactory = mock(ProcessSessionFactory.class);

		when(workflowSessionFactory.openSession("hoho")).thenReturn(session);

		DefaultProcessService service = new DefaultProcessService(workflowSessionFactory);

		assertEquals("Should have returned token", token,
				service.startProcess(foo, "hoho", null, params));
	}
}
