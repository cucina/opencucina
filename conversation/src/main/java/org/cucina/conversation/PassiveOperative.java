package org.cucina.conversation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import java.util.HashMap;


/**
 * Simply waits on the callback channel until it stops reveiving.
 *
 * @author vlevine
 */
public class PassiveOperative
		extends AbstractOperative {
	private static final Logger LOG = LoggerFactory.getLogger(PassiveOperative.class);

	/**
	 * @param request .
	 * @return .
	 */
	@Override
	public Message<?> process(Message<?> request) {
		try {
			loopProcess(new HashMap<String, Object>());
		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Ignoring", e);
			}
		}

		return null;
	}
}
