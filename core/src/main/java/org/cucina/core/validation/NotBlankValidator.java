
package org.cucina.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NotBlankValidator
    implements ConstraintValidator<NotBlank, String> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param value JAVADOC.
     * @param context JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.trim().length() > 0;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param constraintAnnotation JAVADOC.
     */
    @Override
    public void initialize(NotBlank constraintAnnotation) {
        //no-op
    }
}
