
package org.cucina.loader;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.core.service.ContextService;
import org.cucina.loader.agent.ManagedPagingParallelAgent;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class FileLoaderAgent
    extends ManagedPagingParallelAgent<FileLoaderContainer> {
    private static final Logger LOG = LoggerFactory.getLogger(FileLoaderAgent.class);
    private FileLoader fileLoader;
    private boolean complete = false;

    /**
     * Creates a new FileLoaderExecutor object.
     *
     * @param contextService
     *            JAVADOC.
     * @param processor
     *            JAVADOC.
     */
    public FileLoaderAgent(ContextService contextService, Processor processor,
        FileLoader fileLoader) {
        super(contextService, processor);
        Assert.notNull(fileLoader, "fileLoader is null");
        this.fileLoader = fileLoader;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param page
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<FileLoaderContainer> obtainNextPage(int page) {
        if (page == 0) {
            complete = false;
        }

        if (isComplete()) {
            return null;
        }

        Collection<FileLoaderContainer> containers = new ArrayList<FileLoaderContainer>();

        for (int i = 0; i < getPageSize(); i++) {
            FileLoaderContainer container = fileLoader.next();

            if (container == null) {
                LOG.debug("End of file at line " + ((page * getPageSize()) + i));
                setComplete();
                fileLoader.reset();

                break;
            }

            container.setLineNumber((page * getPageSize()) + i);
            containers.add(container);
        }

        return containers;
    }

    /**
     * JAVADOC Method Level Comments
     */
    protected void setComplete() {
        complete = true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected boolean isComplete() {
        return complete;
    }
}
