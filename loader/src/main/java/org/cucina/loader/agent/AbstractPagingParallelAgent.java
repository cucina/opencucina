package org.cucina.loader.agent;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.time.StopWatch;
import org.cucina.core.concurrent.CompletionServiceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractPagingParallelAgent<T>
    implements Agent, ParallelAgent<T>, InitializingBean {
    private static final int DEFAULT_PAGESIZE = 200;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPagingParallelAgent.class);
    private java.util.concurrent.Executor executorService;
    private CompletionServiceFactory completionServiceFactory;
    private ContextService contextService;
    private Processor processor;
    private TransactionTemplate transactionTemplate;
    private boolean waitForCompletion = true;

    /** to wait for completion of each page before starting next one */
    private boolean waitForPageCompletion = false;
    private int pageSize = DEFAULT_PAGESIZE;
    private int timeoutSecs = 60;

    /**
     * Creates a new AbstractPagingParallelExecutor object.
     *
     * @param contextService
     *
     * @param processor
     */
    public AbstractPagingParallelAgent(ContextService contextService, Processor processor) {
        Assert.notNull(contextService, "contextService is null");
        this.contextService = contextService;
        Assert.notNull(processor, "processor is null");
        this.processor = processor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param completionServiceFactory
     *            JAVADOC.
     */
    public void setCompletionServiceFactory(CompletionServiceFactory completionServiceFactory) {
        this.completionServiceFactory = completionServiceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param executorService
     *            JAVADOC.
     */
    public void setExecutorService(java.util.concurrent.Executor executorService) {
        this.executorService = executorService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param pageSize
     *            JAVADOC.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param timeoutSecs
     *            JAVADOC.
     */
    public void setTimeoutSecs(int timeoutSecs) {
        this.timeoutSecs = timeoutSecs;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param transactionTemplate
     *            JAVADOC.
     */
    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * Indicates whether the executor will wait for completion of all threads it
     * has started. Default to true
     *
     * @param waitForCompletion
     *            wait or not.
     */
    public void setWaitForCompletion(boolean waitForCompletion) {
        this.waitForCompletion = waitForCompletion;
    }

    /**
     * If set, them each page processing should complete before the next one
     * started. Defaults to false.
     *
     * @param waitForPageCompletion
     *            JAVADOC.
     */
    public void setWaitForPageCompletion(boolean waitForPageCompletion) {
        this.waitForPageCompletion = waitForPageCompletion;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
    }

    /**
     * Calls for obtainItems and submits to completionService for each one of
     * them to processed by the processItem(...) in a separate thread.
     *
     */

    // Do not add @Transactional annotation here as if it waits for processors
    // and processing exceeds transaction timeout this will rollback, find some
    // other solution.
    @Override
    public void execute() {
        if (!allowStart()) {
            LOG.debug("Did not allow start this executor");

            return;
        }

        // TODO refactor to use advice
        // contextService.put(MeasuringDelegatingExecutorService.EXECUTION_CONTEXT,
        // this.getClass().getName());
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        CompletionService<Collection<Long>> completionService = buildCompletionService();

        int global = 0;

        for (int page = 0;; page++) {
            // contextService.put(MeasuringDelegatingExecutorService.EXECUTION_CONTEXT,
            // this.getClass().getName() + ".obtainNextPage()");
            Collection<T> items = obtainNextPage();

            if ((items == null) || items.isEmpty()) {
                LOG.debug("No items in the page " + page + " in " + getClass().getSimpleName());

                break;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("For page " + page + " obtained " + items.size() + " items in " +
                    getClass().getSimpleName());
            }

            for (T item : items) {
                // actual processing here
                completionService.submit(new CallableWrapper(this, item));
                global++;
            }

            if (waitForPageCompletion) {
                waitUntilLast(completionService, items.size());
            }
        }

        if (waitForCompletion && !waitForPageCompletion) {
            LOG.debug("Global wait:" + global);
            waitUntilLast(completionService, global);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Completed " + getClass().getName() + " in " + stopWatch.getTime() + "ms");
        }
    }

    /**
     * Default implementation simply submits the item to processor.
     *
     * @param item
     *            JAVADOC.
     */
    @Override
    public final Collection<Long> processItem(T item) {
        // contextService.put(MeasuringDelegatingExecutorService.EXECUTION_CONTEXT,
        // this.getClass().getName() + ".processItem()");
        return doProcessItem(item);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param pageSize
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    protected abstract Collection<T> obtainNextPage();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected ContextService getContextService() {
        return contextService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected int getPageSize() {
        return (pageSize <= 0) ? DEFAULT_PAGESIZE : pageSize;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected Processor getProcessor() {
        return processor;
    }

    /**
     * Hook for subclasses to do things before execution starts
     *
     * @return
     */
    protected boolean allowStart() {
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param item
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    protected Collection<Long> doProcessItem(T item) {
        return processor.process(item);
    }

    private CompletionService<Collection<Long>> buildCompletionService() {
        return ((completionServiceFactory != null)
        ? completionServiceFactory.<Collection<Long>>create(executorService)
        : new ExecutorCompletionService<Collection<Long>>(executorService));
    }

    private void waitUntilLast(CompletionService<Collection<Long>> completionService, int size) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting for executors to finish. " + size + " items were submitted in " +
                getClass().getSimpleName());
        }

        for (int i = 0; i < size; i++) {
            try {
                Future<Collection<Long>> future = completionService.take();

                Collection<Long> result = future.get(timeoutSecs, TimeUnit.SECONDS);

                if (LOG.isTraceEnabled()) {
                    LOG.trace("Processed:" + result);
                }
            } catch (TimeoutException e) {
                LOG.error("Executor processing timed out.");
                throw new RuntimeException(
                    "Executor execution failed, check for errors in logs and in rtce.");
            } catch (Exception e) {
                LOG.error("Unexpected exception, propagating exception.", e);
                throw new RuntimeException("Executor execution failed", e);
            }
        }
    }

    private class CallableWrapper
        implements Callable<Collection<Long>> {
        private ParallelAgent<T> exec;
        private T item;

        public CallableWrapper(ParallelAgent<T> exec, T item) {
            this.item = item;
            this.exec = exec;
        }

        @Override
        public Collection<Long> call()
            throws Exception {
            try {
                return transactionTemplate.execute(new TransactionCallback<Collection<Long>>() {
                        public Collection<Long> doInTransaction(TransactionStatus status) {
                            return exec.processItem(item);
                        }
                    });
            } catch (Exception e) {
                LOG.error("Error occurred in " + exec.getClass().getName(), e);
                throw e;
            }
        }
    }
}
