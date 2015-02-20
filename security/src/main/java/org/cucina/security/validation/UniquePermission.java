
package org.cucina.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePermissionValidator.class)
public @interface UniquePermission {String message() default "{org.cucina.meringue.security.validation.UniquePermission.message}";
    Class<?>[] groups() default  {
    }
    ;
    Class<Payload>[] payload() default  {
    }
    ;
}
