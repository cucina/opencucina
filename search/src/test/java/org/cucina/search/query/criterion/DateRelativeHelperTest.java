
package org.cucina.search.query.criterion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Test that the DateRelativeSearchCriterion functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateRelativeHelperTest {
    private DateRelativeHelper helper;

    /**
     * Sets up test
     */
    @Before
    public void setup() {
        helper = DateRelativeHelper.getInstance();
    }
    
    @Test 
    public void testSameInstance(){
    	assertTrue("Should be same instance", helper == DateRelativeHelper.getInstance());
    }

    /**
     * Test minus one day to plus one month
     */
    @Test
    public void testMinusDayAndPlusMonth() {
        List<Date> dates = new ArrayList<Date>();
        dates.add(helper.parseDate("-11d", true));
        dates.add(helper.parseDate("+5m", false));

        Calendar calFrom = getStartOfToday();

        calFrom.add(Calendar.DATE, -11);

        Calendar calTo = getEndOfToday();

        calTo.add(Calendar.MONTH, 5);

        check(dates, calFrom, calTo);
    }

    

    /**
     * Test from minus years and to minus months
     */
    @Test
    public void testMinusYearAndMonth() {
    	
        List<Date> dates = new ArrayList<Date>();
        dates.add(helper.parseDate("-8y", true));
        dates.add(helper.parseDate("-5m", false));
        
        Calendar calFrom = getStartOfToday();

        calFrom.add(Calendar.YEAR, -8);

        Calendar calTo = getEndOfToday();

        calTo.add(Calendar.MONTH, -5);

        check(dates, calFrom, calTo);
    }

    /**
     * Test that a number to increment by is required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoNumeric() {
        
        helper.parseDate("-y", true);
    }

    /**
     * Test no unit marker barfs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoPeriod() {

        helper.parseDate("-2", true);
    }


    

    private Calendar getEndOfToday() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar;
    }

    private Calendar getStartOfToday() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private void check(List<Date> dates, Calendar calFrom, Calendar calTo) {
        assertNotNull("Should have returned dates", dates);
        assertEquals("Incorrect number of dates", 2, dates.size());

        Iterator<Date> it = dates.iterator();
        Date from = it.next();
        Date to = it.next();

        Calendar comparison = Calendar.getInstance();

        comparison.setTime(from);

        assertEquals("From is incorrect", calFrom, comparison);

        comparison.setTime(to);

        assertEquals("To is incorrect", calTo, comparison);
    }
}
