package org.cucina.i18n.validation;

import java.util.Collection;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.ListItem;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.ListItemRepository;
import org.cucina.i18n.service.I18nService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueListItemValidator
    implements ConstraintValidator<UniqueListItem, ListItem> {
    private static final Logger LOG = LoggerFactory.getLogger(UniqueListItemValidator.class);
    @Autowired
    private I18nService i18nService;
    @Autowired
    private ListItemRepository listNodeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNode JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(ListItem listNode, ConstraintValidatorContext arg1) {
        Locale locale = findI18nService().getLocale();
        Message ref = listNode.getLabel();
        String refText = ref.getBestMessage(locale);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reference text for locale '" + locale + "' is  '" + refText + "'");
        }

        if (refText == null) {
            return true;
        }

        Collection<ListItem> results = findListNodeRepository().findByType(listNode.getType());

        for (ListItem ln : results) {
            Message message = ln.getLabel();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Label: " + message);
            }

            if (message == null) {
                continue;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(message.getBestMessage(locale));
            }

            if (refText.equals(message.getBestMessage(locale))) {
                return false;
            }
        }

        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param uniqueMessageCode JAVADOC.
     */
    @Override
    public void initialize(UniqueListItem uniqueListNode) {
    }

    private I18nService findI18nService() {
        if (null == i18nService) {
            LOG.debug("Failed to autowire, attempting to hotwire byName");
            i18nService = (I18nService) SingletonBeanFactory.getInstance()
                                                            .getBean(I18nService.I18N_SERVICE_ID);

            if (i18nService == null) {
                // this may not bring back the desired result
                LOG.debug("Failed to autowire, attempting to hotwire by class");

                i18nService = SingletonBeanFactory.getInstance().getBean(I18nService.class);
            }

            Assert.notNull(i18nService, "i18nService is null");
        }

        return i18nService;
    }

    private ListItemRepository findListNodeRepository() {
        if (null == listNodeRepository) {
            LOG.debug("Failed to autowire, attempting to hotwire byName");
            listNodeRepository = (ListItemRepository) SingletonBeanFactory.getInstance()
                                                                          .getBean(ListItemRepository.LISTNODE_REPOSITORY_ID);

            if (listNodeRepository == null) {
                // this may not bring back the desired result
                LOG.debug("Failed to autowire, attempting to hotwire by class");

                listNodeRepository = SingletonBeanFactory.getInstance()
                                                         .getBean(ListItemRepository.class);
            }

            Assert.notNull(listNodeRepository, "listNodeRepository is null");
        }

        return listNodeRepository;
    }
}
