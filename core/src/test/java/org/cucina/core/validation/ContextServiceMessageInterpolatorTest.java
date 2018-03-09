package org.cucina.core.validation;

import org.cucina.core.service.ContextService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests that ContextServiceMessageInterpolator functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ContextServiceMessageInterpolatorTest {
	private static final String MESSAGE = "message";
	private static final String MESSAGE_TEMPLATE = "messageTemplate";
	private static final String RESULT = "result";
	@Mock
	private MessageSource messageSource;
	@Mock
	private ContextService service;
	private ContextServiceMessageInterpolator interpolator;

	/**
	 * Default locale is used if no locale method called.
	 */
	@Test
	public void interpolatorDefault() {
		when(messageSource.getMessage(MESSAGE_TEMPLATE, new Object[]{}, Locale.getDefault()))
				.thenReturn(RESULT);

		assertEquals("incorrect return value", RESULT,
				interpolator.interpolate(MESSAGE_TEMPLATE, null));
	}

	/**
	 * If contextService contains a message keyed against message template name then we attempt to
	 * get message for that value.
	 */
	@Test
	public void messageFound() {
		when(service.get(MESSAGE_TEMPLATE)).thenReturn(MESSAGE);
		when(messageSource.getMessage(MESSAGE, new Object[]{}, Locale.FRENCH)).thenReturn(RESULT);
		assertEquals("incorrect return value", RESULT,
				interpolator.interpolate(MESSAGE_TEMPLATE, null, Locale.FRENCH));

		verify(messageSource).getMessage(MESSAGE, new Object[]{}, Locale.FRENCH);
	}

	/**
	 * When message not found tries to get message for template.
	 */
	@Test
	public void messageNotFound() {
		when(messageSource.getMessage(MESSAGE_TEMPLATE, new Object[]{}, Locale.FRENCH))
				.thenReturn(RESULT);
		assertEquals("incorrect return value", RESULT,
				interpolator.interpolate(MESSAGE_TEMPLATE, null, Locale.FRENCH));

		verify(messageSource).getMessage(MESSAGE_TEMPLATE, new Object[]{}, Locale.FRENCH);
	}

	/**
	 * Sets up for test.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		interpolator = new ContextServiceMessageInterpolator(service);
		interpolator.setMessageSource(messageSource);
	}
}
