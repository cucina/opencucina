package org.cucina.engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkflowDefinitionUniqueValidator.class)
public @interface WorkflowDefinitionUnique {
	String message() default "org.cucina.engine.validation.alreadyExists";

	Class<?>[] groups() default {
	}
			;

	Class<Payload>[] payload() default {
	}
			;

	String[] properties() default {
	}
			;
}
