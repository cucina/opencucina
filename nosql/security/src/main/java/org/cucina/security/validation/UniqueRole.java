
package org.cucina.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRoleValidator.class)
public @interface UniqueRole {String message() default "{org.cucina.meringue.security.validation.UniqueRole.message}";
    Class<?>[] groups() default  {
    }
    ;
    Class<Payload>[] payload() default  {
    }
    ;
}
