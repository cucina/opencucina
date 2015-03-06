package org.cucina.eggtimer.service;

import java.util.Collection;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ScheduleRepository
    extends Repository<ScheduleRequest, String> {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    Collection<ScheduleRequest> findAll();

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     *
     * @return JAVADOC.
     */
    ScheduleRequest findById(String name);

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    @Transactional
    void removeByName(String name);

    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     */
    @Transactional
    void save(ScheduleRequest request);
}
