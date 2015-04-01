
package org.cucina.loader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoaderExceptionFactoryTest {
    @Mock
    private MessageSource messageSource;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testFailureAndMessageSource()
        throws Exception {
        String returnMessage = "BLAAH oh BLAAH oh blaah blaah";

        //        MessageSource messageSource = mock(MessageSource.class);
        String errorCode = "loader.fileNullOrEmpty";

        when(messageSource.getMessage(errorCode, null, Locale.getDefault())).thenReturn(returnMessage);

        LoaderExceptionFactory service = new LoaderExceptionFactory(messageSource);

        BindException be = new BindException(new Object(), "");

        be.reject(errorCode, null, "");

        LoaderException le = service.getLoaderException(be);

        verify(messageSource).getMessage(errorCode, null, Locale.getDefault());
        assertEquals(le.getErrors()[0], returnMessage);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testFailureAndMessageSourceDefaultLocale()
        throws Exception {
        String returnMessage = "BLAAH oh BLAAH oh blaah blaah";
        String errorCode = "loader.fileNullOrEmpty";

        when(messageSource.getMessage(errorCode, null, Locale.getDefault()))
            .thenReturn(returnMessage);

        LoaderExceptionFactory service = new LoaderExceptionFactory(messageSource);

        BindException be = new BindException(new Object(), "");

        be.reject(errorCode, null, "");

        LoaderException le = service.getLoaderException(be);

        verify(messageSource).getMessage(errorCode, null, Locale.getDefault());
        assertEquals(le.getErrors()[0], returnMessage);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testFailureAndMessageSourceThrowsUp()
        throws Exception {
        String message = "BLAAH oh BLAAH oh blaah blaah";
        String errorCode = "loader.fileNullOrEmpty";

        when(messageSource.getMessage(errorCode, null, Locale.getDefault()))
            .thenThrow(new RuntimeException(message));

        LoaderExceptionFactory service = new LoaderExceptionFactory( messageSource);

        String defaultMessage = "This is the default message";
        BindException be = new BindException(new Object(), defaultMessage);

        be.reject(errorCode, null, defaultMessage);

        LoaderException le = service.getLoaderException(be);

        assertEquals("Should return the default message if MessageSource blows up", defaultMessage,
            le.getErrors()[0]);
    }
}
