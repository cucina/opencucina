package org.cucina.engine.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.Repository;
import org.cucina.engine.model.HistoryRecord;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
  */
public interface HistoryRecordRepository
    extends Repository<HistoryRecord, Long> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     * @param applicationType JAVADOC.
     *
     * @return JAVADOC.
     */
    List<HistoryRecord> findByIdAndApplicationType(Serializable id, String applicationType);
}
