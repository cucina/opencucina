package org.cucina.engine.checks;

import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriverFactory;
import org.cucina.engine.definition.Token;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


/**
 * Ensure SpelCondition functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DomainCheckTest {
	private static final String EXPRESSION = "name == 'mikey'";
	private DomainExpressionCheck action;
	@Mock
	private ExpressionExecutor expressionExecutor;
	private Foo localFoo = new Foo();
	@Mock
	private ProcessDriverFactory executorFactory;
	@Mock
	private Token token;

	/**
	 * False condition
	 */
	@Test
	public void conditionFalse() {
		when(expressionExecutor.evaluate(localFoo, EXPRESSION)).thenReturn(false);

		ExecutionContext exectionContext = new DefaultExecutionContext(token, null, null,
				executorFactory);

		assertFalse("Should have returned false", action.test(exectionContext));
	}

	/**
	 * Test if null value returned is false
	 */
	@Test
	public void conditionNull() {
		when(expressionExecutor.evaluate(localFoo, EXPRESSION)).thenReturn(null);

		ExecutionContext exectionContext = new DefaultExecutionContext(token, null, null,
				executorFactory);

		assertFalse("Should have returned false", action.test(exectionContext));
	}

	/**
	 * Test a true condition where we're making use of BeanResolver and context
	 */
	@Test
	public void conditionTrue() {
		when(expressionExecutor.evaluate(localFoo, EXPRESSION)).thenReturn(true);

		ExecutionContext exectionContext = new DefaultExecutionContext(token, null, null,
				executorFactory);

		assertTrue("Should have returned true", action.test(exectionContext));
	}

	/**
	 * whens context
	 */
	@Test(expected = IllegalArgumentException.class)
	public void contextRequired() {
		action.test(null);
	}

	/**
	 * Set up for test
	 *
	 * @throws Exception.
	 */
	@Before
	public void setup()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		when(executorFactory.getExpressionExecutor()).thenReturn(expressionExecutor);
		doReturn(localFoo).when(token).getDomainObject();
		action = new DomainExpressionCheck();
		action.setExpression(EXPRESSION);
	}
}
