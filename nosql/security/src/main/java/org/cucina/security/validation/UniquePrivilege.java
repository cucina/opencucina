package org.cucina.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePrivilegeValidator.class)
public @interface UniquePrivilege {
	String message() default "{org.cucina.meringue.security.validation.UniquePrivilege.message}";

	Class<?>[] groups() default {
	}
			;

	Class<Payload>[] payload() default {
	}
			;
}
