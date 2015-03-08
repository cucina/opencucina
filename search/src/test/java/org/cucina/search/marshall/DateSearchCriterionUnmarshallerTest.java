
package org.cucina.search.marshall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.criterion.AbstractDateSearchCriterion;
import org.cucina.search.query.criterion.DateRelativeSearchCriterion;
import org.cucina.search.query.criterion.DateSearchCriterion;
import org.junit.Before;
import org.junit.Test;


/**
 * Test unmarshalling of DateSearchCriterionUnmarshaller functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateSearchCriterionUnmarshallerTest {
    private static final String NAME = "name";
    private static final String ALIAS = "alias";
    private static final String ROOT_TYPE = "rootType";
    private static final String ROOT_ALIAS = "rootAlias";
    private DateSearchCriterionUnmarshaller unmarshaller;

    /**
     * Can't mix relative date with date
     */
    @Test(expected = IllegalArgumentException.class)
    public void cantMixRelativeWithDate() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), "+1d");
        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY),
            Calendar.getInstance().getTime());

        unmarshaller.unmarshall(NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);
    }

    /**
     * Can't mix relative date with numeric string
     */
    @Test(expected = IllegalArgumentException.class)
    public void cantMixRelativeWithNumeric() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), "+1d");
        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY),
            Long.toString(Calendar.getInstance().getTime().getTime() / 1000));

        unmarshaller.unmarshall(NAME, ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);
    }

    /**
     * Set up for test.
     */
    @Before
    public void setup() {
        unmarshaller = new DateSearchCriterionUnmarshaller();
    }

    /**
     * unmarshall
     */
    @SuppressWarnings("deprecation")
    @Test
    public void unmarshall() {
        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();

        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), from);
        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY), to);

        DateSearchCriterion searchCriterion = (DateSearchCriterion) unmarshaller.unmarshall(NAME,
                ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

        assertNotNull("searchCriterion must be returned", searchCriterion);

        Date toEOD = searchCriterion.getTo();

        assertEquals("Should be end of day", 23, toEOD.getHours());
        assertEquals("Should be end of day", 59, toEOD.getMinutes());
        assertEquals("Should be end of day", 59, toEOD.getSeconds());
    }

    /**
     * unmarshal by getting the property just by the alias which should be a
     * <code>Map</code>.
     */
    @Test
    public void unmarshallMap() {
        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();

        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(SearchCriterionMarshaller.FROM_PROPERTY, from);
        criteria.put(SearchCriterionMarshaller.TO_PROPERTY, to);

        Map<String, Object> marshalledCriteria = Collections.<String, Object>singletonMap(ALIAS,
                criteria);
        AbstractDateSearchCriterion searchCriterion = (AbstractDateSearchCriterion) unmarshaller.unmarshall(NAME,
                ALIAS, ROOT_TYPE, ROOT_ALIAS, marshalledCriteria);

        assertNotNull("searchCriterion must be returned", searchCriterion);
    }

    /**
     * unmarshall
     */
    @Test
    public void unmarshallNumeric() {
        Calendar cal = Calendar.getInstance();
        Date to = cal.getTime();

        cal.add(Calendar.DATE, -1);

        Date from = cal.getTime();

        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY),
            Long.toString(from.getTime() / 1000));
        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY),
            Long.toString(to.getTime() / 1000));

        AbstractDateSearchCriterion searchCriterion = (AbstractDateSearchCriterion) unmarshaller.unmarshall(NAME,
                ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

        assertNotNull("searchCriterion must be returned", searchCriterion);
    }

    /**
     * unmarshall
     */
    @Test
    public void unmarshallRelativeDate() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), "+1d");
        criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY), "+11m");

        DateRelativeSearchCriterion searchCriterion = (DateRelativeSearchCriterion) unmarshaller.unmarshall(NAME,
                ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

        assertNotNull("searchCriterion must be returned", searchCriterion);
    }
}
