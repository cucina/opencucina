
package org.cucina.core.model.projection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProjectionColumn { //
    /**
     * Name to be used in UI table for this field
     */
    String columnName() default "";
    /**
     * Value of this property will be displayed
     */
    String property() default "";
    String aggregate() default "";
    Class<?>[] groups() default  {
    }
    ;
}
