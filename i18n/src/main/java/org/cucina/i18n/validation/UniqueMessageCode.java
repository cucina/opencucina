package org.cucina.i18n.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueMessageCodeValidator.class)
public @interface UniqueMessageCode {
	String message() default "{org.cucina.meringue.validation.UniqueMessageCode.message}";

	String property() default "label";

	Class<?>[] groups() default {
	}
			;

	Class<Payload>[] payload() default {
	}
			;
}
