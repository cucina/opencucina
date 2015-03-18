package org.cucina.i18n.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.domain.Pageable;

import org.cucina.core.InstanceFactory;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.Message;
import org.cucina.i18n.model.MutableI18nMessage;
import org.cucina.i18n.repository.MessageRepository;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessageRepositoryImplTest {
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<Message> cq;
    @Mock
    private EntityManager em;
    @Mock
    private InstanceFactory instanceFactory;
    private MessageRepositoryImpl repo;
    @Mock
    private TypedQuery<Message> tq;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        repo = new MessageRepositoryImpl(instanceFactory);
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Message.class)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
        when(beanFactory.getBean(MessageRepository.MESSAGE_REPOSITORY_ID)).thenReturn(repo);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFindAll() {
        repo.findAll();
        verify(cq).from(Message.class);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFindAllPage() {
        Pageable pageable = mock(Pageable.class);

        when(pageable.getPageNumber()).thenReturn(0);
        when(pageable.getPageSize()).thenReturn(10);

        repo.findAll(pageable);
        verify(cq).from(Message.class);
        verify(tq).setFirstResult(0);
        verify(tq).setMaxResults(10);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByBasename() {
        Root<Message> root = mock(Root.class);

        when(cq.from(Message.class)).thenReturn(root);

        Path<Object> pathb = mock(Path.class);

        when(root.get("baseName")).thenReturn(pathb);

        Predicate preb = mock(Predicate.class);

        when(cb.equal(pathb, "basename")).thenReturn(preb);
        when(cq.where(preb)).thenReturn(cq);

        repo.findByBasename("basename");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByBasenameAndCode() {
        Root<Message> root = mock(Root.class);

        when(cq.from(Message.class)).thenReturn(root);

        Path<Object> pathb = mock(Path.class);

        when(root.get("baseName")).thenReturn(pathb);

        Predicate preb = mock(Predicate.class);

        when(cb.equal(pathb, "basename")).thenReturn(preb);

        Path<Object> pathc = mock(Path.class);

        when(root.get("messageCd")).thenReturn(pathc);

        Predicate prec = mock(Predicate.class);

        when(cb.equal(pathc, "code")).thenReturn(prec);

        Predicate and = mock(Predicate.class);

        when(cb.and(preb, prec)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        repo.findByBasenameAndCode("basename", "code");
        verify(tq).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByBasenamesAndCode() {
        Collection<String> basenames = new ArrayList<String>();

        basenames.add("a");
        basenames.add("b");

        Root<Message> root = mock(Root.class);

        when(cq.from(Message.class)).thenReturn(root);

        Path<Object> pathb = mock(Path.class);

        when(root.get("baseName")).thenReturn(pathb);

        Predicate preb = mock(Predicate.class);

        when(pathb.in(basenames)).thenReturn(preb);

        Path<Object> pathc = mock(Path.class);

        when(root.get("messageCd")).thenReturn(pathc);

        Predicate prec = mock(Predicate.class);

        when(cb.equal(pathc, "code")).thenReturn(prec);

        Predicate and = mock(Predicate.class);

        when(cb.and(preb, prec)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        repo.findByBasenamesAndCode(basenames, "code");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByCode() {
        Root<Message> root = mock(Root.class);

        when(cq.from(Message.class)).thenReturn(root);

        Path<Object> pathb = mock(Path.class);

        when(root.get("messageCd")).thenReturn(pathb);

        Predicate preb = mock(Predicate.class);

        when(cb.equal(pathb, "code")).thenReturn(preb);
        when(cq.where(preb)).thenReturn(cq);

        repo.findByCode("code");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFindById() {
        repo.findById(11L);
        verify(em).find(Message.class, 11L);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetDefaultLocale() {
        repo.getDefaultLocale();
        repo.setDefaultLocaleString(Locale.KOREAN.toString());
        repo.getDefaultLocale();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveCollectionOfMessage() {
        Collection<Message> messages = new ArrayList<Message>();
        Message mess = new Message();

        messages.add(mess);

        Message old = new Message();

        old.setId(2L);
        messages.add(old);
        repo.save(messages);
        verify(em).persist(mess);
        verify(em).merge(old);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSaveMessage() {
        Message message = new Message();

        repo.save(message);
        verify(em).persist(message);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveStringStringStringString() {
        when(beanFactory.getBean(SingletonBeanFactory.INSTANCE_FACTORY_ID))
            .thenReturn(instanceFactory);

        MutableI18nMessage imess = new MutableI18nMessage();

        when(instanceFactory.getBean(MutableI18nMessage.TYPE)).thenReturn(imess);

        Root<Message> root = mock(Root.class);

        when(cq.from(Message.class)).thenReturn(root);

        Path<Object> pathb = mock(Path.class);

        when(root.get("baseName")).thenReturn(pathb);

        Predicate preb = mock(Predicate.class);

        when(cb.equal(pathb, "basename")).thenReturn(preb);

        Path<Object> pathc = mock(Path.class);

        when(root.get("messageCd")).thenReturn(pathc);

        Predicate prec = mock(Predicate.class);

        when(cb.equal(pathc, "code")).thenReturn(prec);

        Predicate and = mock(Predicate.class);

        when(cb.and(preb, prec)).thenReturn(and);
        when(cq.where(and)).thenReturn(cq);

        Message found = new Message();

        when(tq.getSingleResult()).thenReturn(found);
        repo.save("basename", "locale", "code", "msg");
        assertEquals(imess, found.getInternationalisedMessages().iterator().next());
        verify(em).persist(found);
    }
}
