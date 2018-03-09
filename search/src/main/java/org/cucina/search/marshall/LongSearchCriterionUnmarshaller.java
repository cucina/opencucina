package org.cucina.search.marshall;


/**
 * NumberSearchCriterionUnmarshaller for Longs.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LongSearchCriterionUnmarshaller
		extends NumberSearchCriterionUnmarshaller {
	/**
	 * Ensures that we return the correct type.
	 */
	@Override
	protected Number parseNumber(Number number) {
		if (number instanceof Long) {
			return number;
		}

		return Long.valueOf(number.longValue());
	}

	/**
	 * Parses number to Long and returns
	 *
	 * @param number String representation of long.
	 * @return Long.
	 */
	@Override
	protected Number parseNumber(String number) {
		return Long.valueOf(number);
	}
}
