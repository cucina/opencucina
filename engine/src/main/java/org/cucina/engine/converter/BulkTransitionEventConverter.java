
package org.cucina.engine.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.engine.model.HistoryRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;


/**
 * Converter of String of format <type>:<workflowId>:<transition> or <type>:<workflowId>:<transition>:<comment>
 * into a BulkTransitionEvent.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BulkTransitionEventConverter
    implements Converter<String, PluralTransitionEvent> {
    /** This is a field JAVADOC */
    public static final String DEFAULT_COMMENT = "Transitioned by system";

    /**
     * Convert String into BulkTransitionEvent, barfs if String split on ':' does not result in
     * at least two tokens.
     *
     * @param source String.
     *
     * @return BulkTransitionEvent.
     */
    @Override
    public PluralTransitionEvent convert(String source) {
        if (StringUtils.isNotEmpty(source)) {
            String[] values = source.split(":");

            Assert.isTrue(values.length > 2,
                "Workflow transition value must be at least of the format <type>:<workflowId>:<transitionId>, value [" +
                source + "]");

            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put(HistoryRecord.COMMENTS_PROPERTY,
                (values.length > 3) ? values[3] : DEFAULT_COMMENT);

            return new PluralTransitionEvent(values[2], values[1], values[0], null, parameters);
        }

        return null;
    }
}
