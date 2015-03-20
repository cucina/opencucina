package org.cucina.engine.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;

import org.cucina.engine.definition.Token;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine $
 */
@Entity
@Cacheable
@Table(name = "Token")
public class ProcessToken
    extends PersistableEntity
    implements Token, Versioned {
    private static final long serialVersionUID = -5610021631597142222L;
    private List<HistoryRecord> histories;
    private Long domainObjectId;
    private Persistable<Long> domainObject;
    private Set<ProcessToken> children;
    private String domainObjectType;
    private String placeId;
    private String workflowDefinitionId;
    private ProcessToken parent;
    private int version;

    /**
     * JAVADOC Method Level Comments
     *
     * @param children
     *            JAVADOC.
     */
    public void setChildren(Set<ProcessToken> children) {
        this.children = children;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<ProcessToken> getChildren() {
        return children;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param domainObject
     *            JAVADOC.
     */
    public void setDomainObject(Persistable<Long> domainObject) {
        this.domainObject = domainObject;
    }

    /**
     * The setting of domainObject is for convenience only, and should be
     * ignored by underlying ORM.
     *
     * @return domainObject PersistableObject.
     */
    @Transient
    public Persistable<Long> getDomainObject() {
        return domainObject;
    }

    /**
     * JAVADOC.
     *
     * @param expiryDate
     *            JAVADOC.
     */
    public void setDomainObjectId(Long domainObjectId) {
        this.domainObjectId = domainObjectId;
    }

    /**
     * JAVADOC.
     *
     * @param expiryDate
     *            JAVADOC.
     */
    @Basic
    @Column(name = "domain_object_id", nullable = false)
    public Long getDomainObjectId() {
        return this.domainObjectId;
    }

    /**
     * JAVADOC.
     *
     * @param domainObjectType
     *            JAVADOC.
     */
    public void setDomainObjectType(String domainObjectType) {
        this.domainObjectType = domainObjectType;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    @Basic
    @Column(name = "domain_object_type", nullable = false)
    public String getDomainObjectType() {
        return domainObjectType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param histories
     *            JAVADOC.
     */
    public void setHistories(List<HistoryRecord> histories) {
        this.histories = histories;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "token")
    @OrderBy("")
    public List<HistoryRecord> getHistories() {
        return histories;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Transient
    public org.cucina.engine.model.HistoryRecord getLatestHistory() {
        return getHistories().get(getHistories().size() - 1);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param parent
     *            JAVADOC.
     */
    public void setParent(ProcessToken parent) {
        this.parent = parent;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne
    public ProcessToken getParent() {
        return parent;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param token
     *            JAVADOC.
     */
    @Override
    public void setParentToken(Token token) {
        Assert.isInstanceOf(ProcessToken.class, token, "token must be instance of WorkflowToken");
        this.setParent((ProcessToken) token);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Transient
    public Token getParentToken() {
        return getParent();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param placeId
     *            JAVADOC.
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Set version
     *
     * @param version
     *            int.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Get version number
     *
     * @return version.
     */
    @Version
    public int getVersion() {
        return version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowDefinitionId
     *            JAVADOC.
     */
    public void setProcessDefinitionId(String workflowDefinitionId) {
        this.workflowDefinitionId = workflowDefinitionId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Basic
    @Column(name = "workflow_definition_id")
    public String getProcessDefinitionId() {
        return workflowDefinitionId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    public void addChild(Token token) {
        Assert.isTrue(token instanceof ProcessToken, "cannot handle not WorkflowToken instances");

        ProcessToken wflToken = (ProcessToken) token;

        if (getChildren() == null) {
            setChildren(new HashSet<ProcessToken>());
        }

        getChildren().add(wflToken);
        wflToken.setParent(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param historyRecord
     *            JAVADOC.
     */
    public void addHistoryRecord(org.cucina.engine.model.HistoryRecord historyRecord) {
        if (getHistories() == null) {
            setHistories(new ArrayList<org.cucina.engine.model.HistoryRecord>());
        }

        getHistories().add(historyRecord);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean hasChildren() {
        return CollectionUtils.isNotEmpty(getChildren());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    public void removeChild(Token arg0) {
        if (getChildren() != null) {
            getChildren().remove(arg0);
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String toString() {
        return new ToStringBuilder(this).append(PersistableEntity.ID_PROPERTY, getId())
                                        .append("placeId", getPlaceId())
                                        .append("domainObject", getDomainObject())
                                        .append("workflowDefinitionId", getProcessDefinitionId())
                                        .toString();
    }
}
