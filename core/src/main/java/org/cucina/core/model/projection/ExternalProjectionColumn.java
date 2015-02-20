
package org.cucina.core.model.projection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalProjectionColumn { //
    /**
     * The projection alias to use.
     * @return
     */
    String columnName();
    /**
     * Property to be selected.
     * @return
     */
    String property();
    /**
     * If an aggregate is required, i.e. count
     *
     * @return
     */
    String aggregate() default "";
    /**
     *
     * @return
     */
    Class<?>[] groups() default  {
    }
    ;
}
