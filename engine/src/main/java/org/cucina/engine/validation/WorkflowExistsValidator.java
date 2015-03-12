package org.cucina.engine.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.cucina.engine.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowExistsValidator
    implements ConstraintValidator<WorkflowExists, String> {
    private WorkflowRepository workflowRepository;

    /**
    * JAVADOC Method Level Comments
    *
    * @param arg0 JAVADOC.
    * @param arg1 JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    public boolean isValid(String target, ConstraintValidatorContext arg1) {
        return workflowRepository.exists(target);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowRepository JAVADOC.
     */
    @Autowired
    public void setWorkflowRepository(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     */
    @Override
    public void initialize(WorkflowExists arg0) {
    }
}
