package org.cucina.loader;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.service.ContextService;
import org.cucina.loader.agent.ManagedPagingParallelAgent;
import org.cucina.loader.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractFileLoaderExecutor<T>
		extends ManagedPagingParallelAgent<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractFileLoaderExecutor.class);
	private transient String filename;
	private boolean complete = false;

	/**
	 * Creates a new AbstractFileLoaderExecutor object.
	 *
	 * @param contextService  JAVADOC.
	 * @param processor       JAVADOC.
	 * @param instanceFactory JAVADOC.
	 */
	public AbstractFileLoaderExecutor(ContextService contextService, Processor processor) {
		super(contextService, processor);
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

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	protected String getFilename() {
		return filename;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param filename JAVADOC.
	 * @throws
	 */
	public void setFilename(String filename) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Setting filename to " + filename);
		}

		Assert.notNull(filename, "Filename is null");

		File test = new File(filename);

		if (test.exists() && test.isFile()) {
			this.filename = filename;
			reset();
		} else {
			LOG.warn("Given filename is invalid:" + filename);
			this.filename = null;
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	protected boolean allowStart() {
		if (StringUtils.isEmpty(filename)) {
			LOG.debug("Filename is null, not starting this executor");

			return false;
		}

		return true;
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	protected void reset() {
		complete = false;
	}
}
