package org.cucina.security.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.repository.PermissionRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Repository
public class PermissionRepositoryImpl
    implements PermissionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param permission
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean exists(Permission permission) {
        Role role = permission.getRole();

        Assert.notNull(role, "Permission has no role");

        Collection<Dimension> dims = permission.getDimensions();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);
        Collection<Predicate> preds = new ArrayList<Predicate>();

        preds.add(cb.equal(root.get("role"), role));

        if (permission.getId() != null) {
            preds.add(cb.not(cb.equal(root.get("id"), permission.getId())));
        }

        for (Dimension dimension : dims) {
            Root<Dimension> droot = cq.from(Dimension.class);

            preds.add(cb.equal(droot.get("domainObjectId"), dimension.getDomainObjectId()));
            preds.add(cb.equal(droot.get("domainObjectType"), dimension.getDomainObjectType()));
            preds.add(cb.equal(droot.get("propertyName"), dimension.getPropertyName()));
        }

        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<Dimension> croot = subquery.from(Dimension.class);
        Predicate cpre = cb.equal(croot.get("permission").get("id"), root.get("id"));

        subquery.where(cpre);
        subquery.select(cb.count(croot));

        preds.add(cb.equal(subquery, dims.size()));

        Predicate superand = cb.and(preds.toArray(new Predicate[dims.size()]));

        return CollectionUtils.isNotEmpty(entityManager.createQuery(cq.where(superand))
                                                       .getResultList());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Permission find(Long id) {
        return entityManager.find(Permission.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Permission> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param role
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Permission> findByRole(Role role) {
        // select perm from Permission perm where perm.role = ?1
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);
        Predicate wi = cb.equal(root.get("role"), role);

        return entityManager.createQuery(cq.where(wi)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param roleName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Permission> findByRoleName(String roleName) {
        // select perm from Permission perm where perm.role.name = ?1
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);
        Predicate wi = cb.equal(root.get("role").get("name"), roleName);

        return entityManager.createQuery(cq.where(wi)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param user
     *            JAVADOC.
     * @param privilege
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Permission> findByUserAndPrivilege(String userName, String privilegeName) {
        // select perm from Permission perm where ?1 member of
        // perm.role.privileges and ?2 member of perm.users
        /*
         * CriteriaBuilder cb = entityManager.getCriteriaBuilder();
         * CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
         * Root<Permission> root = cq.from(Permission.class); Path<Role> prole =
         * root.get("role");
         *
         * Subquery<Role> subpriv = cq.subquery(Role.class); Root<Privilege>
         * privRoot = subpriv.from(Privilege.class); Path<String> privn =
         * privRoot.get("name"); Predicate pren = cb.equal(privn,
         * privilegeName); subpriv.select(privRoot.<Role>get("roles"));
         * subpriv.where(pren);
         *
         * Predicate pi = cb.in(prole).value(subpriv);
         *
         * Path<Collection<User>> puser = root.get("users");
         *
         * Subquery<User> subuser = cq.subquery(User.class); Root<User> uRoot =
         * subuser.from(User.class); Path<String> uname = uRoot.get("username");
         * Predicate prun = cb.equal(uname, userName); subuser.select(uRoot);
         * subuser.where(prun);
         *
         * Predicate ui = cb.isMember(subuser, puser);
         *
         * cq.select(root).where(cb.and(pi, ui)); TypedQuery<Permission> tq =
         * entityManager.createQuery(cq);
         */
        TypedQuery<Permission> tq = entityManager.createQuery(
                "select p from Permission p, User u where u.username = ?1 " +
                " and u member of p.users " +
                " and p.role in (select r from Privilege pr, Role r where pr.name=?2 and r member of pr.roles)",
                Permission.class);

        tq.setParameter(1, privilegeName);
        tq.setParameter(2, userName);

        return tq.getResultList();
    }
}
