package org.cucina.engine.server.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.projection.ExternalProjectionColumn;
import org.cucina.core.model.projection.ExternalProjectionColumns;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.search.model.projection.Search;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@ExternalProjectionColumns(fieldName = HistoryRecord.ALIAS, clazz = HistoryRecord.class, value =  {
    @ExternalProjectionColumn(columnName = "lastUpdatedDate", property = "modifiedDate")
    , @ExternalProjectionColumn(columnName = "tokenStatus", property = "status")
    , @ExternalProjectionColumn(columnName = "approvedBy", property = "approvedBy")
    , @ExternalProjectionColumn(columnName = "comments", property = "comments")
    , @ExternalProjectionColumn(columnName = "transitionedBy", property = "modifiedBy")
    , @ExternalProjectionColumn(columnName = "assignedTo", property = "assignedTo")
    , @ExternalProjectionColumn(columnName = "domainObjectType", property = "token.domainObjectType", groups = Search.class)
    , @ExternalProjectionColumn(columnName = "version", property = "token.version", groups = Search.class)
    , @ExternalProjectionColumn(columnName = "workflowDefinitionId", property = "token.workflowDefinitionId", groups = Search.class)
    , @ExternalProjectionColumn(columnName = "placeId", property = "token.placeId", groups = Search.class)
}
)
@Cacheable
@Entity
public class EntityDescriptor
    extends PersistableEntity {
    private static final long serialVersionUID = -2567806169877757973L;

    /** This is a field JAVADOC */
    public static final String APPLICATION_TYPE_PROP = "applicationType";
    private Long id;
    private Long localId;
    private String applicationName;
    private String applicationType;

    /**
     * Creates a new EntityDescriptor object.
     */
    public EntityDescriptor() {
    }

    /**
     * Creates a new EntityDescriptor object.
     *
     * @param applicationType
     *            JAVADOC.
     * @param refid
     *            JAVADOC.
     */
    public EntityDescriptor(String applicationType, Long id, String applicationName) {
        this.applicationType = applicationType;
        this.id = id;
        this.applicationName = applicationName;
    }

    /**
     * Creates a new EntityDescriptor object.
     *
     * @param entity
     *            JAVADOC.
     */
    public EntityDescriptor(PersistableEntity entity) {
        this.applicationType = entity.getApplicationType();
        this.id = entity.getId();
    }

    /**
     * Set applicationName
     *
     * @param applicationName String.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Get applicationName
     *
     * @return applicationName String.
     */
    @Basic(optional = false)
    @ProjectionColumn
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType
     *            JAVADOC.
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ProjectionColumn
    @Basic(optional = false)
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param refid
     *            JAVADOC.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ProjectionColumn
    public Long getId() {
        return id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param localId
     *            JAVADOC.
     */
    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdSeq")
    @ProjectionColumn
    public Long getLocalId() {
        return localId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
