package org.cucina.engine.repository;

import org.cucina.engine.model.HistoryRecord;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;


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
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	List<HistoryRecord> findByIdAndApplicationType(Serializable id, String applicationType);
}
