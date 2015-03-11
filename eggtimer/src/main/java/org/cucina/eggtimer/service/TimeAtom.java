package org.cucina.eggtimer.service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity
public class TimeAtom {
    private int one = 1;

    /**
     * JAVADOC Method Level Comments
     *
     * @param one JAVADOC.
     */
    public void setOne(int one) {
        this.one = one;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Id
    @GeneratedValue
    public int getOne() {
        return one;
    }
}
