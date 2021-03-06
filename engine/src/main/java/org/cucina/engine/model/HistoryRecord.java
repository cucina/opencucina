package org.cucina.engine.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.Attachment;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.model.projection.ProjectionColumns;
import org.cucina.i18n.api.TranslatedColumn;

import javax.persistence.*;
import java.util.Date;


/**
 * Stores history for a subject to a workflow
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Entity
@Cacheable
public class HistoryRecord
		extends PersistableEntity {
	/**
	 * history
	 */
	public static final String ALIAS = "history";
	/**
	 * comments
	 */
	public static final String COMMENTS_PROPERTY = "comments";
	private static final long serialVersionUID = -4534381149119584527L;
	private Attachment attachment;
	private Date modifiedDate;
	private ProcessToken token;
	private String approvedBy;
	private String assignedTo;
	private String comments;
	private String modifiedBy;
	@TranslatedColumn
	private String reason;
	private String status;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Basic
	@Column(name = "approved_by")
	@ProjectionColumn
	public String getApprovedBy() {
		return approvedBy;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param approvedBy JAVADOC.
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return
	 */
	@Basic
	@Column(name = "assigned_to")
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */

	//@Transient
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "attachment")
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param attachment JAVADOC.
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@ProjectionColumn
	public String getComments() {
		return comments;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param comments JAVADOC.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Basic
	@Column(name = "modified_by")
	@ProjectionColumn
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param modifiedBy JAVADOC.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Basic
	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@ProjectionColumn
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param modifiedDate JAVADOC.
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param reason JAVADOC.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@ProjectionColumn
	public String getStatus() {
		return status;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param status JAVADOC.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@ManyToOne
	@JoinColumn(name = "token", nullable = false)
	@ProjectionColumns({@ProjectionColumn(columnName = "domainObjectId", property = "domainObjectId")
			, @ProjectionColumn(columnName = "domainObjectType", property = "domainObjectType")
	})
	public ProcessToken getToken() {
		return token;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param token JAVADOC.
	 */
	public void setToken(ProcessToken token) {
		this.token = token;
	}

	/**
	 * Default toString implementation
	 *
	 * @return This object as String.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(PersistableEntity.ID_PROPERTY, getId())
				.append("comments", getComments()).toString();
	}
}
