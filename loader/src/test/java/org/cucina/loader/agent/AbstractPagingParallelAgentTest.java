package org.cucina.loader.agent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.cucina.core.concurrent.CompletionServiceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.loader.processor.Processor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Ensure that the AbstractPagingParallelExecutor functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AbstractPagingParallelAgentTest {
    private AbstractPagingParallelAgent<Object> executor;
    private Collection<Object> items = new ArrayList<Object>();
    @Mock
    private CompletionService<Collection<Long>> completionService;
    @Mock
    private CompletionServiceFactory completionServiceFactory;
    private ContextService contextService;
    @Mock
    private ExecutorService executorService;
    @Mock
    private Future<Collection<Long>> future;
    private Object item;
    @Mock
    private Processor processor;
    @Mock
    private TransactionTemplate transactionTemplate;
    private boolean completed;

    /**
         * Set up for test. The obtainNextPage method only returns value once
         * so that we don't get into a loop.
         */
    @Before
    public void asetup()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        contextService = new ThreadLocalContextService();
        items.add(item);
        executor = new AbstractPagingParallelAgent<Object>(contextService, processor) {
                    private boolean returned;

                    @Override
                    protected Collection<Object> obtainNextPage() {
                        if (!returned) {
                            returned = true;

                            return items;
                        }

                        return null;
                    }

                    @Override
                    public Collection<Object> obtainItems() {
                        throw new RuntimeException("Not implemented");
                    }
                };
        executor.setTimeoutSecs(3);
        executor.setExecutorService(executorService);
        executor.setTransactionTemplate(transactionTemplate);
        when(completionServiceFactory.<Collection<Long>>create(executorService))
            .thenReturn(completionService);
        executor.setCompletionServiceFactory(completionServiceFactory);
        when(completionService.take()).thenReturn(future);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void completeFailure()
        throws Exception {
        doThrow(new InterruptedException()).when(completionService).take();

        try {
            executor.execute();
            fail("Shouldn't have finished nicely");
        } catch (Exception e1) {
        }

        assertFalse("Should have timed out before completed", completed);
    }

    /**
     * By sleeping for 2 seconds the task will ensure that the executor waits until the task has completed before returning.
     * We check this by asserting the completed flag has been set to true.
     */
    @Test
    public void completesIfFinished()
        throws Exception {
        Collection<Long> value = Collections.singleton(111L);

        when(future.get(20, TimeUnit.SECONDS)).thenReturn(value);
        executor.setTimeoutSecs(20);
        executor.execute();

        verify(future).get(20, TimeUnit.SECONDS);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecute() {
        executor.execute();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsDone() {
        FutureTask<String> future = new FutureTask<String>(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, null);

        assertFalse("Should not be true until run", future.isDone());
        future.run();
        assertTrue("Should have run", future.isDone());
    }
}
