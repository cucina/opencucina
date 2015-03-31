package org.cucina.security.validation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UsernameValidatorTest {
    /**
     * JAVADOC Method Level Comments
     */
    public void testInvalid() {
        UsernameValidator validator = new UsernameValidator();
        ValidUsername vu = mock(ValidUsername.class);

        when(vu.message()).thenReturn("Oops");
        validator.initialize(vu);

        UsernameValidatingPlugin plugin = mock(UsernameValidatingPlugin.class);

        when(plugin.isValid("username")).thenReturn(false);

        // TODO autowire plugin
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintViolationBuilder cvb = mock(ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate("Oops")).thenReturn(cvb);

        NodeBuilderCustomizableContext nbdc = mock(NodeBuilderCustomizableContext.class);

        when(cvb.addPropertyNode("username")).thenReturn(nbdc);
        when(nbdc.addConstraintViolation()).thenReturn(context);
        assertFalse("Should be unique", validator.isValid("username", context));
        verify(context).buildConstraintViolationWithTemplate("Oops");
        verify(nbdc).addConstraintViolation();
        verify(context).disableDefaultConstraintViolation();
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void testIsValid() {
        UsernameValidator validator = new UsernameValidator();
        UsernameValidatingPlugin plugin = mock(UsernameValidatingPlugin.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        when(plugin.isValid("username")).thenReturn(true);
        // TODO autowire plugin
        assertTrue("Should be unique", validator.isValid("username", context));
        verify(context).disableDefaultConstraintViolation();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsValidDefault() {
        UsernameValidator validator = new UsernameValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        assertTrue("Should be unique", validator.isValid("username", context));
        verify(context).disableDefaultConstraintViolation();
    }
}
