package org.cucina.engine.validation;

import org.cucina.core.model.Attachment;
import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowHistory;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowPlacesRetainedValidatorTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsValid() {
		WorkflowPlacesRetainedValidator validator = new WorkflowPlacesRetainedValidator();
		ProcessEnvironment workflowEnvironment = mock(ProcessEnvironment.class);
		ProcessDefinitionParser parser = mock(ProcessDefinitionParser.class);
		ProcessDefinition wd1 = new ProcessDefinition();

		wd1.setId("newPlaces");

		State p1 = mock(State.class);

		when(p1.getId()).thenReturn("p1");

		State p2 = mock(State.class);

		when(p2.getId()).thenReturn("p2");

		State[] newPlaces = new State[]{p1, p2};

		wd1.setAllPlaces(newPlaces);

		ProcessDefinition wd2 = new ProcessDefinition();

		wd2.setId("oldPlaces");

		State p21 = mock(State.class);

		when(p21.getId()).thenReturn("p1");

		State p22 = mock(State.class);

		when(p22.getId()).thenReturn("p2");

		State[] oldPlaces = new State[]{p21, p22};

		wd2.setAllPlaces(oldPlaces);

		ProcessDefinition wd1bad = new ProcessDefinition();

		wd1bad.setId("badPlaces");

		State p21bad = mock(State.class);

		when(p21bad.getId()).thenReturn("p1b");

		State[] newPlacesBad = new State[]{p21bad, p22};

		wd1bad.setAllPlaces(newPlacesBad);
		doReturn(wd1).doReturn(wd2).doReturn(wd1bad).doReturn(wd2).when(parser)
				.parse(any(ByteArrayResource.class));
		when(workflowEnvironment.getDefinitionParser()).thenReturn(parser);
		validator.setWorkflowEnvironment(workflowEnvironment);
		validator.initialize(null);

		Workflow wf = new Workflow();
		List<WorkflowHistory> histories = new ArrayList<WorkflowHistory>();
		WorkflowHistory h1 = new WorkflowHistory();
		byte[] data1 = "hello".getBytes();
		Attachment a1 = new Attachment();

		a1.setData(data1);
		h1.setAttachment(a1);
		histories.add(h1);

		WorkflowHistory h2 = new WorkflowHistory();
		byte[] data2 = "bye".getBytes();
		Attachment a2 = new Attachment();

		a2.setData(data2);
		h2.setAttachment(a2);

		histories.add(h2);
		wf.setWorkflowHistories(histories);
		assertTrue(validator.isValid(wf, null));
		histories = new ArrayList<WorkflowHistory>();
		h1 = new WorkflowHistory();

		h1.setAttachment(a1);
		histories.add(h1);
		h2 = new WorkflowHistory();
		h2.setAttachment(a2);
		histories.add(h2);
		wf.setWorkflowHistories(histories);
		assertFalse(validator.isValid(wf, null));
	}
}
