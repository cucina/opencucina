
package org.cucina.loader.agent;

import java.util.Collection;

import org.cucina.core.service.ContextService;
import org.cucina.loader.processor.Processor;


/**
 * Decorated superclass providing managed page number. Page number is stored in
 * thread local.
 *
 * @author $Author: $
 * @version $Revision: $
 *
 * @param <T>
 *            JAVADOC.
 */
public abstract class ManagedPagingParallelAgent<T>
    extends AbstractPagingParallelAgent<T> {
    /**
     * Creates a new ManagedPagingParalleExecutor object.
     *
     * @param contextService
     *            JAVADOC.
     * @param processor
     *            JAVADOC.
     */
    public ManagedPagingParallelAgent(ContextService contextService, Processor processor) {
        super(contextService, processor);
    }

    /**
     * Stubbed in paging implementation
     *
     * @return JAVADOC.
    */
    @Override
    public Collection<T> obtainItems() {
        throw new IllegalArgumentException("Not implemented in paging, use obtainNextPage()");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected final Collection<T> obtainNextPage() {
        String PAGE_NUMBER = this.getClass().getName() + "_page";
        Integer page = (Integer) getContextService().get(PAGE_NUMBER);

        if (page == null) {
            page = 0;
            getContextService().put(PAGE_NUMBER, page);
        }

        Collection<T> items = obtainNextPage(page);

        if ((items != null) && (items.size() != 0)) {
            page++;
            getContextService().put(PAGE_NUMBER, page);

            return items;
        }

        getContextService().put(PAGE_NUMBER, null);

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param number
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    protected abstract Collection<T> obtainNextPage(int number);
}
