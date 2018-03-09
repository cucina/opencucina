package org.cucina.engine.converter;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.schedule.CronSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;


/**
 * Converter of String of format <name>:<workflowId>:<transition>:<cron>
 * into a CronSlot.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CronSlotConverter
		implements Converter<String, CronSlot> {
	private static final Logger LOG = LoggerFactory.getLogger(CronSlotConverter.class);

	/**
	 * Convert String into CronSlot, barfs if String split on ':' does not result in
	 * at least two tokens.
	 *
	 * @param source String.
	 * @return WorkflowTransitionWrapper.
	 */
	@Override
	public CronSlot convert(String source) {
		if (StringUtils.isNotEmpty(source)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Convert into a cron slot [" + source + "]");
			}

			String[] values = source.split(":");

			Assert.isTrue(values.length == 4,
					"Cron slot value must be of the format <name>:<workflowId>:<transition>:<cron>, value [" +
							source + "]");

			return new CronSlot(values[0], values[1], values[2], values[3]);
		}

		return null;
	}
}
