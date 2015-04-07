package org.cucina.engine.repository.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.repository.HistoryRecordRepository;
import org.springframework.stereotype.Repository;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Repository
public class HistoryRecordRepositoryImpl
    implements HistoryRecordRepository {
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
     * @param id JAVADOC.
     * @param applicationType JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<HistoryRecord> findByIdAndApplicationType(Serializable id, String applicationType) {
        //  "select hr.id as id, hr.status as status, hr.comments as comments, hr.modifiedBy as modifiedBy," +
        //" hr.modifiedDate as modifiedDate, hr.approvedBy as approvedBy, hrReason, hrAttachment.id as attachmentId " +
        //"from HistoryRecord hr left join hr.reason as hrReason left join hr.attachment as hrAttachment " +
        //"where hr.token.domainObjectId=?1 and hr.token.domainObjectType=?2 " +
        //"order by hr.modifiedDate desc"
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<HistoryRecord> cq = cb.createQuery(HistoryRecord.class);
        Root<HistoryRecord> root = cq.from(HistoryRecord.class);
        Predicate pi = cb.equal(root.get("token").get("domainObjectId"), id);
        Predicate pa = cb.equal(root.get("token").get("domainObjectType"), applicationType);

        // TODO join to listnode. or not
        cq.where(cb.and(pi, pa));

        return entityManager.createQuery(cq).getResultList();
    }
}
