package org.cucina.security.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;

import org.cucina.testassist.utils.LoggingEnabler;
import static org.junit.Assert.assertTrue;

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
public class PermissionRepositoryImplTest {
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private CriteriaQuery<Permission> cq;
    @Mock
    private EntityManager em;
    private PermissionRepositoryImpl repo;
    @Mock
    private TypedQuery<Permission> tq;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        LoggingEnabler.enableLog(PermissionRepositoryImpl.class);
        repo = new PermissionRepositoryImpl();
        repo.setEntityManager(em);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Permission.class)).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExists() {
        Role role = new Role();
        Permission permission = new Permission();

        permission.setRole(role);

        Collection<Dimension> dims = new ArrayList<Dimension>();
        Dimension dim = new Dimension();

        dim.setDomainObjectId(123L);
        dim.setDomainObjectType("foo");
        dim.setPropertyName("propertyName");
        dims.add(dim);
        permission.setDimensions(dims);

        Root<Permission> root = mock(Root.class);

        when(cq.from(Permission.class)).thenReturn(root);

        Collection<Predicate> preds = new ArrayList<Predicate>();

        Path<Object> pr = mock(Path.class);

        when(root.get("role")).thenReturn(pr);

        Predicate rpre = mock(Predicate.class);

        when(cb.equal(pr, role)).thenReturn(rpre);

        preds.add(rpre);

        //        if (permission.getId() != null) {
        //            preds.add(cb.not(cb.equal(root.get("id"), permission.getId())));
        //        }
        Root<Dimension> droot = mock(Root.class);

        when(cq.from(Dimension.class)).thenReturn(droot);

        Path<Object> pri = mock(Path.class);

        when(droot.get("domainObjectId")).thenReturn(pri);

        Predicate ipre = mock(Predicate.class);

        when(cb.equal(pri, 123L)).thenReturn(ipre);
        preds.add(ipre);

        Path<Object> prt = mock(Path.class);

        when(droot.get("domainObjectType")).thenReturn(prt);

        Predicate trpre = mock(Predicate.class);

        when(cb.equal(prt, "foo")).thenReturn(trpre);
        preds.add(trpre);

        Path<Object> prp = mock(Path.class);

        when(droot.get("propertyName")).thenReturn(prp);

        Predicate ppre = mock(Predicate.class);

        when(cb.equal(prp, "propertyName")).thenReturn(ppre);
        preds.add(ppre);

        Subquery<Long> subquery = mock(Subquery.class);

        when(cq.subquery(Long.class)).thenReturn(subquery);

        Root<Dimension> croot = mock(Root.class);

        when(subquery.from(Dimension.class)).thenReturn(croot);

        Path<Object> pep = mock(Path.class);

        when(croot.get("permission")).thenReturn(pep);

        Path<Object> pip = mock(Path.class);

        when(pep.get("id")).thenReturn(pip);

        Path<Object> piip = mock(Path.class);

        when(root.get("id")).thenReturn(piip);

        Predicate cpre = mock(Predicate.class);

        when(cb.equal(pip, piip)).thenReturn(ppre);

        when(subquery.where(cpre)).thenReturn(subquery);

        Expression<Long> count = mock(Expression.class);

        when(cb.count(croot)).thenReturn(count);
        when(subquery.select(count)).thenReturn(subquery);

        Predicate pedc = mock(Predicate.class);

        when(cb.equal(subquery, dims.size())).thenReturn(pedc);

        preds.add(pedc);

        Predicate superand = mock(Predicate.class);

        when(cb.and(preds.toArray(new Predicate[dims.size()]))).thenReturn(superand);
        when(cq.where(superand)).thenReturn(cq);

        List<Permission> results = new ArrayList<Permission>();

        results.add(new Permission());
        when(tq.getResultList()).thenReturn(results);
        assertTrue(repo.exists(permission));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testFindAll() {
        repo.findAll();
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByRole() {
        Role role = new Role();

        Root<Permission> root = mock(Root.class);

        when(cq.from(Permission.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("role")).thenReturn(pr);

        Predicate wi = mock(Predicate.class);

        when(cb.equal(pr, role)).thenReturn(wi);
        when(cq.where(wi)).thenReturn(cq);
        repo.findByRole(role);
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByRoleName() {
        Root<Permission> root = mock(Root.class);

        when(cq.from(Permission.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("role")).thenReturn(pr);

        Path<Object> pn = mock(Path.class);

        when(pr.get("name")).thenReturn(pn);

        Predicate wi = mock(Predicate.class);

        when(cb.equal(pn, "roleName")).thenReturn(wi);
        when(cq.where(wi)).thenReturn(cq);
        repo.findByRoleName("roleName");
        verify(tq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByUserAndPrivilege() {
        User user = new User();
        Privilege privilege = new Privilege();
        Root<Permission> root = mock(Root.class);

        when(cq.from(Permission.class)).thenReturn(root);

        Path<Object> pr = mock(Path.class);

        when(root.get("role")).thenReturn(pr);

        Path<Collection<Privilege>> pp = pr.get("privileges");

        Predicate pi = mock(Predicate.class);

        when(cb.isMember(privilege, pp)).thenReturn(pi);

        Path<Collection<User>> pu = mock(Path.class);

        when(root.<Collection<User>>get("users")).thenReturn(pu);

        Predicate ui = mock(Predicate.class);

        when(cb.isMember(user, pu)).thenReturn(ui);

        Predicate preand = mock(Predicate.class);

        when(cb.and(pi, ui)).thenReturn(preand);
        when(cq.where(preand)).thenReturn(cq);
        repo.findByUserAndPrivilege(user, privilege);
    }
}
