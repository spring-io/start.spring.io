/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Main Controller.
 *
 * @author Brian Clozel
 */
@Controller
public class HomeController {

	@GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String home() {
		return "forward:index.html";
	}

}
