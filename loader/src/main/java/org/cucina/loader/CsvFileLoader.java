package org.cucina.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import au.com.bytecode.opencsv.CSVReader;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CsvFileLoader
    implements FileLoader, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(CsvFileLoader.class);
    private CSVReader csvReader;
    private HeadersModifier headersModifier;
    private InstanceFactory instanceFactory;
    private String applicationType;
    private String filename;
    private String[] headers;
    private boolean invalid = false;

    /**
     * Creates a new CsvFileLoader object.
     *
     * @param filename JAVADOC.
     * @param applicationType JAVADOC.
     * @param headersModifier JAVADOC.
     */
    public CsvFileLoader(String filename, String applicationType, InstanceFactory instanceFactory) {
        this.filename = filename;
        this.applicationType = applicationType;
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param headersModifier JAVADOC.
     */
    public void setHeadersModifier(HeadersModifier headersModifier) {
        this.headersModifier = headersModifier;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (StringUtils.isEmpty(filename) || filename.trim().isEmpty()) {
            // could be intentional
            LOG.info("Given filename is empty");
            invalid = true;

            return;
        }

        File test = new File(filename);

        if (!test.exists() || !test.isFile()) {
            LOG.warn("Given filename is invalid:" + filename);
            this.filename = null;
            invalid = true;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public FileLoaderContainer next() {
        if (invalid) {
            return null;
        }

        Assert.notNull(findReader(), "csvReader is null for filename '" + filename + "'");

        try {
            loadHeaders();

            String[] line = findReader().readNext();

            if (line == null) {
                findReader().close();
                reset();

                return null;
            }

            return new FileLoaderContainer(applicationType, headers, line);
        } catch (Exception e) {
            LOG.error("Oops", e);

            return null;
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void reset() {
        headers = null;
        csvReader = null;
    }

    private CSVReader findReader() {
        if (csvReader == null) {
            try {
                csvReader = new CSVReader(new FileReader(filename));

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Opened csvReader for file " + filename);
                }
            } catch (FileNotFoundException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Failed to open file " + filename + ", skipping execution", e);
                } else {
                    LOG.warn("Failed to open file " + filename + ", skipping execution");
                }
            }
        }

        return csvReader;
    }

    private void loadHeaders()
        throws Exception {
        if (headers == null) {
            headers = findReader().readNext();

            if ((headers == null) || (headers.length == 0)) {
                LOG.warn("The file " + filename + "appears to be empty");

                throw new IllegalArgumentException();
            }

            processHeaders();
        }
    }

    private void processHeaders() {
        if (headersModifier != null) {
            Class<?> entityClass = instanceFactory.getClassType(applicationType);

            Assert.notNull(entityClass,
                "Failed to find class from applicationType '" + applicationType + "'");

            headers = headersModifier.modifyHeaders(headers, entityClass);
        }
    }
}
