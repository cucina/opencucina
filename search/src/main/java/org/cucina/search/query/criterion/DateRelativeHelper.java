
package org.cucina.search.query.criterion;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.CharUtils;
import org.springframework.util.Assert;


/**
 * Helper which converts relative date syntax into actual dates.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DateRelativeHelper {
    /** This is a field JAVADOC */
    public static final String TODAY = "today";

    /** This is a field JAVADOC */
    public static final char DAY = 'd';

    /** This is a field JAVADOC */
    public static final char MONTH = 'm';

    /** This is a field JAVADOC */
    public static final char YEAR = 'y';

    /** This is a field JAVADOC */
    public static final char MINUS = '-';

    /** This is a field JAVADOC */
    public static final char PLUS = '+';
    
    private static DateRelativeHelper instance = null; 
    

    public static DateRelativeHelper getInstance(){
    	if ( instance == null ){
    		instance = new DateRelativeHelper();
    	}
    	return instance;
    }
    
    /**
     * Parse the relative syntax into a Date instance.
     * @param dateValue
     * @param startDay
     * @return
     */
    public Date parseDate(String dateValue, boolean startDay) {
        int field = -1;
        Calendar cal = Calendar.getInstance();
        
        if (!dateValue.equalsIgnoreCase(TODAY)) {
            StringBuffer numericBuf = new StringBuffer();

            for (int i = 0; i < dateValue.length(); i++) {
                char ch = dateValue.charAt(i);

                if (i == 0) {
                    if (ch == MINUS) {
                        numericBuf.append(ch);
                    }

                    if ((ch == PLUS) || (ch == MINUS)) {
                        continue;
                    }
                }

                if (CharUtils.isAsciiNumeric(ch)) {
                    numericBuf.append(ch);
                } else if (ch == DAY) {
                    field = Calendar.DATE;
                } else if (ch == MONTH) {
                    field = Calendar.MONTH;
                } else if (ch == YEAR) {
                    field = Calendar.YEAR;
                }
            }

            Assert.isTrue(field != -1, "There was no field provided in [" + dateValue + "]");
            Assert.isTrue(numericBuf.length() > 0,
                "Numeric value not provided in [" + dateValue +
                "], needs to have sign and numeric value");

            cal.add(field, Integer.parseInt(numericBuf.toString()));
        }

        if (startDay) {
            //Set to start of day
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } else {
            //Set to end of day
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        }

        return cal.getTime();
    }
}
