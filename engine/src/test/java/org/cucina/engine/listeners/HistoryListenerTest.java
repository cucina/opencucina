package org.cucina.engine.listeners;

import org.cucina.core.InstanceFactory;
import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.definition.Decision;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HistoryListenerTest {
	private HistoryListener listener;
	@Mock
	private InstanceFactory instanceFactory;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		when(instanceFactory.getBean(any(String.class))).thenReturn(new HistoryRecord());
		listener = new HistoryListener();
		listener.setInstanceFactory(instanceFactory);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("serial")
	@Test
	public void testEnteredState() {
		ProcessToken token = new ProcessToken();
		HistoryRecord historyRecord = new HistoryRecord();
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(HistoryRecord.COMMENTS_PROPERTY, "Hahaha");
		parameters.put("mock", "shock");
		parameters.put(HistoryListener.HISTORY_RECORD, historyRecord);

		DefaultExecutionContext executionContext = new DefaultExecutionContext(token, parameters,
				null, null);

		State place = mock(State.class);

		when(place.getId()).thenReturn("haha");
		listener.enteredState(place,
				new Transition() {
					@Override
					public String getId() {
						return "hehe";
					}
				}, executionContext);
		assertEquals("Hahaha", historyRecord.getComments());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("serial")
	@Test
	public void testLeavingState() {
		ProcessToken token = new ProcessToken();
		HistoryRecord oldHistoryRecord = new HistoryRecord();

		oldHistoryRecord.setApprovedBy("joe");
		oldHistoryRecord.setAssignedTo("fred");
		token.addHistoryRecord(oldHistoryRecord);

		Foo domainObject = new Foo();

		token.setDomainObject(domainObject);

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(HistoryRecord.COMMENTS_PROPERTY, "Hahaha");

		DefaultExecutionContext executionContext = new DefaultExecutionContext(token, parameters,
				null, null);
		State place = mock(State.class);

		when(place.getId()).thenReturn("haha");

		listener.leavingState(place,
				new Transition() {
					@Override
					public String getId() {
						return "hehe";
					}
				}, executionContext);

		List<HistoryRecord> histories = token.getHistories();

		assertNotNull("History is null", histories);
		assertEquals(2, histories.size());
		assertEquals("joe", parameters.get("approvedBy"));
		assertNull(parameters.get("assignedTo"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@SuppressWarnings("serial")
	@Test
	public void testLeavingStateToDecision() {
		ProcessToken token = new ProcessToken();
		HistoryRecord oldHistoryRecord = new HistoryRecord();

		oldHistoryRecord.setApprovedBy("joe");
		oldHistoryRecord.setAssignedTo("fred");
		token.addHistoryRecord(oldHistoryRecord);

		Foo domainObject = new Foo();

		token.setDomainObject(domainObject);

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(HistoryRecord.COMMENTS_PROPERTY, "Hahaha");

		DefaultExecutionContext executionContext = new DefaultExecutionContext(token, parameters,
				null, null);
		State place = mock(State.class);

		when(place.getId()).thenReturn("haha");

		listener.leavingState(place,
				new Transition() {
					@Override
					public String getId() {
						return "hehe";
					}

					@Override
					public State getOutput() {
						return new Decision();
					}
				}, executionContext);

		List<HistoryRecord> histories = token.getHistories();

		assertNotNull("History is null", histories);
		assertEquals(1, histories.size());
	}
}
