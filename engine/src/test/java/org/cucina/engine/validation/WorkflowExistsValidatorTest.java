package org.cucina.engine.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.engine.repository.WorkflowRepository;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowExistsValidatorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsValid() {
        WorkflowExistsValidator validator = new WorkflowExistsValidator();
        WorkflowRepository workflowRepository = mock(WorkflowRepository.class);

        when(workflowRepository.exists("target")).thenReturn(true);
        validator.setWorkflowRepository(workflowRepository);
        validator.initialize(null);
        assertTrue(validator.isValid("target", null));
        when(workflowRepository.exists("target")).thenReturn(false);
        assertFalse(validator.isValid("target", null));
    }
}
