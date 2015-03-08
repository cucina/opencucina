
package org.cucina.search.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.cucina.search.query.criterion.TextSearchCriterion;
import org.cucina.search.query.projection.MaxProjection;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.junit.Before;
import org.junit.Test;


/**
 * Test that SearchBean works correctly
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBeanTest {
    private static final String TYPE1 = "type1";
    private static final String TYPE2 = "type2";
    private static final String ALIAS1 = "alias1";
    private static final String ALIAS2 = "alias2";
    private Projection proj1Type1;
    private Projection proj1Type2;
    private Projection proj2Type1;
    private SearchBean searchBean;
    private SearchCriterion crit1Type1;
    private SearchCriterion crit1Type2;
    private SearchCriterion crit2Type2;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void getCriterion() {
        Projection uniqueProj = new SimplePropertyProjection("mine", "theres", "ours");

        searchBean.addProjection(uniqueProj);
        assertEquals("Incorrect criterion", uniqueProj, searchBean.getProjection("mine"));
        assertNull("Shouldn't return projection", searchBean.getProjection("madeup"));
    }

    /**
     * Test getProjectios with alias with no projections
     */
    @Test
    public void getNoProjections() {
        Collection<Projection> projs = searchBean.getProjections("made up");

        assertNotNull("should have returned projs", projs);
        assertEquals("incorrect number projs", 0, projs.size());
    }

    /**
     * Test getCriteria with alias with no SearchCriterion
     */
    @Test
    public void getNoSpecificCriterion() {
        Collection<SearchCriterion> crits = searchBean.getCriteria("made up");

        assertNotNull("should have returned crits", crits);
        assertEquals("incorrect number crits", 0, crits.size());
    }

    /**
     * Test getCriteria with alias method returns correct SearchCriterion
     */
    @Test
    public void getSpecificCriterion() {
        Collection<SearchCriterion> crits = searchBean.getCriteria(ALIAS2);

        assertNotNull("should have returned crits", crits);
        assertEquals("incorrect number crits", 2, crits.size());
        assertTrue("Should contain crit1Type2", crits.contains(crit1Type2));
        assertTrue("Should contain crit2Type2", crits.contains(crit2Type2));
    }

    /**
     * Test getProjections with alias method returns correct projections
     */
    @Test
    public void getSpecificProjections() {
        Collection<Projection> projs = searchBean.getProjections(ALIAS1);

        assertNotNull("should have returned projs", projs);
        assertEquals("incorrect number projs", 2, projs.size());
        assertTrue("Should contain proj1Type1", projs.contains(proj1Type1));
        assertTrue("Should contain proj1Type2", projs.contains(proj2Type1));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void cloneBean() {
        //add some order bys to check it copies across
        searchBean.setOrder(new ArrayList<OrderBy>());
        searchBean.getOrder().add(new OrderBy("that", true));

        SearchBean clonedBean = (SearchBean) searchBean.clone();

        assertEquals("Should have same aliases", searchBean.getAliasByType(),
            clonedBean.getAliasByType());
        assertNotSame("Should not be same aliases map", searchBean.getAliasByType(),
            clonedBean.getAliasByType());
        assertEquals("Should have same criteria", searchBean.getCriteria(), clonedBean.getCriteria());
        assertNotSame("Should not be same criteria collection", searchBean.getCriteria(),
            clonedBean.getCriteria());
        assertEquals("Should have same order", searchBean.getOrder(), clonedBean.getOrder());
        assertNotSame("Should not be same order collection", searchBean.getOrder(),
            clonedBean.getOrder());
        assertEquals("Should have same projections", searchBean.getProjections(),
            clonedBean.getProjections());
        assertNotSame("Should not be same projections collection", searchBean.getProjections(),
            clonedBean.getProjections());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void hasAggregate() {
        assertEquals("No aggregate", 0, searchBean.aggregateProjectionCount());
        searchBean.addProjection(new MaxProjection("name", "alias", "root"));
        assertEquals("Should have aggregate", 1, searchBean.aggregateProjectionCount());
    }

    /**
     * Set up test
     */
    @Before
    public void onsetup() {
        LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

        aliasByType.put(TYPE1, ALIAS1);
        aliasByType.put(TYPE2, ALIAS2);

        searchBean = new SearchBean();
        searchBean.setAliasByType(aliasByType);

        proj1Type1 = new SimplePropertyProjection("name1", "alias1", ALIAS1);
        searchBean.addProjection(proj1Type1);
        proj2Type1 = new SimplePropertyProjection("name1", "alias1", ALIAS1);
        searchBean.addProjection(proj2Type1);
        proj1Type2 = new SimplePropertyProjection("name2", "alias2", ALIAS2);
        searchBean.addProjection(proj1Type2);

        crit1Type1 = new TextSearchCriterion("name1", null, ALIAS1, "");
        searchBean.addCriterion(crit1Type1);
        crit1Type2 = new TextSearchCriterion("name2", null, ALIAS2, "");
        searchBean.addCriterion(crit1Type2);
        crit2Type2 = new TextSearchCriterion("name2", null, ALIAS2, "");
        searchBean.addCriterion(crit2Type2);
    }
}
