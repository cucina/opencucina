package org.cucina.eggtimer.service;

import java.util.Calendar;
import java.util.Date;

import org.cucina.eggtimer.testassist.JpaProvider;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JpaTemporalRepositorySystemTest
    extends JpaProvider {
    private JpaTemporalRepository repo;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testBefore() {
        assertFalse(repo.beforeCurrentDate(new Date()));

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);
        assertTrue(repo.beforeCurrentDate(cal.getTime()));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testBeforeDouble() {
        getEntityManager().persist(new TimeAtom());
        assertFalse(repo.beforeCurrentDate(new Date()));

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);
        assertTrue(repo.beforeCurrentDate(cal.getTime()));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    protected void onSetUp()
        throws Exception {
        // TODO Auto-generated method stub
        super.onSetUp();
        repo = new JpaTemporalRepository();
        repo.setEntityManager(getEntityManager());
    }
}
