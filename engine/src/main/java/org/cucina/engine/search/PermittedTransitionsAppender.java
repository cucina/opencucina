package org.cucina.engine.search;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.definition.Token;
import org.cucina.engine.service.TransitionsAccessor;
import org.cucina.search.AbstractResultSetModifier;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * Implementation of <code>ResultSetModifier</code> which appends the user's permitted transitions
 * to the results.
 *
 * @author hkelsey
 */
public class PermittedTransitionsAppender
		extends AbstractResultSetModifier {
	/**
	 * This is a field JAVADOC
	 */
	public static final String PROPERTY_NAME = "transitionIds";
	private TransitionsAccessor transitionsAccessor;

	/**
	 * Creates a new PermittedTransitionsAppender object.
	 *
	 * @param workflowSupportService JAVADOC.
	 */
	public PermittedTransitionsAppender(TransitionsAccessor transitionsAccessor) {
		Assert.notNull(transitionsAccessor, "transitionsAccessor is required");
		this.transitionsAccessor = transitionsAccessor;
	}

	/**
	 * Each row is expected to contain the placeId and workflowDefinitionId, as long with the row itself as these
	 * are required to get the permitted transitions.
	 *
	 * @see org.cucina.meringue.search.AbstractResultSetModifier#doModify(java.lang.String, java.util.Collection)
	 */
	@Override
	public void doModify(String applicationType, Collection<Map<String, Object>> results) {
		for (Map<String, Object> row : results) {
			String workflowDefinitionId = (String) row.get(Token.WFD_ID_PROPERTY_NAME);
			String placeId = (String) row.get(Token.PLACE_ID_PROPERTY_NAME);


			if (StringUtils.isBlank(workflowDefinitionId) || StringUtils.isBlank(placeId)) {
				continue;
			}

			Collection<String> pTransitions = transitionsAccessor.listPermittedTransitions(workflowDefinitionId,
					placeId, applicationType, row);

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
