package org.cucina.engine.server.service;

import org.cucina.engine.service.DefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;


/**
 * @author vlevine
 */
@Controller
@RequestMapping(value = "/workflow")
public class DefinitionController {
	@Autowired
	private DefinitionService definitionService;

	/**
	 * @param name .
	 * @param file .
	 * @return .
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(@RequestParam("file")
										   MultipartFile file) {
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename();
			try {
				definitionService.save(name, "text/xml", file.getBytes());

				return "You successfully uploaded " + name + "!";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}

		return "You failed to upload because the file was empty.";
	}

	/**
	 * @return list of all workflow definitions.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> list() {
		return definitionService.list();
	}

	/**
	 * @return .
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	@ResponseBody
	public String simpleForm() {
		return "<html><body><form method=\"POST\" enctype=\"multipart/form-data\" action=\"/workflow/upload\">" +
				"File to upload: <input type=\"file\" name=\"file\"><br />" +
				"<input type=\"submit\" value=\"Upload\">Press here to upload the file!</form></body></html>";
	}
}
