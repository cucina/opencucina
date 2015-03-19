package org.cucina.i18n.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.ListNodeRepository;
import org.cucina.i18n.service.I18nService;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        listNode.setType("listNode");

        Message label = mock(Message.class);

        when(label.getBestMessage(Locale.UK)).thenReturn("xxx");

        listNode.setLabel(label);

        UniqueListNodeValidator validator = new UniqueListNodeValidator();
        ListNodeRepository listNodeRepository = mock(ListNodeRepository.class);

        Collection<ListNode> nodes = new ArrayList<ListNode>();

        when(listNodeRepository.findByType("listNode")).thenReturn(nodes);

        ReflectionTestUtils.setField(validator, "listNodeRepository", listNodeRepository);

        I18nService i18nService = mock(I18nService.class);

        when(i18nService.getLocale()).thenReturn(Locale.UK);

        ReflectionTestUtils.setField(validator, "i18nService", i18nService);

        assertTrue("Should be unique", validator.isValid(listNode, null));

        ListNode ln = new ListNode();

        ln.setLabel(label);
        nodes.add(ln);
        assertFalse("Should not be unique", validator.isValid(listNode, null));
    }
}
