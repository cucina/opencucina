package org.cucina.search.query.projection;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.utils.NameUtils;
import org.springframework.util.Assert;


/**
 * Inline query projection.
 */
public class HasAttachmentsProjection
		extends AbstractProjection {
	private static final String QUERY = "(select (case count(history.id) when 0 then false else true end) from HistoryRecord history " +
			"where history.attachment is not null " + "and history.token.domainObjectType = '?1' " +
			"and history.token.domainObjectId = ?2 ) " + "as ";
	private static final String NAME = "hasAttachments";
	private String rootType;

	/**
	 * Creates a new HasAttachmentsProjection object.
	 *
	 * @param rootAlias JAVADOC.
	 * @param rootType  JAVADOC.
	 */
	public HasAttachmentsProjection(String rootAlias, String rootType) {
		super(NAME, NAME, rootAlias);
		Assert.hasText(rootType, "rootType cannot be null");
		this.rootType = rootType;
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private HasAttachmentsProjection() {
		super();
	}

	/**
	 * inner select should not be part of group by
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getProjection() {
		String hql = QUERY.replace("?1", getRootType());

		hql = hql.replace("?2", NameUtils.concat(getRootAlias(), PersistableEntity.ID_PROPERTY));

		return hql + getAlias();
	}

	/**
	 * Get rootType
	 *
	 * @return rootType String.
	 */
	public String getRootType() {
		return rootType;
	}

	/**
	 * Set rootType
	 *
	 * @param rootType String.
	 */
	public void setRootType(String rootType) {
		this.rootType = rootType;
	}
}
