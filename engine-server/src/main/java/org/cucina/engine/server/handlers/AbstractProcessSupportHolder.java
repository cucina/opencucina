package org.cucina.engine.server.handlers;

import org.cucina.engine.service.ProcessSupportService;
import org.springframework.util.Assert;


/**
 * @param <T> .
 * @author vlevine
 */
public abstract class AbstractProcessSupportHolder {
	private ProcessSupportService processSupportService;

	public AbstractProcessSupportHolder(ProcessSupportService processSupportService) {
		Assert.notNull(processSupportService, "processSupportService is null");
		this.processSupportService = processSupportService;
	}

	protected ProcessSupportService getProcessSupportService() {
		return processSupportService;
	}
}
