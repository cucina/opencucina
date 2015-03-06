package org.cucina.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindException;

import org.cucina.core.validation.Create;

import org.cucina.loader.processor.Processor;
import org.cucina.loader.testassist.Foo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvRowLoaderImplTest {
    @Mock
    private ConstraintViolation<User> constraintViolation;
    @Mock
    private ConversionService conversionService;
    private CsvRowLoaderImpl rowProcessor;
    @Mock
    private Processor processor;
    private User u1;
    @Mock
    private Validator validator;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        rowProcessor = new CsvRowLoaderImpl(validator, conversionService);
        rowProcessor.setPropagateErrors(true);
        rowProcessor.setProcessor(processor);
        u1 = new User("user", "pass", new ArrayList<GrantedAuthority>());
        when(conversionService.convert(any(CsvlineWrapper.class), eq(Object.class))).thenReturn(u1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEmptyArrayForLine()
        throws Exception {
        // when(environment.getDefinitionRegistry()).thenReturn(registry);
        rowProcessor.processRow("User",
            new String[] { "username", "name", "active", "system", "email", "password" },
            new String[] {  }, 1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testInvalidHeader() {
        when(validator.validate(u1, Default.class, Create.class))
            .thenReturn(Collections.<ConstraintViolation<User>>emptySet());

        try {
            rowProcessor.processRow("User",
                new String[] { "username", "name", "active", "system", "email", "password" },
                new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" }, 1);
        } catch (BindException e) {
            assertEquals("loader.invalidHeaderError", e.getAllErrors().get(0).getCode());
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLineIncorrect()
        throws Exception {
        rowProcessor.processRow("User",
            new String[] { "username", "name", "active", "system", "email", "password" },
            new String[] { "", "" }, 1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLineNull()
        throws Exception {
        rowProcessor.processRow("User",
            new String[] { "username", "name", "active", "system", "email", "password" }, null, 1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNotPropagateException()
        throws Exception {
        rowProcessor = new CsvRowLoaderImpl(validator, conversionService);
        rowProcessor.setProcessor(processor);

        when(validator.validate(u1, Default.class, Create.class))
            .thenReturn(Collections.<ConstraintViolation<User>>emptySet());
        doThrow(new RuntimeException("Whoops")).when(processor)
            .process(any(LoadedObjectWrapper.class));

        rowProcessor.processRow("User",
            new String[] { "username", "name", "active", "system", "email", "password" },
            new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" }, 1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testObjectProcessing()
        throws BindException {
        Object domain = new Object();

        when(conversionService.convert(any(CsvlineWrapper.class), eq(Object.class)))
            .thenReturn(domain);

        rowProcessor.processRow("Domain", new String[] { "nowt" }, new String[] { "nowt" }, 1);

        verify(processor).process(domain);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testThrowsBindExceptionAndHeaders() {
        Foo u1 = new Foo();

        when(validator.validate(u1, Default.class, Create.class))
            .thenReturn(Collections.<ConstraintViolation<Foo>>emptySet());

        doThrow(new FatalBeanException("Oops")).when(conversionService)
            .convert(any(CsvlineWrapper.class), eq(Object.class));

        try {
            rowProcessor.processRow("Foo", new String[] { "name", "value" },
                new String[] { "Username", "BRYAN" }, 1);
            fail("Should throw bind exception");
        } catch (BindException be) {
            // force a parse error, results in a bind exception
            assertEquals("loader.parseError", be.getAllErrors().get(0).getCode());
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = BindException.class)
    public void testThrowsException()
        throws Exception {
        when(validator.validate(u1, Default.class, Create.class))
            .thenReturn(Collections.<ConstraintViolation<User>>emptySet());
        doThrow(new RuntimeException("Whoops")).when(processor)
            .process(any(LoadedObjectWrapper.class));

        rowProcessor.processRow("User",
            new String[] { "username", "name", "active", "system", "email", "password" },
            new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" }, 1);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testValidationResultsInBindException() {
        Set<ConstraintViolation<User>> violations = new HashSet<ConstraintViolation<User>>();

        when(constraintViolation.getMessage()).thenReturn("blaah");
        violations.add(constraintViolation);
        when(validator.validate(u1, Default.class, Create.class)).thenReturn(violations);

        try {
            rowProcessor.processRow("User",
                new String[] { "username", "name", "active", "system", "email", "password" },
                new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" }, 1);
            fail("Should throw bind exception");
        } catch (BindException be) {
            //be.printStackTrace();
            // force a parse error, results in a bind exception
            assertEquals("loader.validation.failure", be.getAllErrors().get(0).getCode());
            assertEquals("blaah", be.getAllErrors().get(0).getDefaultMessage());
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testValidationResultsInNestedException() {
        when(validator.validate(u1, Default.class, Create.class))
            .thenReturn(Collections.<ConstraintViolation<User>>emptySet());

        String string = "message1";

        doThrow(new BeanInitializationException("outer", new BeanInitializationException(string)))
            .when(processor).process(any(LoadedObjectWrapper.class));

        try {
            rowProcessor.processRow("User",
                new String[] { "username", "name", "active", "system", "email", "password" },
                new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" }, 1);
        } catch (BindException e) {
            assertEquals("loader.exception", e.getAllErrors().get(0).getCode());
            assertEquals(1, e.getAllErrors().get(0).getArguments()[0]);
            assertEquals(string, e.getAllErrors().get(0).getArguments()[1]);
        }
    }
}
