package org.cucina.email;

import org.cucina.email.service.AbstractEmailHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@RestController
public class HttpEmailHandler
    extends AbstractEmailHandler {
    /**
     * JAVADOC Method Level Comments
     */
    @RequestMapping(value = "/addTemplate", method = RequestMethod.POST)
    public void addTemplate() {
        // TODO
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param emailDto JAVADOC.
     */
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public void sendEmail(@RequestBody
    EmailDto dto) {
        sendEmail(dto);
    }
}
