
package org.cucina.loader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import org.cucina.cluster.ChangeNotifier;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.service.I18nService;
import org.cucina.loader.testassist.Foo;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvLoaderTest {
    @Mock
    private HeadersModifier headersModifier;
    @Mock
    private I18nService i18nService;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private LoaderExceptionFactory loaderExceptionFactory;
    @Mock
    private MessageSource messageSource;
    @Mock
    private ChangeNotifier changeNotifier;
    

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        LoggingEnabler.enableLog(CsvLoader.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Fixing a bug - the sum is wrong
     *
     * @throws LoaderException
     */
    @Test
    public void testCounts()
        throws LoaderException {
        when(instanceFactory.getBean("SpriteUser")).thenReturn(new Foo());

        CsvRowLoader rowProcessor = new CsvRowLoader() {
                @Override
                @Transactional(rollbackFor = BindException.class)
                public void processRow(String applicationType, String[] headers, String[] line,
                    final int lineNo)
                    throws BindException {
                    assertArrayEquals(headers,
                        new String[] { "username", "name", "active", "admin", "email", "password" });
                }
            };

        CsvLoader loader = new CsvLoader(instanceFactory, rowProcessor, loaderExceptionFactory, changeNotifier);
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baout);

        pw.println("username,name,active,admin,email,password");
        pw.println("Username,Name,1,0,user@email.com,Password");
        pw.println("Username2,Name2,1,0,user@email.com,Password");
        pw.println("Username3,Name3,1,0,user@email.com,Password");
        pw.flush();

        int val = loader.loadCollection("SpriteUser", baout.toByteArray());

        verify(instanceFactory).getBean("SpriteUser");
        assertEquals(3, val);
        verify(changeNotifier).setProgrammatic();
        verify(changeNotifier).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Test
    public void testHeaderLookupSunnyDay()
        throws Exception {
        when(instanceFactory.getBean("SpriteUser")).thenReturn(new Annointed());

        CsvRowLoader rowProcessor = new CsvRowLoader() {
                @Override
                @Transactional(rollbackFor = BindException.class)
                public void processRow(String applicationType, String[] headers, String[] line,
                    final int lineNo)
                    throws BindException {
                    assertArrayEquals(headers, new String[] { "value", "name", "aNumber" });
                    assertArrayEquals(line, new String[] { "1", "Name", "1" });
                }
            };

        CsvLoader loader = new CsvLoader(instanceFactory, rowProcessor, loaderExceptionFactory,changeNotifier);

        when(headersModifier.modifyHeaders(new String[] { "VALUE BLAAH", "name", "BLAAAH BLAAH" },
                Annointed.class)).thenReturn(new String[] { "value", "name", "aNumber" });
        loader.setHeadersModifier(headersModifier);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baout);

        pw.println("VALUE BLAAH,name,BLAAAH BLAAH");
        pw.println("1,Name,1");
        pw.flush();
        loader.loadCollection("SpriteUser", baout.toByteArray());
        verify(instanceFactory).getBean("SpriteUser");
        
        verify(changeNotifier).setProgrammatic();
        verify(changeNotifier).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLineNoOnError() {
        when(instanceFactory.getBean("SpriteUser")).thenReturn(new Foo());

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baout);

        pw.println("username,name,active,admin,email,password");
        pw.println("Username,Name,1,0,user@email.com,Password");
        pw.println("Username2,Name2,1,0,user@email.com,Password");
        pw.println("Username3,Name3,1,0,user@email.com,Password");
        pw.flush();
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
            .thenReturn("2");

        LoaderExceptionFactory loaderExceptionFactory = new LoaderExceptionFactory(i18nService,
                messageSource);
        CsvRowLoader rowProcessor = new CsvRowLoader() {
                @Override
                @Transactional(rollbackFor = BindException.class)
                public void processRow(String applicationType, String[] headers, String[] line,
                    final int lineNo)
                    throws BindException {
                    assertArrayEquals(headers,
                        new String[] { "username", "name", "active", "admin", "email", "password" });

                    if (lineNo == 2) {
                        // exception on line 2
                        throw new RuntimeException();
                    }
                }
            };

        CsvLoader loader = new CsvLoader(instanceFactory, rowProcessor, loaderExceptionFactory,changeNotifier);

        try {
            loader.loadCollection("SpriteUser", baout.toByteArray());
            fail("should throw an exception");
        } catch (LoaderException e) {
            assertEquals("should be 2 as exception thrown on line 2", e.getErrors()[0], "2");
        }

        verify(instanceFactory).getBean("SpriteUser");
        
        verify(changeNotifier).setProgrammatic();
        verify(changeNotifier).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLoadCollectionInstanceFactoryReturnsNull() {
        String type = "SpriteUser";

        when(instanceFactory.getBean(type)).thenReturn(null);

        byte[] target = new byte[0];
        BindException be = new BindException(target, type);
        String errorCode = "loader.applicationType";

        be.reject(errorCode, new Object[] { type }, "Applicationtype 'SpriteUser' is invalid");

        LoaderException le = new LoaderException(new String[] { errorCode + ".translated" });

        when(loaderExceptionFactory.getLoaderException(be)).thenReturn(le);

        CsvLoader loader = new CsvLoader(instanceFactory,
                new CsvRowLoader() {
                    public void processRow(String applicationType, String[] headers, String[] line,
                        final int lineNo)
                        throws BindException {
                    }
                }, loaderExceptionFactory,changeNotifier);

        try {
            loader.loadCollection(type, target);
            fail("Should throw an exception, shouldn't get to here");
        } catch (LoaderException e) {
            assertTrue(e.getErrors().length > 0);
            assertEquals("loader.applicationType.translated", e.getErrors()[0]);
        }
        
        verify(changeNotifier, times(0)).setProgrammatic();
        verify(changeNotifier, times(0)).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLoadCollectionNullHeaders() {
        String type = "SpriteUser";

        when(instanceFactory.getBean(type)).thenReturn(new Foo());

        byte[] target = new byte[0];
        BindException be = new BindException(target, type);
        String errorCode = "loader.noHeaders";

        be.reject(errorCode, null, "The input file is empty");

        LoaderException le = new LoaderException(new String[] { errorCode + ".translated" });

        when(loaderExceptionFactory.getLoaderException(be)).thenReturn(le);

        CsvLoader loader = new CsvLoader(instanceFactory,
                new CsvRowLoader() {
                    public void processRow(String applicationType, String[] headers, String[] line,
                        final int lineNo)
                        throws BindException {
                    }
                }, loaderExceptionFactory,changeNotifier);

        try {
            loader.loadCollection(type, new byte[0]);
            fail("Should throw an exception, shouldn't get to here");
        } catch (LoaderException e) {
            assertTrue(e.getErrors().length > 0);
            assertEquals(errorCode + ".translated", e.getErrors()[0]);
        }

        verify(instanceFactory).getBean(type);
        
        verify(changeNotifier, times(1)).setProgrammatic();
        verify(changeNotifier, times(1)).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Test
    public void testLoadCollectionSunnyDay()
        throws Exception {
        when(instanceFactory.getBean("SpriteUser")).thenReturn(new Foo());

        CsvRowLoader rowProcessor = new CsvRowLoader() {
                @Override
                @Transactional(rollbackFor = BindException.class)
                public void processRow(String applicationType, String[] headers, String[] line,
                    final int lineNo)
                    throws BindException {
                    assertArrayEquals(headers,
                        new String[] { "username", "name", "active", "admin", "email", "password" });
                    assertArrayEquals(line,
                        new String[] { "Username", "Name", "1", "0", "user@email.com", "Password" });
                }
            };

        CsvLoader loader = new CsvLoader(instanceFactory, rowProcessor, loaderExceptionFactory,changeNotifier);
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baout);

        pw.println("username,name,active,admin,email,password");
        pw.println("Username,Name,1,0,user@email.com,Password");
        pw.flush();
        loader.loadCollection("SpriteUser", baout.toByteArray());
        verify(instanceFactory).getBean("SpriteUser");
        
        verify(changeNotifier, times(1)).setProgrammatic();
        verify(changeNotifier, times(1)).sendEvent();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws LoaderException
     *             JAVADOC.
     * @throws BindException
     *             JAVADOC.
     */
    @Test(expected = LoaderException.class)
    public void testRowProcessorThrowsException()
        throws LoaderException {
        String applicationType = "SpriteUser";

        when(instanceFactory.getBean(applicationType)).thenReturn(new Foo());

        String data = "Hello";
        BindException be = new BindException(new String[] { data }, applicationType);
        LoaderException le = new LoaderException(new String[] {  });

        when(loaderExceptionFactory.getLoaderException(be)).thenReturn(le);

        CsvRowLoader processor = new CsvRowLoader() {
                @Override
                public void processRow(String applicationType, String[] headers, String[] line,
                    final int lineNumber)
                    throws BindException {
                    assertEquals(applicationType, applicationType);
                    assertArrayEquals(headers, new String[] { "hello" });
                    assertArrayEquals(line, new String[] { "Hello" });
                    throw new BindException(line, applicationType);
                }
            };

        CsvLoader loader = new CsvLoader(instanceFactory, processor, loaderExceptionFactory,changeNotifier);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();

        PrintWriter pw = new PrintWriter(baout);

        pw.println("hello");
        pw.println(data);
        pw.flush();
        loader.loadCollection(applicationType, baout.toByteArray());
        
        verify(changeNotifier, times(1)).setProgrammatic();
        verify(changeNotifier, times(1)).sendEvent();
    }

    public static class Annointed
        extends PersistableEntity {
        private static final long serialVersionUID = 1L;
        @LoaderColumnLookup(propertyAlias = "BLAAAH BLAAH")
        private String aNumber;
        @SuppressWarnings("unused")
        private String name;
        @LoaderColumnLookup(propertyAlias = "VALUE BLAAH")
        private String value;
    }
}
