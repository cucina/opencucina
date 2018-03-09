package org.cucina.search.marshall;


/**
 * NumberSearchCriterionUnmarshaller for Doubles.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DoubleSearchCriterionUnmarshaller
		extends NumberSearchCriterionUnmarshaller {
	/*
	 * (non-Javadoc)
	 * @see org.cucina.meringue.search.support.NumberSearchCriterionUnmarshaller#parseNumber(java.lang.Number)
	 */

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param number JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected Number parseNumber(Number number) {
		if (number instanceof Double) {
			return number;
		}

		return Double.valueOf(number.toString());
	}

	/**
	 * Parses number to Double and returns
	 *
	 * @param number String representation of double.
	 * @return Double.
	 */
	@Override
	protected Number parseNumber(String number) {
		return Double.valueOf(number);
	}
}
