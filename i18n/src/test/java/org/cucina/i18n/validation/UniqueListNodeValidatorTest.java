package org.cucina.i18n.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.repository.ListNodeRepository;
import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueListNodeValidatorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsValid() {
        ListNode listNode = new ListNode();
        UniqueListNodeValidator validator = new UniqueListNodeValidator();
        ListNodeRepository listNodeRepository = mock(ListNodeRepository.class);

        when(listNodeRepository.exists(listNode)).thenReturn(false);
        validator.setListNodeDao(listNodeRepository);
        assertTrue("Should be unique", validator.isValid(listNode, null));
        when(listNodeRepository.exists(listNode)).thenReturn(true);
        assertFalse("Should not be unique", validator.isValid(listNode, null));
    }
}
