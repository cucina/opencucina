
package org.cucina.core.model.projection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * External projection columns referencing another type which will have it's own alias.
 *
 * @author
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExternalProjectionColumns { //
    ExternalProjectionColumn[] value();
    String fieldName();
    Class<?> clazz();
    Class<?>[] groups() default  {
    }
    ;
}
