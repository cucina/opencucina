package org.cucina.loader.agent;

import org.cucina.core.service.ScheduleService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SchedulingAgentTest {
	private Agent delegate;
	private ScheduleService service;
	private String GROUPNAME = "myGroupName";
	private String NAME = "myName";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		service = mock(ScheduleService.class);
		delegate = new Agent() {
			@Override
			public void execute() {
			}
		};
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCtor() {
		new SchedulingAgent(null, delegate, NAME, 2);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCtor2() {
		new SchedulingAgent(service, null, NAME, 2);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCtor3() {
		new SchedulingAgent(service, delegate, "", 2);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCtor4() {
		new SchedulingAgent(service, delegate, NAME, 0);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testExecute() {
		SchedulingAgent executor = new SchedulingAgent(service, delegate, NAME, 2);

		executor.execute();
		verify(service, times(1)).stop(NAME, "agent");
		verify(service, times(1))
				.start(NAME, "agent", 2000, delegate, "execute", new HashMap<String, Object>());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testExecuteProps() {
		SchedulingAgent executor = new SchedulingAgent(service, delegate, NAME, 21);

		executor.execute();

		Map<String, Object> propsMap = new HashMap<String, Object>();

		propsMap.put("x", "y");
		executor.setProperties(propsMap);
		executor.execute();
		verify(service, times(2)).stop(NAME, "agent");
		verify(service, times(1))
				.start(NAME, "agent", 21000, delegate, "execute", new HashMap<String, Object>());
		verify(service, times(1)).start(NAME, "agent", 21000, delegate, "execute", propsMap);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGroupName() {
		SchedulingAgent executor = new SchedulingAgent(service, delegate, NAME, 5);

		executor.setGroupName(GROUPNAME);
		executor.execute();
		verify(service, times(1)).stop(NAME, GROUPNAME);
		verify(service, times(1))
				.start(NAME, GROUPNAME, 5 * 1000, delegate, "execute", new HashMap<String, Object>());
	}
}
