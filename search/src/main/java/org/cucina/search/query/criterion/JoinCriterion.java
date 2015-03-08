
package org.cucina.search.query.criterion;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cucina.search.query.Join;
import org.springframework.util.Assert;


/**
 * Criterion which specifies join fields
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JoinCriterion
    extends AbstractSearchCriterion {
    private String lhs;
    private String lhsRootAlias;
    private String rhs;
    private String rhsRootAlias;

    /**
     * Creates a new JoinCriterion object.
     *
     * @param rootAlias String.
     * @param lhs String left hand side of join.
     * @param rhs String right hand side of join.
     */
    public JoinCriterion(String rootAlias, String lhs, String lhsRootAlias, String rhs,
        String rhsRootAlias) {
        super(rootAlias);
        Assert.notNull(lhs, "lhs must be provided as a parameter");
        Assert.notNull(rhs, "rhs must be provided as a parameter");
        Assert.notNull(rhsRootAlias, "rhsRootAlias must be provided as a parameter");
        Assert.notNull(lhsRootAlias, "lhsRootAlias must be provided as a parameter");
        this.lhs = lhs;
        this.rhs = rhs;
        this.rhsRootAlias = rhsRootAlias;
        this.lhsRootAlias = lhsRootAlias;
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private JoinCriterion() {
        super();
    }

    /**
     * Ensure joins are created for both lhs and rhs
     *
     * @return JAVADOC.
     */
    @Override
    public Map<String, List<Join>> getJoins() {
        Map<String, List<Join>> twoSetsOfJoins = new HashMap<String, List<Join>>();

        twoSetsOfJoins.put(lhsRootAlias, super.getJoins(lhs));
        twoSetsOfJoins.put(rhsRootAlias, super.getJoins(rhs));

        return twoSetsOfJoins;
    }

    /**
    * Get left hand side of join
    *
    * @return lhs String.
    */
    public String getLhs() {
        return lhs;
    }

    /**
     * Get root alias of lhs
     *
     * @return String rootAlias of left hand side.
     */
    public String getLhsRootAlias() {
        return lhsRootAlias;
    }

    /**
    * Get restriction, lhs = rhs.
    *
    * @return restriction.
    */
    @Override
    public String getRestriction() {
        Assert.notNull(getParentAliases(), "There should be parent aliases");

        return getSearchPropertyName(lhs, getParentAlias(lhs, lhsRootAlias)) + " = " +
        getSearchPropertyName(rhs, getParentAlias(rhs, rhsRootAlias));
    }

    /**
     * Get right hand side of join
     *
     * @return rhs String.
     */
    public String getRhs() {
        return rhs;
    }

    /**
     * Get root alias of rhs
     *
     * @return String rootAlias of right hand side.
     */
    public String getRhsRootAlias() {
        return rhsRootAlias;
    }

    /**
    * Get values, empty in this case
    *
    * @return empty list.
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getValues() {
        return Collections.EMPTY_LIST;
    }
}
