package org.cucina.email.repository;

import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author vlevine
  */
public interface TemplateRepository
    extends Repository<EmailTemplate, Long> {
    EmailTemplate findById(Long id);

    EmailTemplate findByName(String name);

    void save(EmailTemplate template);
}
