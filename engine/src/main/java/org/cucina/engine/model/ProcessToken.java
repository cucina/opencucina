package org.cucina.engine.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.engine.definition.Token;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@Entity
@Cacheable
public class ProcessToken
		extends PersistableEntity
		implements Token, Versioned {
	private static final long serialVersionUID = -5610021631597142222L;
	private List<HistoryRecord> histories;
	private Serializable domainObjectId;
	private Persistable<? extends Serializable> domainObject;
	private Set<ProcessToken> children;
	private String domainObjectType;
	private String placeId;
	private String processDefinitionId;
	private ProcessToken parent;
	private int version;

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
	 * @param children JAVADOC.
	 */
	public void setChildren(Set<ProcessToken> children) {
		this.children = children;
	}

	/**
	 * The setting of domainObject is for convenience only, and should be
	 * ignored by underlying ORM.
	 *
	 * @return domainObject PersistableObject.
	 */
	@Transient
	public Persistable<? extends Serializable> getDomainObject() {
		return domainObject;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param domainObject JAVADOC.
	 */
	public void setDomainObject(Persistable<? extends Serializable> domainObject) {
		this.domainObject = domainObject;
	}

	/**
	 * JAVADOC.
	 *
	 * @param expiryDate JAVADOC.
	 */
	@Basic
	@Column(nullable = false)
	public Serializable getDomainObjectId() {
		return this.domainObjectId;
	}

	/**
	 * JAVADOC.
	 *
	 * @param expiryDate JAVADOC.
	 */
	public void setDomainObjectId(Serializable domainObjectId) {
		this.domainObjectId = domainObjectId;
	}

	/**
	 * JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	@Basic
	@Column(nullable = false)
	public String getDomainObjectType() {
		return domainObjectType;
	}

	/**
	 * JAVADOC.
	 *
	 * @param domainObjectType JAVADOC.
	 */
	public void setDomainObjectType(String domainObjectType) {
		this.domainObjectType = domainObjectType;
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
	 * @param histories JAVADOC.
	 */
	public void setHistories(List<HistoryRecord> histories) {
		this.histories = histories;
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
	 * @return JAVADOC.
	 */
	@ManyToOne
	public ProcessToken getParent() {
		return parent;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param parent JAVADOC.
	 */
	public void setParent(ProcessToken parent) {
		this.parent = parent;
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
	 * @param token JAVADOC.
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
	public String getPlaceId() {
		return placeId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param placeId JAVADOC.
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
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
	 * Set version
	 *
	 * @param version int.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Basic
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param processDefinitionId JAVADOC.
	 */
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
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
	 * @param historyRecord JAVADOC.
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
	 * @param arg0 JAVADOC.
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
