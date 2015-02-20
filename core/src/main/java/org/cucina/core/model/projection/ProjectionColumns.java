package org.cucina.core.model.projection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProjectionColumns { //
    ProjectionColumn[] value();
    String fieldName() default "";
    Class<?>[] groups() default  {
    }
    ;
}
