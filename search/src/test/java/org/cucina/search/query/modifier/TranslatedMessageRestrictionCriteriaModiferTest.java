
package org.cucina.search.query.modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.cucina.i18n.service.I18nService;
import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.projection.Projection;
import org.cucina.search.query.projection.SimplePropertyProjection;
import org.cucina.search.query.projection.TranslatedMessageWithJoinProjection;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests TranslatedMessageRestrictionCriteriaModifer functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TranslatedMessageRestrictionCriteriaModiferTest {
    private TranslatedMessageRestrictionCriteriaModifer modifier;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        I18nService i18nService = mock(I18nService.class);

        when(i18nService.getLocale()).thenReturn(Locale.ENGLISH);

        modifier = new TranslatedMessageRestrictionCriteriaModifer(i18nService);
    }

    /**
     * Adds restriction criterion for users locale per TranslatedMessageWithJoinProjection.
     */
    @Test
    public void testAddsCriterion() {
        Projection proj1 = new TranslatedMessageWithJoinProjection("tName", "tAlias", "foo");
        Projection proj2 = new TranslatedMessageWithJoinProjection("tName2", "tAlias2", "foo");
        SearchBean searchBean = new SearchBean();

        searchBean.addProjection(new SimplePropertyProjection("name", "any", "foo"));
        searchBean.addProjection(proj1);
        searchBean.addProjection(proj2);

        modifier.modify(searchBean, null);

        Collection<SearchCriterion> criteria = searchBean.getCriteria();

        assertEquals("incorrect number criterion added", 2, criteria.size());

        boolean found1 = false;
        boolean found2 = false;

        proj1.setParentAliases(Collections.singletonMap("foo", "parent1"));
        proj2.setParentAliases(Collections.singletonMap("foo", "parent2"));

        for (SearchCriterion searchCriterion : criteria) {
            if ("parent1".equals(searchCriterion.getParentAliases().get("foo"))) {
                assertEquals("Incorrect rootAlias", "foo", searchCriterion.getRootAlias());
                assertEquals("Incorrect value", "en", searchCriterion.getValues().get(0));
                assertEquals("Incorrect name", "localeCd", searchCriterion.getName());
                found1 = true;
            }

            if ("parent2".equals(searchCriterion.getParentAliases().get("foo"))) {
                assertEquals("Incorrect rootAlias", "foo", searchCriterion.getRootAlias());
                assertEquals("Incorrect value", "en", searchCriterion.getValues().get(0));
                assertEquals("Incorrect name", "localeCd", searchCriterion.getName());
                found2 = true;
            }
        }

        assertTrue("tName criterion not found", found1);
        assertTrue("tName2 criterion not found", found2);
    }

    /**
     * Does nothing if no criteria of interest
     */
    @Test
    public void testIgnoresIrrelevantCriterion() {
        SearchBean searchBean = new SearchBean();

        searchBean.addProjection(new SimplePropertyProjection("name", "any", "foo"));

        modifier.modify(searchBean, null);

        Collection<SearchCriterion> criteria = searchBean.getCriteria();

        assertEquals("incorrect number criterion added", 0, criteria.size());
    }
}
