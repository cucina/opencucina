package org.cucina.engine.validation;

import org.cucina.engine.repository.WorkflowRepository;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowUnusedValidatorTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsValid() {
		WorkflowUnusedValidator validator = new WorkflowUnusedValidator();
		WorkflowRepository workflowRepository = mock(WorkflowRepository.class);

		when(workflowRepository.isActive("target")).thenReturn(true);
		validator.setWorkflowRepository(workflowRepository);
		validator.initialize(null);

		assertFalse(validator.isValid("target", null));
		when(workflowRepository.isActive("target")).thenReturn(false);
		assertTrue(validator.isValid("target", null));
	}
}
