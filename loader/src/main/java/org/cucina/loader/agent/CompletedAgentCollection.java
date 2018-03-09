package org.cucina.loader.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.concurrent.*;


/**
 * Ensures that all Threads created by any Executors are finished/ended before continuing.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompletedAgentCollection
		implements Agent {
	private static final Logger LOG = LoggerFactory.getLogger(CompletedAgentCollection.class);
	private ExecutorService executorService;
	private List<Agent> executors;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param executorService JAVADOC.
	 */
	@Required
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * Set executors
	 *
	 * @param executors List<Executor>.
	 */
	@Required
	public void setExecutors(List<Agent> executors) {
		this.executors = executors;
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Override
	public void execute() {
		CompletionService<Agent> completionService = new ExecutorCompletionService<Agent>(executorService);

		for (Agent executor : executors) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing executor:" + executor);
			}

			completionService.submit(new CallableExecutor(executor));
		}


		for (int t = 0, n = executors.size(); t < n; t++) {
			try {
				Future<Agent> f = completionService.take();

				Agent e = f.get();

				if (LOG.isDebugEnabled()) {
					LOG.debug("Finished executor: " + e);
				}
			} catch (InterruptedException e) {
				LOG.warn("Oops", e);
			} catch (ExecutionException e) {
				LOG.warn("Oops", e);
			}
		}
	}

	private static class CallableExecutor
			implements Callable<Agent> {
		private Agent executor;

		public CallableExecutor(Agent executor) {
			this.executor = executor;
		}

		@Override
		public Agent call()
				throws Exception {
			executor.execute();

			return executor;
		}
	}
}
