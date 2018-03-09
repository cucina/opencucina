package org.cucina.engine.definition;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Test that StartState functions as expected
 *
 * @author $Author: thornton $
 * @version $Revision: 1.1 $
 */
public class StartStateTest {
	private StartStation state;

	/**
	 * Set up for test
	 *
	 * @throws Exception.
	 */
	@Before
	public void setUp()
			throws Exception {
		state = new StartStation();
		state.setId("start");
	}

	/**
	 * Test finds named transition
	 */
	@Test
	public void testFindsNamedTransition() {
		Transition defaultTransition = new Transition();

		defaultTransition.setId("mikes");

		defaultTransition.setDefault(true);
		state.addTransition(defaultTransition);

		Transition mikesTransition = new Transition();

		mikesTransition.setId("mikes");

		mikesTransition.setDefault(false);
		state.addTransition(mikesTransition);

		assertEquals("Should find transition", mikesTransition, state.getTransition("mikes"));
	}

	/**
	 * Tests that if it has default finds it
	 */
	@Test
	public void testWithoutTransitionHasDefault() {
		Transition defaultTransition = new Transition();

		defaultTransition.setDefault(true);
		state.addTransition(defaultTransition);

		assertEquals("Should find transition", defaultTransition, state.getTransition(null));
	}

	/**
	 * Checks barfs if default message not found
	 */
	@Test
	public void testWithoutTransitionNoDefault() {
		try {
			state.getTransition(null);
			fail("Should have thrown exception");
		} catch (TransitionNotFoundException e) {
		}
	}
}
