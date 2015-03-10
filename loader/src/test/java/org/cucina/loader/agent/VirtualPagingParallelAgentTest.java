package org.cucina.loader.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.cucina.core.concurrent.CompletionServiceFactory;
import org.cucina.core.service.ContextService;

import org.cucina.loader.processor.Processor;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class VirtualPagingParallelAgentTest {
    private Collection<Object> items;
    @Mock
    private CompletionService<Collection<Long>> completionService;
    @Mock
    private CompletionServiceFactory completionServiceFactory;
    @Mock
    private ContextService contextService;
    @Mock
    private ExecutorService executorService;
    @Mock
    private Future<Collection<Long>> future;
    private Object item;
    private VirtualPagingParallelAgent<Object> executor;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup()
        throws Exception {
        MockitoAnnotations.initMocks(this);

        //ContextService contextService = new ThreadLocalContextService();
        executor = new VirtualPagingParallelAgent<Object>(contextService, mock(Processor.class)) {
                    @Override
                    public Collection<Object> obtainItems() {
                        return items;
                    }

                    @Override
                    protected void doProcessItem(Object arg) {
                    }
                };
        when(completionServiceFactory.<Collection<Long>>create(executorService))
            .thenReturn(completionService);
        when(completionService.take()).thenReturn(future);
        executor.setCompletionServiceFactory(completionServiceFactory);
        executor.setExecutorService(executorService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecute() {
        items = new ArrayList<Object>();

        Object obj = new Object();

        items.add(obj);
        items.add(item);
        when(contextService.get(executor.getClass().getName() + "_items")).thenReturn(items)
            .thenReturn(null);
        when(contextService.get(executor.getClass().getName() + "_pageId")).thenReturn(null)
            .thenReturn(1);
        executor.setPageSize(1);
        executor.execute();
        verify(contextService).put(executor.getClass().getName() + "_pageId", 1);
    }
}
