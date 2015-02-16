
package org.cucina.email;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.StringUtils;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision$
 */
public class MailUtils {
    private static final Pattern SPLIT_LINES = Pattern.compile(":");
    private static final Pattern LINE_ENDING_SPLIT = Pattern.compile("\n");

    /**
    * JAVADOC.
    *
    * @param input JAVADOC.
    *
    * @return JAVADOC.
    */
    public static String resolveSubjectAndBody(String input, MimeMessage message)
        throws MessagingException {
        if (null == input) {
            return null;
        }

        input = input.trim();

        String[] splitByLineEnding = LINE_ENDING_SPLIT.split(input);

        String subject = extractStringStartingWith(splitByLineEnding, "subject");

        message.setSubject(StringUtils.isNotEmpty(subject) ? subject : "Sprite message");

        return rebuildBody(splitByLineEnding);
    }

    private static String extractStringStartingWith(String[] parts, String prefix) {
        for (int i = 0; i < parts.length; i++) {
            if ((parts[i] != null) && parts[i].toLowerCase().startsWith(prefix)) {
                String[] splitLines = SPLIT_LINES.split(parts[i]);

                if (splitLines.length >= 2) {
                    //remove the first token i.e. Subject:
                    String result = StringUtils.removeStart(parts[i], splitLines[0] + ":");

                    parts[i] = null;

                    return (result == null) ? result : result.trim();
                }
            }
        }

        return null;
    }

    private static String rebuildBody(String[] parts) {
        List<String> list = new ArrayList<String>();

        CollectionUtils.addAll(list, parts);
        CollectionUtils.filter(list, NotNullPredicate.getInstance());

        return StringUtils.join(list, "\n");
    }
}
