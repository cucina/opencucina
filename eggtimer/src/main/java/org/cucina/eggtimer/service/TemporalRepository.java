package org.cucina.eggtimer.service;

import java.util.Date;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface TemporalRepository {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    boolean beforeCurrentDate(Date date);
}
