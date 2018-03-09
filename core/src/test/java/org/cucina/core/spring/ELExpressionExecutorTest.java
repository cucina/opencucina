package org.cucina.core.spring;

import org.cucina.core.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.spel.SpelEvaluationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test ELExpressionExecutor functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ELExpressionExecutorTest {
	private static final String BEAN_ID = "foo";
	private static final String BEANS_NAME = "mikey";
	private BeanFactory beanFactory;
	private ELExpressionExecutor executor;
	private Foo bean;
	private Map<String, Object> context;

	/**
	 * Test set failure
	 */
	@Test(expected = SpelEvaluationException.class)
	public void setFailure() {
		executor.set(context, "anotherloadofrubbish", "failed");
	}

	/**
	 * Test set successful
	 */
	@Test
	public void setSuccess() {
		executor.set(context, "#this['answer']", "failed");

		assertEquals("Wrong value", "failed", context.get("answer"));
	}

	/**
	 * Test set success using bean in spring config
	 */
	@Test
	public void setSuccessUsingBean() {
		executor.set(context, "@foo.name", "failed");

		assertEquals("Wrong value", "failed", bean.getName());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = SpelEvaluationException.class)
	public void evaluateFailUsingBean() {
		executor.setBeanFactory(null);
		context.put("answer", "failed");

		executor.evaluate(context, "@foo.name");
		fail("Should have thrown exception");
	}

	/**
	 * Test exception throws out
	 */
	@Test(expected = SpelEvaluationException.class)
	public void evaluateFailure() {
		context.put("answer", "failed");

		executor.evaluate(context, "madeuploadofrubbish");
		fail("Should have thrown exception");
	}

	/**
	 * Test exception throws out
	 */
	@Test
	public void evaluateNull() {
		context.put("answer", "failed");

		System.out.println("I'm here: " + executor.evaluate(context, "null > 0.0"));
	}

	/**
	 * Test simple evaluation works.
	 */
	@Test
	public void evaluateSuccess() {
		context.put("answer", "failed");

		assertEquals("Wrong value", "failed", executor.evaluate(context, "#this.get('answer')"));
	}

	/**
	 * Test simple evaluation using bean in spring config works.
	 */
	@Test
	public void evaluateSuccessUsingBean() {
		context.put("answer", "failed");

		assertEquals("Wrong value", BEANS_NAME, executor.evaluate(context, "@foo.name"));
	}

	/**
	 * Set up for test
	 */
	@Before
	public void onsetup() {
		executor = new ELExpressionExecutor();
		context = new HashMap<String, Object>();
		bean = new Foo();
		bean.setName(BEANS_NAME);
		beanFactory = mock(BeanFactory.class);
		when(beanFactory.getBean(BEAN_ID)).thenReturn(bean);
		when(beanFactory.getBean("ok")).thenReturn(new Add());
		executor.setBeanFactory(beanFactory);
	}

	/**
	 * Iterate over the list of values and pass to 'ok' bean which will add to value and return result
	 */
	@Test
	public void testCollectionProjection() {
		List<Integer> values = new ArrayList<Integer>();

		values.add(1);
		values.add(2);

		List<Integer> results = executor.evaluate(values, "#this.![#this + 4]");

		assertEquals("Incorrect number results", 2, results.size());
		assertEquals("Should have added value", Integer.valueOf(5), results.get(0));
		assertEquals("Should have added value", Integer.valueOf(6), results.get(1));
	}

	/**
	 * Iterate over the list of values and pass to 'ok' bean which will add to value and return result. This
	 * test illustrates that #this does not reference current value in context correctly if nested.
	 */
	public void testCollectionProjectionNestedCall() {
		List<Integer> values = new ArrayList<Integer>();

		values.add(1);
		values.add(2);

		List<Integer> results = executor.evaluate(values, "#this.![@ok.add(#this)]");

		assertEquals("Incorrect number results", 2, results.size());
		assertEquals("Should have added value", Integer.valueOf(5), results.get(0));
		assertEquals("Should have added value", Integer.valueOf(6), results.get(1));
	}

	/**
	 * Iterate over the list of values and pass to 'ok' bean which will add to value and then compare result with
	 * 5, of which the first should match.
	 */
	@Test
	public void testCollectionValueSelection() {
		List<Integer> values = new ArrayList<Integer>();

		values.add(1);
		values.add(2);

		assertTrue("Should be true",
				(Boolean) executor.evaluate(values, "#this.?[#this + 4 == 5].size() == 1"));
	}

	/**
	 * Iterate over the list of values and pass to 'ok' bean which will add to value and then compare result with
	 * 5, of which the first should match.
	 */
	public void testCollectionValueSelectionNestedCall() {
		List<Integer> values = new ArrayList<Integer>();

		values.add(1);
		values.add(2);

		assertTrue("Should be true",
				(Boolean) executor.evaluate(values, "#this.?[@ok.add(#this) == 5].size() == 1"));
	}

	public class Add {
		public int add(int i) {
			return i + 4;
		}
	}
}
