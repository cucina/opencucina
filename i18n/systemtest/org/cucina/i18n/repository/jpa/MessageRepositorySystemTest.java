package org.cucina.i18n.repository.jpa;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.BeanFactory;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.cucina.i18n.testassist.JpaProvider;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessageRepositorySystemTest
    extends JpaProvider {
    private static final String MIKE_CODE = "mike";
    private static final String VIKTOR_CODE = "viktor";
    private static final String ED_BASENAME = "ed";
    private static final String CRAIG_BASENAME = "pond";
    private static final String MIKE_ED_TEXT = "help";
    private static final String MIKE_CRAIG_TEXT = "exactumundo";
    private static final String VIKTOR_ED_TEXT = "whhoppyydo";
    private static final String VIKTOR_CRAIG_TEXT = "whatssuppp";
    @Mock
    private BeanFactory bf;
    private Message mikeCraigMessage;
    private Message mikeEdMessage;
    private Message viktorCraigMessage;
    private Message viktorEdMessage;
    private Message viktorNullMessage;
    private MessageRepositoryImpl repo;

    /**
     * basename and code are required.
     */
    @Test(expected = NoResultException.class)
    public void testBasenameAndCodeRequired() {
        repo.findByBasenameAndCode(null, null);
    }

    /**
     * basenames and code required.
     */
    @Test
    public void testBasenamesAndCodeRequired() {
        try {
            repo.findByBasenamesAndCode(null, null);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should have failed");
    }

    /**
     * Code is required.
     */
    @Test
    public void testCodeRequired() {
        assertTrue("Should be empty", repo.findByCode(null).isEmpty());
    }

    /**
     * Can find by basename.
     */
    @Test
    public void testLoadByBasename() {
        Collection<Message> msgs = repo.findByBasename(ED_BASENAME);

        assertNotNull("messages are null", msgs);
        assertEquals("Incorrect number of messages", 2, msgs.size());
        assertTrue("Should contain mikeEdMessage", msgs.contains(mikeEdMessage));
        assertTrue("Should contain viktorEdMessage", msgs.contains(viktorEdMessage));
        assertFalse("Should not contain viktorCraigMessage", msgs.contains(viktorCraigMessage));
        assertFalse("Should not contain mikeCraigMessage", msgs.contains(mikeCraigMessage));
    }

    /**
     * can find by basename and code.
     */
    @Test
    public void testLoadByBasenameAndCode() {
        Message msg = repo.findByBasenameAndCode(ED_BASENAME, MIKE_CODE);

        assertNotNull("message cannot be null", msg);
        assertEquals("Should be mikeEdMessage", mikeEdMessage, msg);
    }

    /**
     * can load by basenames and code.
     */
    @Test
    public void testLoadByBasenamesAndCode() {
        Collection<String> basenames = new HashSet<String>();

        basenames.add(ED_BASENAME);
        basenames.add(CRAIG_BASENAME);

        Collection<Message> msgs = repo.findByBasenamesAndCode(basenames, VIKTOR_CODE);

        assertNotNull("messages are null", msgs);
        assertEquals("Incorrect number of messages", 2, msgs.size());
        assertTrue("Should contain viktorCraigMessage", msgs.contains(viktorCraigMessage));
        assertTrue("Should contain viktorEdMessage", msgs.contains(viktorEdMessage));
        assertFalse("Should not contain mikeEdMessage", msgs.contains(mikeEdMessage));
        assertFalse("Should not contain mikeCraigMessage", msgs.contains(mikeCraigMessage));
    }

    /**
     * Can find by code.
     */
    @Test
    public void testLoadByCode() {
        Collection<Message> msgs = repo.findByCode(MIKE_CODE);

        assertNotNull("messages are null", msgs);
        assertEquals("Incorrect number of messages", 2, msgs.size());
        assertTrue("Should contain mikeEdMessage", msgs.contains(mikeEdMessage));
        assertTrue("Should contain mikeCraigMessage", msgs.contains(mikeCraigMessage));
        assertFalse("Should not contain viktorCraigMessage", msgs.contains(viktorCraigMessage));
        assertFalse("Should not contain viktorEdMessage", msgs.contains(viktorEdMessage));
    }

    /**
     * can find by null basename and code.
     */
    @Test
    public void testLoadByNullBasenameAndCode() {
        Message msg = repo.findByBasenameAndCode(null, VIKTOR_CODE);

        assertNotNull("message cannot be null", msg);
        assertEquals("Should be viktorNullMessage", viktorNullMessage, msg);
    }

    /**
     * Basename is required.
     */
    @Test
    public void testNullBasename() {
        Collection<Message> mess = repo.findByBasename(null);

        assertEquals(1, mess.size());
        assertTrue("Should contain viktorNullMessage", mess.contains(viktorNullMessage));
    }

    /**
     * save message works.
     */
    @Test
    public void testSaveMessage() {
        Message mess = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());

        mess.setBaseName("basename");
        mess.setMessageCd("code");

        repo.save(mess);

        Message loadedMessage = getEntityManager().find(Message.class, mess.getId());

        assertNotNull("result is null", loadedMessage);
        assertEquals("Should be same message", mess, loadedMessage);
    }

    /**
     * save multiple messages.
     */
    @Test
    public void testSaveMessages()
        throws Exception {
        Message mess = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());

        mess.setBaseName("madeupbasename");
        mess.setMessageCd("code");

        Message mess2 = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());

        mess2.setBaseName("madeupbasename");
        mess2.setMessageCd("cofdsafdsade");

        Collection<Message> messages = new HashSet<Message>();

        messages.add(mess);
        messages.add(mess2);
        repo.save(messages);

        Collection<Message> loadedMessages = repo.findByBasename("madeupbasename");

        assertNotNull("messages are null", loadedMessages);
        assertEquals("Incorrect number of messages", 2, loadedMessages.size());
        assertTrue("Should contain mikeEdMessage", loadedMessages.contains(mess));
        assertTrue("Should contain mikeCraigMessage", loadedMessages.contains(mess2));
    }

    /**
     * save values as message.
     */
    @Test
    public void testSaveStringStringStringString()
        throws Exception {
        repo.save("basename", "en", "code", "msg");

        Message loadedMessage = repo.findByBasenameAndCode("basename", "code");

        assertNotNull("result is null", loadedMessage);
        assertEquals("basename", loadedMessage.getBaseName());
        assertEquals("code", loadedMessage.getMessageCd());
        assertEquals("msg", loadedMessage.getMessageTx("en"));
    }

    /**
     * Set up.
     *
     * @throws Exception.
     */
    protected void onSetUp()
        throws Exception {
        super.onSetUp();
        MockitoAnnotations.initMocks(this);
        repo = new MessageRepositoryImpl(getInstanceFactory());
        repo.setEntityManager(getEntityManager());
        repo.setDefaultLocaleString(Locale.ENGLISH.toString());
        when(bf.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID)).thenReturn(getInstanceFactory());
        when(bf.getBean(MessageRepository.MESSAGE_REPOSITORY_ID)).thenReturn(repo);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);

        mikeEdMessage = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());
        mikeEdMessage.setBaseName(ED_BASENAME);
        mikeEdMessage.setMessageCd(MIKE_CODE);
        mikeEdMessage.setMessageTx(MIKE_ED_TEXT, Locale.ENGLISH.toString());
        getEntityManager().persist(mikeEdMessage);

        mikeCraigMessage = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());
        mikeCraigMessage.setBaseName(CRAIG_BASENAME);
        mikeCraigMessage.setMessageCd(MIKE_CODE);
        mikeCraigMessage.setMessageTx(MIKE_CRAIG_TEXT, Locale.ENGLISH.toString());
        getEntityManager().persist(mikeCraigMessage);

        viktorCraigMessage = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());
        viktorCraigMessage.setBaseName(CRAIG_BASENAME);
        viktorCraigMessage.setMessageCd(VIKTOR_CODE);
        viktorCraigMessage.setMessageTx(VIKTOR_CRAIG_TEXT, Locale.ENGLISH.toString());
        getEntityManager().persist(viktorCraigMessage);

        viktorEdMessage = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());
        viktorEdMessage.setBaseName(ED_BASENAME);
        viktorEdMessage.setMessageCd(VIKTOR_CODE);
        viktorEdMessage.setMessageTx(VIKTOR_ED_TEXT, Locale.ENGLISH.toString());
        getEntityManager().persist(viktorEdMessage);

        viktorNullMessage = (Message) getInstanceFactory().getBean(Message.class.getSimpleName());
        viktorNullMessage.setMessageCd(VIKTOR_CODE);
        viktorNullMessage.setMessageTx(VIKTOR_ED_TEXT, Locale.ENGLISH.toString());
        getEntityManager().persist(viktorNullMessage);

        Message catAmongstThePigeonsMessage = (Message) getInstanceFactory()
                                                            .getBean(Message.class.getSimpleName());

        catAmongstThePigeonsMessage.setBaseName("cat");
        catAmongstThePigeonsMessage.setMessageCd("pigeons");
        catAmongstThePigeonsMessage.setMessageTx("peskypigeons", Locale.ENGLISH.toString());
        getEntityManager().persist(catAmongstThePigeonsMessage);
    }
}
