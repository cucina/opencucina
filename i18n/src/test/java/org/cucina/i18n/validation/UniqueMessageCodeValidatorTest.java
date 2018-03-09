package org.cucina.i18n.validation;

import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.cucina.i18n.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UniqueMessageCodeValidatorTest {
	@Mock
	private MessageRepository messageRepository;
	@Mock
	private UniqueMessageCode uniqueMessageCode;
	private UniqueMessageCodeValidator validator;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		validator = new UniqueMessageCodeValidator();

		validator.setMessageRepository(messageRepository);
		validator.initialize(uniqueMessageCode);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void test() {
		//        when(uniqueMessageCode.basename()).thenReturn("messages.listnode");
		when(uniqueMessageCode.property()).thenReturn("message");

		Foo foo = new Foo();
		Message message = new Message();

		foo.setMessage(message);
		message.setBaseName("messages.listnode");
		message.setMessageCd("CODE");
		when(messageRepository.findByBasenameAndCode("messages.listnode", "CODE")).thenReturn(null);
		assertTrue("Should be unique", validator.isValid(foo, null));
		when(messageRepository.findByBasenameAndCode("messages.listnode", "CODE"))
				.thenReturn(new Message());
		assertFalse("Should not be unique", validator.isValid(foo, null));

		message.setMessageCd(null);
		assertFalse("Should return false when code is null", validator.isValid(foo, null));

		assertFalse("Should return false when obj is null", validator.isValid(null, null));

		foo.setMessage(null);
		assertFalse("Should return false when msg is null", validator.isValid(foo, null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testEmptyBasename() {
		when(uniqueMessageCode.property()).thenReturn("message");

		Foo foo = new Foo();
		Message message = new Message();

		foo.setMessage(message);
		assertFalse("Should return false when basename is null", validator.isValid(foo, null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testEmptyProperty() {
		Foo foo = new Foo();
		Message message = new Message();

		message.setBaseName("basename");
		foo.setMessage(message);
		assertFalse("Should return false when property is null", validator.isValid(foo, null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testPropertyNotRightType() {
		when(uniqueMessageCode.property()).thenReturn("id");

		Foo foo = new Foo();

		foo.setId(1L);

		assertFalse("Should return false when property is wrong type", validator.isValid(foo, null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testWrongPropertyName() {
		when(uniqueMessageCode.property()).thenReturn("messagexx");

		Foo foo = new Foo();

		assertFalse("Should return false when property is wrong name", validator.isValid(foo, null));
	}
}
