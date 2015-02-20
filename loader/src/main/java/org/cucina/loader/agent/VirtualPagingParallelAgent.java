
package org.cucina.loader.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cucina.core.service.ContextService;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A wrapper for those executors without a natural paging facility.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class VirtualPagingParallelAgent<T>
    extends AbstractPagingParallelAgent<T> {
    private static final Logger LOG = LoggerFactory.getLogger(VirtualPagingParallelAgent.class);

    /**
     * Creates a new VirtualPagingParallelExecutor object.
     *
     * @param contextService JAVADOC.
     * @param executor JAVADOC.
     */
    public VirtualPagingParallelAgent(ContextService contextService, Processor processor) {
        super(contextService, processor);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public abstract Collection<T> obtainItems();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected final Collection<T> obtainNextPage() {
        String pagingId = this.getClass().getName() + "_pageId";
        String itemsId = this.getClass().getName() + "_items";
        Integer pageId = (Integer) getContextService().get(pagingId);

        if ((pageId == null) || (pageId == 0)) {
            List<T> items = new ArrayList<T>(obtainItems());

            getContextService().put(itemsId, items);
            pageId = 0;
            getContextService().put(pagingId, pageId);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing page " + pageId);
        }

        @SuppressWarnings("unchecked")
        List<T> items = (List<T>) getContextService().get(itemsId);

        if ((items == null) || items.isEmpty()) {
            LOG.debug("No items");

            return null;
        }

        int fromIndex = pageId * getPageSize();
        int toIndex = ((fromIndex + getPageSize()) > items.size()) ? items.size()
                                                                   : (fromIndex + getPageSize());

        if (fromIndex >= toIndex) {
            LOG.debug("page index points to end");
            cleanup(pagingId, itemsId);

            return null;
        }

        List<T> page = items.subList(fromIndex, toIndex);

        if ((page == null) || page.isEmpty()) {
            LOG.debug("page empty");
            cleanup(pagingId, itemsId);

            return null;
        }

        getContextService().put(pagingId, ++pageId);

        return page;
    }

    private void cleanup(String pagingId, String itemsId) {
        getContextService().put(pagingId, null);
        getContextService().put(itemsId, null);
    }
}
