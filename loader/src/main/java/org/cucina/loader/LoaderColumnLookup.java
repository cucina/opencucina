package org.cucina.loader;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoaderColumnLookup {
	public abstract String[] propertyAlias() default "";

	public abstract Class<?>[] groups() default {
	}
			;

	public abstract Class<? extends Payload>[] payload() default {
	}
			;
}
