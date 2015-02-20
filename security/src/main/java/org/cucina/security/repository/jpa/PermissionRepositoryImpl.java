package org.cucina.security.repository.jpa;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.security.model.Dimension;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Privilege;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermissionRepositoryImpl
    implements PermissionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param entityManager JAVADOC.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param permission JAVADOC.
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
     * @param id JAVADOC.
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
     * @param role JAVADOC.
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
     * @param roleName JAVADOC.
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
     * @param user JAVADOC.
     * @param privilege JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Permission> findByUserAndPrivilege(User user, Privilege privilege) {
        // select perm from Permission perm where ?1 member of perm.role.privileges and ?2 member of perm.users
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);

        Predicate pi = cb.isMember(privilege,
                root.get("role").<Collection<Privilege>>get("privileges"));
        Predicate ui = cb.isMember(user, root.<Collection<User>>get("users"));

        return entityManager.createQuery(cq.where(cb.and(pi, ui))).getResultList();
    }
}
