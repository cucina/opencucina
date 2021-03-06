package org.cucina.engine.server.definition;

import java.io.Serializable;
import java.util.Date;


/**
 * @author vlevine
 */
public class HistoryRecordDto
		implements Serializable {
	private static final long serialVersionUID = -2580964481554531022L;
	private Date modifiedDate;
	private String approvedBy;
	private String assignedTo;
	private String comments;
	private String modifiedBy;
	private String reason;
	private String status;

	/**
	 * @return .
	 */
	public String getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy .
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return .
	 */
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo .
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * @return .
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments .
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return .
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy .
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return .
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate .
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return .
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason .
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return .
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status .
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
