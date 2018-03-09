package org.cucina.engine.validation;

import org.cucina.engine.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowUnusedValidator
		implements ConstraintValidator<WorkflowUnused, String> {
	private WorkflowRepository workflowRepository;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 * @param arg1 JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean isValid(String target, ConstraintValidatorContext arg1) {
		return !workflowRepository.isActive(target);
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
	public void initialize(WorkflowUnused arg0) {
	}
}
