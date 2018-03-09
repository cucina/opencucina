package org.cucina.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {
		NotBlankValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
	public abstract String message() default "{org.cucina.core.validation.NotBlank.message}";

	public abstract Class<?>[] groups() default {
	}
			;

	public abstract Class<? extends Payload>[] payload() default {
	}
			;
}
