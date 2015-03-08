package org.cucina.loader;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.service.BeanPopulator;
import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.service.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListNodeMessageBeanPopulator
    implements BeanPopulator {
    protected static final String LISTNODE_BASENAME = "messages.listnode";
    private static final Logger LOG = LoggerFactory.getLogger(ListNodeMessageBeanPopulator.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param entity JAVADOC.
     * @param params not used.
     *
     * @return JAVADOC.
     */
    @Override
    public <T> T populate(T entity, Map<String, Object> params) {
        if (!(entity instanceof ListNode)) {
            LOG.warn("Weird. Object is not a ListNode. Should be. " +
                ((entity != null) ? entity.getClass().getName() : ""));

            return entity;
        }

        ListNode listNode = (ListNode) entity;
        Message label = listNode.getLabel();
        String type = listNode.getType();

        if (label == null) {
            LOG.warn("Message is missing from ListNode. Returning");

            return entity;
        }

        String i18nText = label.getMessageTx(MessageHelper.getDefaultLocale().toString());

        if (StringUtils.isBlank(type) || StringUtils.isBlank(i18nText)) {
            LOG.warn("Type or i18n text is missing from ListNode. Returning. Type [" + type +
                "], i18nText [" + i18nText + "]");

            return entity;
        }

        if (StringUtils.isBlank(label.getMessageCd())) {
            String msgCd = type + "." + i18nText.replaceAll("\\s", ".");

            label.setMessageCd(msgCd);
        }

        label.setBaseName(LISTNODE_BASENAME);

        return entity;
    }
}
