package org.cucina.i18n.validation;

import org.cucina.i18n.model.ListItem;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.ListItemRepository;
import org.cucina.i18n.service.I18nService;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UniqueListItemValidatorTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsValid() {
		ListItem listNode = new ListItem();

		listNode.setType("listNode");

		Message label = mock(Message.class);

		when(label.getBestMessage(Locale.UK)).thenReturn("xxx");

		listNode.setLabel(label);

		UniqueListItemValidator validator = new UniqueListItemValidator();
		ListItemRepository listNodeRepository = mock(ListItemRepository.class);

		Collection<ListItem> nodes = new ArrayList<ListItem>();

		when(listNodeRepository.findByType("listNode")).thenReturn(nodes);

		ReflectionTestUtils.setField(validator, "listNodeRepository", listNodeRepository);

		I18nService i18nService = mock(I18nService.class);

		when(i18nService.getLocale()).thenReturn(Locale.UK);

		ReflectionTestUtils.setField(validator, "i18nService", i18nService);

		assertTrue("Should be unique", validator.isValid(listNode, null));

		ListItem ln = new ListItem();

		ln.setLabel(label);
		nodes.add(ln);
		assertFalse("Should not be unique", validator.isValid(listNode, null));
	}
}
