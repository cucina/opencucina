package org.cucina.loader;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.cucina.core.service.ContextService;

import org.cucina.loader.processor.Processor;

import org.cucina.testassist.utils.LoggingEnabler;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class FileLoaderAgentTest {
    @Mock
    private ContextService contextService;
    @Mock
    private FileLoader fileLoader;
    @Mock
    private Processor processor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        LoggingEnabler.enableLog(FileLoaderAgent.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testObtainNextPageInt() {
        FileLoaderAgent executor = new FileLoaderAgent(contextService, processor, fileLoader);

        executor.setPageSize(100);

        FileLoaderContainer flc = new FileLoaderContainer();

        when(fileLoader.next()).thenReturn(flc).thenReturn(null);

        Collection<FileLoaderContainer> containers = executor.obtainNextPage(0);

        assertEquals(1, containers.size());

        /*        for (FileLoaderContainer container : containers) {
                    System.err.println(ToStringBuilder.reflectionToString(container));
                }
        */
    }
}
