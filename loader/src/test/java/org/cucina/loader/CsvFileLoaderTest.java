
package org.cucina.loader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.PrintWriter;

import org.cucina.core.InstanceFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CsvFileLoaderTest {
    private static final String HEADERS = "h1,h2,h3";
    private static final String DATA = "d1,d2,d3";
    private CsvFileLoader loader;
    @Mock
    private HeadersModifier headersModifier;
    @Mock
    private InstanceFactory instanceFactory;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);

        File temp = File.createTempFile("test", ".csv");

        temp.deleteOnExit();

        PrintWriter pw = new PrintWriter(temp);

        pw.println(HEADERS);
        pw.println(DATA);
        pw.flush();
        pw.close();
        loader = new CsvFileLoader(temp.getAbsolutePath(), "applicationType", instanceFactory);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testAfterPropertiesSet()
        throws Exception {
        loader.afterPropertiesSet();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testModifier() {
        when(instanceFactory.<String>getClassType("applicationType")).thenReturn(String.class);

        loader.setHeadersModifier(headersModifier);

        loader.next();

        ArgumentCaptor<String[]> hc = ArgumentCaptor.forClass(String[].class);

        verify(headersModifier).modifyHeaders(hc.capture(), eq(String.class));

        String[] h = hc.getValue();

        assertEquals("h1", h[0]);
        assertEquals("h2", h[1]);
        assertEquals("h3", h[2]);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNext() {
        FileLoaderContainer flc = loader.next();

        assertEquals("applicationType", flc.getApplicationType());

        String[] h = flc.getHeaders();

        assertEquals("h1", h[0]);
        assertEquals("h2", h[1]);
        assertEquals("h3", h[2]);

        String[] d = flc.getData();

        assertEquals("d1", d[0]);
        assertEquals("d2", d[1]);
        assertEquals("d3", d[2]);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testReset() {
        loader.reset();
    }
}
