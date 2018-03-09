package org.cucina.core.repository;

import org.cucina.core.model.Attachment;
import org.springframework.data.repository.Repository;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface AttachmentRepository
		extends Repository<Attachment, Long> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	Collection<Attachment> findAll();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	Attachment findById(Long id);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param attachment JAVADOC.
	 */
	void save(Attachment attachment);
}
