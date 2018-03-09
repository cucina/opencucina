package org.cucina.engine.operations;

import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriverFactory;
import org.cucina.engine.definition.Token;
import org.cucina.engine.email.UserAccessorBean;
import org.cucina.engine.event.EmailEvent;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Class EmailActionTest.
 */
public class EmailOperationTest {
	private static final String TEMPLATE_NAME = "MyEmail";
	private static final String PARAMETER_EXPRESSION = "parameters.prop_map";
	private static final String PARAM_NAME = "something";
	private static final String PARAM_VALUE = "value";
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	private EmailOperation emailAction;
	@Mock
	private ExpressionExecutor expressionExecutor;
	private Foo localFoo = new Foo();
	private List<Object> users = new ArrayList<Object>();
	private Map<String, Object> parametersMap;
	@Mock
	private ProcessDriverFactory executorFactory;
	@Mock
	private Token token;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		when(executorFactory.getExpressionExecutor()).thenReturn(expressionExecutor);
		doReturn(localFoo).when(token).getDomainObject();

		users = getUsers(2);

		emailAction = new EmailOperation();

		emailAction.setApplicationEventPublisher(applicationEventPublisher);
		emailAction.setTemplateName(TEMPLATE_NAME);
		emailAction.setFilterCurrentUser(false);
		emailAction.setParameterMapExpr(PARAMETER_EXPRESSION);

		parametersMap = new HashMap<String, Object>();
		parametersMap.put(PARAM_NAME, PARAM_VALUE);
	}

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testEmailAttachments()
			throws Exception {
		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		context.addParameter("users", users);

		when(expressionExecutor.evaluate(context, PARAMETER_EXPRESSION)).thenReturn(parametersMap);

		emailAction.setApplicationEventPublisher(applicationEventPublisher);
		emailAction.execute(context);

		ArgumentCaptor<ApplicationEvent> captor = ArgumentCaptor.forClass(ApplicationEvent.class);

		verify(applicationEventPublisher).publishEvent(captor.capture());

		ApplicationEvent event = captor.getValue();

		assertNotNull("must have set an event", event);
		assertTrue("must be EmailEvent", event instanceof EmailEvent);
	}

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testEmailMapParameters()
			throws Exception {
		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		context.addParameter("users", users);

		when(expressionExecutor.evaluate(context, PARAMETER_EXPRESSION)).thenReturn(parametersMap);

		emailAction.setParameterMapExpr(PARAMETER_EXPRESSION);
		emailAction.execute(context);
	}

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testEmailWithNoContextUsers()
			throws Exception {
		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		emailAction.execute(context);
	}

	/**
	 * JAVADOC.
	 */
	@Test
	public void testExcludeCurrentUser() {
		emailAction.setFilterCurrentUser(true);
		emailAction.setUserAccessor(new UserAccessorBean() {
			@Override
			public Object getCurrentUser() {
				return users.get(1);
			}
		});

		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		context.addParameter(EmailOperation.USERS, users);

		emailAction.setContextUsersKey(EmailOperation.USERS);

		ExpressionExecutor executor = mock(ExpressionExecutor.class);

		when(executor.evaluate(context, PARAMETER_EXPRESSION)).thenReturn(parametersMap);
		emailAction.execute(context);
	}

	/**
	 * Ensure no runtime exception.
	 */
	@Test
	public void testExcludeCurrentUserNull() {
		emailAction.setFilterCurrentUser(true);
		emailAction.setUserAccessor(new UserAccessorBean() {
			@Override
			public Object getCurrentUser() {
				return null;
			}
		});

		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		context.addParameter("users", users);

		ExpressionExecutor executor = mock(ExpressionExecutor.class);

		when(executor.evaluate(context, PARAMETER_EXPRESSION)).thenReturn(parametersMap);
		emailAction.execute(context);
	}

	/**
	 * JAVADOC.
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void testTokenParameters()
			throws Exception {
		when(token.getPlaceId()).thenReturn("myPlace");

		ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

		context.addParameter("users", users);

		ExpressionExecutor executor = mock(ExpressionExecutor.class);

		when(executor.evaluate(context, PARAMETER_EXPRESSION)).thenReturn(parametersMap);
		when(executor.evaluate(token, "placeId")).thenReturn("myPlace");

		emailAction.setPropertiesList("placeId");
		emailAction.execute(context);
	}

	private List<Object> getUsers(int num) {
		List<Object> users = new ArrayList<Object>();

		for (int i = 0; i < num; i++) {
			users.add(new Object());
		}

		return users;
	}
}
