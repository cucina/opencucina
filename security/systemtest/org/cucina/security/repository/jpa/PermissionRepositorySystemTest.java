package org.cucina.security.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.test.util.ReflectionTestUtils;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.testassist.JpaProvider;

import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PermissionRepositorySystemTest
    extends JpaProvider {
    private static final String MIKE_NAME = "mike";
    private static final String VIKTOR_NAME = "viktor";
    private static final String ED_PRIV = "ed";
    private static final String POND_PRIV = "pond";
    private static final String H_PERM = "h";
    private static final String A_ROLE = "a";
    @Mock
    private BeanFactory bf;
    private Permission hperm;
    private PermissionRepositoryImpl repo;
    private Privilege edpriv;
    private Privilege pondpriv;
    private Role arole;
    private User mike;
    private User viktor;

    // @Test
    /**
     *
     */
    public void findByUserAndPrivilege() {
        Collection<Permission> result = repo.findByUserAndPrivilege(MIKE_NAME, ED_PRIV);

        System.err.println(result);
    }

    //@Test
    /**
     *
     */
    public void testSubP() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);
        Path<Role> prole = root.get("role");

        Subquery<Role> subpriv = cq.subquery(Role.class);
        Root<Privilege> privRoot = subpriv.from(Privilege.class);
        Path<String> privn = privRoot.get("name");
        Predicate pren = cb.equal(privn, ED_PRIV);

        subpriv.select(privRoot.<Role>get("roles"));
        subpriv.where(pren);

        Predicate pi = cb.in(prole).value(subpriv);

        Path<Collection<User>> puser = root.get("users");

        Subquery<User> subuser = cq.subquery(User.class);
        Root<User> uRoot = subuser.from(User.class);
        Path<String> uname = uRoot.get("username");
        Predicate prun = cb.equal(uname, VIKTOR_NAME);

        subuser.select(uRoot);
        subuser.where(prun);

        Predicate ui = cb.isMember(subuser, puser);

        cq.select(root).where(cb.and(pi, ui));

        TypedQuery<Permission> tq = getEntityManager().createQuery(cq);

        System.err.println(tq.getResultList());
    }

    /**
     *
     */

    //@Test
    public void testSubPu() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Privilege> privRoot = cq.from(Privilege.class);
        Path<String> privn = privRoot.get("name");
        Predicate pren = cb.equal(privn, ED_PRIV);

        cq.select(privRoot.<Role>get("roles"));
        cq.where(pren);

        TypedQuery<Role> tq = getEntityManager().createQuery(cq);

        System.err.println(tq.getResultList());
    }

    /**
     *
     */
    @Test
    public void xtestQ() {
        Query q = getEntityManager()
                      .createQuery("select p from Permission p, User u where u.username = ?1 and u member of p.users " +
                " and p.role in (select r from Privilege pr, Role r where pr.name=?2 and r member of pr.roles)");

        q.setParameter(1, VIKTOR_NAME);
        q.setParameter(2, ED_PRIV);

        System.err.println(q.getResultList());
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
        repo = new PermissionRepositoryImpl();
        ReflectionTestUtils.setField(repo, "entityManager", getEntityManager());

        mike = getInstanceFactory().getBean(User.class.getSimpleName());
        mike.setUsername(MIKE_NAME);
        getEntityManager().persist(mike);

        viktor = getInstanceFactory().getBean(User.class.getSimpleName());
        viktor.setUsername(VIKTOR_NAME);
        getEntityManager().persist(viktor);

        edpriv = getInstanceFactory().getBean(Privilege.class.getSimpleName());
        edpriv.setName(ED_PRIV);
        getEntityManager().persist(edpriv);

        pondpriv = getInstanceFactory().getBean(Privilege.class.getSimpleName());
        pondpriv.setName(POND_PRIV);
        getEntityManager().persist(pondpriv);

        arole = getInstanceFactory().getBean(Role.class.getSimpleName());
        arole.setName(A_ROLE);

        Collection<Privilege> privileges = new ArrayList<Privilege>();

        privileges.add(edpriv);
        privileges.add(pondpriv);
        arole.setPrivileges(privileges);
        getEntityManager().persist(arole);

        System.err.println(arole);

        Collection<Role> roles = new ArrayList<Role>();

        roles.add(arole);
        edpriv.setRoles(roles);
        getEntityManager().merge(edpriv);
        pondpriv.setRoles(roles);
        getEntityManager().merge(pondpriv);

        hperm = getInstanceFactory().getBean(Permission.class.getSimpleName());

        hperm.setRole(arole);
        hperm.setName(H_PERM);

        Collection<User> users = new ArrayList<User>();

        users.add(mike);
        users.add(viktor);
        hperm.setUsers(users);

        getEntityManager().persist(hperm);
    }
}
