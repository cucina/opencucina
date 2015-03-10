package org.cucina.core.repository;

import java.util.Collection;

import org.springframework.data.repository.Repository;

import org.cucina.core.model.Attachment;


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
     *
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
