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
public class WorkflowDefinitionUniqueValidatorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsValid() {
        WorkflowDefinitionUniqueValidator validator = new WorkflowDefinitionUniqueValidator();
        WorkflowRepository workflowRepo = mock(WorkflowRepository.class);

        when(workflowRepo.exists("target")).thenReturn(true);
        validator.setWorkflowRepository(workflowRepo);
        validator.initialize(null);
        assertFalse(validator.isValid("target", null));
        when(workflowRepo.exists("target")).thenReturn(false);
        assertTrue(validator.isValid("target", null));
    }
}
