package org.cucina.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.ListNode;
import org.cucina.core.model.Message;
import org.cucina.core.model.MutableI18nMessage;
import org.cucina.core.repository.MessageRepository;
import org.cucina.core.service.I18nService;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.loader.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListNodeMessageBeanPopulatorTest {
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private I18nService i18nService;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private MessageRepository messageRepository;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(instanceFactory.getBean(any(String.class))).thenReturn(new MutableI18nMessage());
        when(messageRepository.getDefaultLocale()).thenReturn(Locale.ENGLISH);
        when(beanFactory.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(instanceFactory);
        when(beanFactory.getBean(SingletonBeanFactory.I18N_SERVICE_ID)).thenReturn(i18nService);
        when(beanFactory.getBean(SingletonBeanFactory.MESSAGE_REPOSITORY_ID))
            .thenReturn(messageRepository);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testArgsMissing() {
        ListNodeMessageBeanPopulator populator = new ListNodeMessageBeanPopulator();
        ListNode listNode = new ListNode();

        populator.populate(listNode, null);
        assertNull(listNode.getLabel());

        Message label = new Message();

        listNode.setLabel(label);
        populator.populate(listNode, null);
        assertNull(listNode.getLabel().getMessageCd());

        String type = "Blaah";

        listNode.setType(type);
        populator.populate(listNode, null);
        assertNull(listNode.getLabel().getMessageCd());

        label.setMessageTx("This is a message", "en");
        populator.populate(listNode, null);
        assertNotNull(listNode.getLabel().getMessageCd());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testMessageCdAlreadySet() {
        ListNode listNode = new ListNode();
        Message label = new Message();

        listNode.setLabel(label);
        label.setMessageTx("This is a message", "en");

        String code = "code";

        label.setMessageCd(code);

        String type = "Blaah";

        listNode.setType(type);

        ListNodeMessageBeanPopulator populator = new ListNodeMessageBeanPopulator();

        populator.populate(listNode, null);
        assertNotNull(listNode.getLabel().getMessageCd());
        assertNotNull(listNode.getLabel().getBaseName());
        assertEquals(listNode.getLabel().getMessageCd(), code);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoExceptionNotListNode() {
        ListNodeMessageBeanPopulator populator = new ListNodeMessageBeanPopulator();

        populator.populate(null, null);
        populator.populate(new Foo(), null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSunnyDay() {
        ListNode listNode = new ListNode();
        Message label = new Message();

        listNode.setLabel(label);
        label.setMessageTx("This is a message", "en");

        String type = "Blaah";

        listNode.setType(type);

        ListNodeMessageBeanPopulator populator = new ListNodeMessageBeanPopulator();

        populator.populate(listNode, null);
        assertNotNull(listNode.getLabel().getMessageCd());
        assertNotNull(listNode.getLabel().getBaseName());
    }
}
