package org.cucina.loader;

import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import org.cucina.loader.processor.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvRowLoaderProcessor
    implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(CsvRowLoaderProcessor.class);
    private CsvRowLoader rowLoader;

    /**
     * Creates a new CsvLoaderRowProcessorImpl object.
     *
     * @param instanceFactory
     *            JAVADOC.
     * @param persistenceService
     *            JAVADOC.
     */
    public CsvRowLoaderProcessor(CsvRowLoader rowLoader) {
        Assert.notNull(rowLoader, "rowLoader is null");
        this.rowLoader = rowLoader;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param object JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public void process(Object object) {
        if (!(object instanceof FileLoaderContainer)) {
            LOG.warn("Invalid call to this processor with object:" + object);

            return;
        }

        FileLoaderContainer container = (FileLoaderContainer) object;

        try {
            rowLoader.processRow(container.getApplicationType(), container.getHeaders(),
                container.getData(), container.getLineNumber());
        } catch (BindException e) {
            LOG.error("Oops", e);
        }
    }
}
