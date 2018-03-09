package org.cucina.engine.search;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.cucina.engine.definition.Token;
import org.cucina.engine.service.TransitionsAccessor;
import org.cucina.search.AbstractResultSetModifier;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NoObjectCheckPermittedTransitionsAppender
		extends AbstractResultSetModifier {
	/**
	 * This is a field JAVADOC
	 */
	public static final String PROPERTY_NAME = "transitionIds";
	private TransitionsAccessor transitionsAccessor;

	/**
	 * Creates a new NoObjectCheckPermittedTransitionsAppender object.
	 *
	 * @param workflowSupportService JAVADOC.
	 */
	public NoObjectCheckPermittedTransitionsAppender(TransitionsAccessor transitionsAccessor) {
		Assert.notNull(transitionsAccessor, "transitionsAccessor is required");
		this.transitionsAccessor = transitionsAccessor;
	}

	/**
	 * uses only workflow id and placeid and therefore results can be cached via map (ptCache)
	 *
	 * @param applicationType JAVADOC.
	 * @param results         JAVADOC.
	 */
	@Override
	public void doModify(String applicationType, Collection<Map<String, Object>> results) {
		Map<String, Collection<String>> ptCache = new HashMap<String, Collection<String>>();

		for (Map<String, Object> row : results) {
			String workflowDefinitionId = (String) row.get(Token.WFD_ID_PROPERTY_NAME);
			String placeId = (String) row.get(Token.PLACE_ID_PROPERTY_NAME);

			if (StringUtils.isBlank(workflowDefinitionId) || StringUtils.isBlank(placeId)) {
				continue;
			}

			String cacheKey = NameUtils.concat(workflowDefinitionId, placeId);
			Collection<String> pTransitions = ptCache.get(cacheKey);

			if (pTransitions == null) {
				pTransitions = transitionsAccessor.listPermittedTransitionsNoObjectCheck(workflowDefinitionId,
						placeId);
				ptCache.put(cacheKey, pTransitions);
			}

			row.put(PROPERTY_NAME, pTransitions);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<String> listProperties(String applicationType) {
		return Arrays.asList(PROPERTY_NAME);
	}
}
