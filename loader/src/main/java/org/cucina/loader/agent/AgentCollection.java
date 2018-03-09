package org.cucina.loader.agent;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AgentCollection
		implements Agent {
	private static final Logger LOG = LoggerFactory.getLogger(AgentCollection.class);
	private Agent output;
	private List<Agent> executors;

	/**
	 * Creates a new ExecutorCollection object.
	 *
	 * @param executors JAVADOC.
	 */
	public AgentCollection(List<Agent> executors) {
		this.executors = executors;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param output JAVADOC.
	 */
	public void setOutput(Agent output) {
		this.output = output;
	}

	/**
	 * Executes each executor in turn. Note that this does not have
	 * <code>@Transactional</code> annotation, but each executor should have
	 * their own. Reason being that datasets expected could be rather large and
	 * it is probably beneficial to commit them at some points.
	 * <p>
	 * TODO handle exception - is it tolerant or not.
	 */
	@Override
	public void execute() {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		for (Agent executor : executors) {
			if (executor instanceof Router) {
				Router router = (Router) executor;

				//if route returns false, we are running the alternative executor
				if (!router.route()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug(
								"Running alternative executors, will not run any more in current collection when these are finished");
					}

					try {
						runAlternative(router);
					} catch (RuntimeException e) {
						LOG.error("Caught exception in alternative executor ", e);
						throw e;
					}

					if (LOG.isDebugEnabled()) {
						LOG.debug("No further executions from this collection " + this +
								", returning");
					}

					return;

					//if it returns true, continue. currently I am not running the executor
					//execute method on the router, but this can be changed if nec, by removing the else block below
				}

				//proceed, move onto the next one
				if (LOG.isDebugEnabled()) {
					LOG.debug(
							"Router has returned true, carrying on with next executor in collection");
				}

				continue;
			}

			StopWatch localSw = new StopWatch();

			localSw.start();

			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing executor:" + executor);
			}

			try {
				executor.execute();
			} catch (RuntimeException e) {
				LOG.error("Caught exception in executor ", e);
				throw e;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Completed executor in " + localSw.getTime() + "ms");
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Completed all executors in " + stopWatch.getTime() + "ms");
		}

		if (output != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing output executor");
			}

			StopWatch localSw = new StopWatch();

			localSw.start();

			try {
				output.execute();
			} catch (RuntimeException e) {
				LOG.error("Caught exception in output executor ", e);
				throw e;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Completed output executor in " + localSw.getTime() + "ms");
			}
		}
	}

	private void runAlternative(Router router) {
		StopWatch altSw = new StopWatch();

		altSw.start();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Router has returned false. Running alternative executor from router " +
					router);
		}

		router.runAlternative();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Completed alternative executor in " + altSw.getTime() + "ms");
		}
	}
}
