package org.cucina.core.validation;

import org.cucina.core.testassist.Foo;
import org.junit.Test;

import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BeanMessageInterpolatorTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void testDefaultConstructor() {
		Context context = mock(Context.class);
		ConstraintDescriptor cd = mock(ConstraintDescriptor.class);
		Map<String, Object> atts = new HashMap<String, Object>();
		String[] properties = {"name", "value"};

		atts.put("properties", properties);

		when(cd.getAttributes()).thenReturn(atts);
		when(context.getConstraintDescriptor()).thenReturn(cd);

		Foo foo = new Foo();

		foo.setName("Name");
		foo.setValue(200);
		when(context.getValidatedValue()).thenReturn(foo);

		BeanMessageInterpolator interpolator = new BeanMessageInterpolator();

		assertEquals("message Name and 200",
				interpolator.interpolate("message {0} and {1}", context));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void testInterpolateStringContext() {
		Context context = mock(Context.class);
		ConstraintDescriptor cd = mock(ConstraintDescriptor.class);
		Map<String, Object> atts = new HashMap<String, Object>();

		when(cd.getAttributes()).thenReturn(atts);
		when(context.getConstraintDescriptor()).thenReturn(cd);

		Foo foo = new Foo();

		when(context.getValidatedValue()).thenReturn(foo);

		MessageInterpolator delegate = mock(MessageInterpolator.class);

		when(delegate.interpolate("message", context)).thenReturn("MEssAGE");

		BeanMessageInterpolator interpolator = new BeanMessageInterpolator(delegate);

		interpolator.interpolate("message", context);
		assertEquals(foo, atts.get("0"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testInterpolateStringContextLocale() {
		Context context = mock(Context.class);
		@SuppressWarnings("rawtypes")
		ConstraintDescriptor cd = mock(ConstraintDescriptor.class);
		Map<String, Object> atts = new HashMap<String, Object>();
		String[] properties = {"name", "value"};

		atts.put("properties", properties);

		when(cd.getAttributes()).thenReturn(atts);
		when(context.getConstraintDescriptor()).thenReturn(cd);

		Foo foo = new Foo();

		foo.setName("Name");
		foo.setValue(200);
		when(context.getValidatedValue()).thenReturn(foo);

		MessageInterpolator delegate = mock(MessageInterpolator.class);

		when(delegate.interpolate("message", context, Locale.getDefault())).thenReturn("MEssAGE");

		BeanMessageInterpolator interpolator = new BeanMessageInterpolator(delegate);

		interpolator.interpolate("message", context, Locale.getDefault());
		assertEquals("Name", atts.get("0"));
		assertEquals("200", atts.get("1"));
	}
}
