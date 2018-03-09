package org.cucina.loader;

import au.com.bytecode.opencsv.CSVReader;
import org.cucina.cluster.ChangeNotifier;
import org.cucina.core.InstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvLoader
		implements Loader {
	private static final Logger LOG = LoggerFactory.getLogger(CsvLoader.class);
	private ChangeNotifier changeNotifier;
	private CsvRowLoader rowProcessor;
	private HeadersModifier headersModifier;
	private InstanceFactory instanceFactory;
	private LoaderExceptionFactory loaderExceptionFactory;

	/**
	 * Creates a new CsvLoader object.
	 *
	 * @param instanceFactory    JAVADOC.
	 * @param persistenceService JAVADOC.
	 */
	public CsvLoader(InstanceFactory instanceFactory, CsvRowLoader rowProcessor,
					 LoaderExceptionFactory loaderExceptionFactory, ChangeNotifier changeNotifier) {
		Assert.notNull(instanceFactory, "instanceFactory is null");
		this.instanceFactory = instanceFactory;
		Assert.notNull(rowProcessor, "rowProcessor is null");
		this.rowProcessor = rowProcessor;
		Assert.notNull(loaderExceptionFactory, "loaderExceptionFactory is null");
		this.loaderExceptionFactory = loaderExceptionFactory;
		Assert.notNull(changeNotifier, "changeNotifier is null");
		this.changeNotifier = changeNotifier;
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
	 * @param applicationType JAVADOC.
	 * @param attachment      JAVADOC.
	 * @throws BindException
	 */
	@Override
	public int loadCollection(String applicationType, byte[] data)
			throws LoaderException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Attempted to load collection of '" + applicationType + "'");
		}

		BindException bindException = new BindException(data, applicationType);
		Object entity = instanceFactory.getBean(applicationType);

		if (entity == null) {
			LOG.warn("Cannot instantiate bean of type '" + applicationType + "'");
			bindException.reject("loader.applicationType", new Object[]{applicationType},
					"Applicationtype '" + applicationType + "' is invalid");
			throw toLoaderException(bindException);
		}

		CSVReader csvReader = null;
		int i = 0;

		try {
			//we want to delay notification of changes to objects
			//until all rows have been processed
			changeNotifier.setProgrammatic();

			csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data), "UTF-8"));

			String[] headers = csvReader.readNext();

			if ((headers == null) || (headers.length == 0)) {
				bindException.reject("loader.noHeaders", "The input file is empty");
				throw bindException;
			}

			if (headersModifier != null) {
				headers = headersModifier.modifyHeaders(headers, entity.getClass());
			}

			String[] line = null;

			while ((line = csvReader.readNext()) != null) {
				// assume that headers are property names
				i++;
				rowProcessor.processRow(applicationType, headers, line, i);
			}
		} catch (BindException be) {
			throw toLoaderException(be);
		} catch (Exception e) {
			bindException.reject("loader.exception", new Object[]{i, e.getMessage()},
					e.getMessage());
			throw toLoaderException(bindException);
		} finally {
			changeNotifier.sendEvent();

			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					// ignore it for now
				}
			}
		}

		return i;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param be JAVADOC.
	 * @return JAVADOC.
	 */
	public LoaderException toLoaderException(BindException be) {
		return loaderExceptionFactory.getLoaderException(be);
	}
}
