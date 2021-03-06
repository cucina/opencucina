package org.cucina.engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkflowUnusedValidator.class)
public @interface WorkflowUnused {
	String message() default "org.cucina.engine.validation.workflowUsed";

	Class<?>[] groups() default {
	}
			;

	Class<Payload>[] payload() default {
	}
			;
}
