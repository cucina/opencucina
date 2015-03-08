package org.cucina.i18n.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.repository.ListNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueListNodeValidator
    implements ConstraintValidator<UniqueListNode, ListNode> {
    private ListNodeRepository listNodeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageDao JAVADOC.
     */
    @Autowired
    public void setListNodeDao(ListNodeRepository listNodeRepository) {
        this.listNodeRepository = listNodeRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNode JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(ListNode listNode, ConstraintValidatorContext arg1) {
        if (listNodeRepository == null) {
            return true;
        }

        return !listNodeRepository.exists(listNode);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param uniqueMessageCode JAVADOC.
     */
    @Override
    public void initialize(UniqueListNode uniqueListNode) {
    }
}
