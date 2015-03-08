
package org.cucina.i18n.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueMessageCodeValidator.class)
public @interface UniqueMessageCode {String message() default "{org.cucina.meringue.validation.UniqueMessageCode.message}";
    String property() default "label";
    String basename() default "messages";
    Class<?>[] groups() default  {
    }
    ;
    Class<Payload>[] payload() default  {
    }
    ;
}
