package org.cucina.core.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.cucina.core.model.Attachment;
import org.cucina.core.repository.AttachmentRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Controller
public class AttachmentController {
    private AttachmentRepository attachmentRepository;

    /**
     * Creates a new AttachmentController object.
     *
     * @param attachmentRepository JAVADOC.
     */
    @Autowired
    public AttachmentController(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @RequestMapping
    public Attachment get(Long id) {
        return attachmentRepository.findById(id);
    }
}
