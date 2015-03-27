package org.cucina.engine.server.web.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.cucina.engine.service.DefinitionService;


/**
 *
 *
 * @author vlevine
  */
@Controller
@RequestMapping(value = "/workflow")
// TODO apply security
public class RestfulDefinitionController {
    @Autowired
    private DefinitionService definitionService;

    /**
     *
     *
     * @param name .
     * @param file .
     *
     * @return .
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("name")
    String name, @RequestParam("file")
    MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                definitionService.create(name, "text/xml", file.getBytes());

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }

        return "You failed to upload " + name + " because the file was empty.";
    }

    /**
     *
     *
     * @return list of all workflow definitions.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> list() {
        return definitionService.list();
    }
}
