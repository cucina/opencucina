package org.cucina.search.query.projection;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.Join;
import org.cucina.search.query.LeftJoin;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;


/**
 * Creates joins across internationalised message tables in order to select according to a locale.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TranslatedMessageWithJoinProjection
		extends AbstractProjection {
	/**
	 * Creates a new TranslatedMessageWithJoinProjection object.
	 *
	 * @param name      JAVADOC.
	 * @param alias     JAVADOC.
	 * @param rootAlias JAVADOC.
	 */
	public TranslatedMessageWithJoinProjection(String name, String alias, String rootAlias) {
		super(name, alias, rootAlias);
	}

	@SuppressWarnings("unused")
	private TranslatedMessageWithJoinProjection() {
		super();
	}

	/**
	 * Gets standard joins plus those across internationalised messages
	 *
	 * @return List<Join>.
	 */
	@Override
	public Map<String, List<Join>> getJoins() {
		Map<String, List<Join>> joins = super.getJoins();
		String[] tokens = StringUtils.split(getName(), NameUtils.DEFAULT_SEPARATOR);

		joins.get(getRootAlias()).add(new LeftJoin(tokens[tokens.length - 1], true));
		joins.get(getRootAlias()).add(new LeftJoin("internationalisedMessages", true));

		return joins;
	}

	/**
	 * Gets the search property name plus alias
	 *
	 * @return projection.
	 */
	@Override
	public String getProjection() {
		return getSearchPropertyName() + " as " + getAlias();
	}

	/**
	 * Gets the search property name, in this instance parentAlias.messageTx
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getSearchPropertyName() {
		Assert.notNull(getParentAliases(), "parentAlias must be set before calling");

		return NameUtils.concat(getParentAlias(getName(), getRootAlias()), "messageTx");
	}
}
