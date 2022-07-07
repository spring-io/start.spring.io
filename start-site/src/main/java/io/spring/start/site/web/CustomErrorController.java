/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom Error Controller.
 *
 * @author 10xtechie
 */
@Controller
public class CustomErrorController implements ErrorController {

	@Autowired
	private ErrorAttributes errorAttributes;

	@GetMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
	public String handleErrorInHtml(WebRequest webRequest, HttpServletResponse response) {
		ErrorResponse aErrorResponse = new ErrorResponse(
				getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()));
		int statusCode = Integer.parseInt(aErrorResponse.getStatus());
		if (statusCode >= 400 && statusCode <= 499) {
			return "forward:fourxx.html";
		}
		else if (statusCode >= 500 && statusCode <= 599) {
			return "forward:fivexx.html";
		}
		else {
			return "forward:error.html";
		}
	}

	@RequestMapping(path = "/error", produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ErrorResponse> error(WebRequest webRequest, HttpServletResponse response) {
		ErrorResponse aErrorResponse = new ErrorResponse(
				getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()));
		return ResponseEntity.ok(aErrorResponse);
	}

	private Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		return errorAttributes.getErrorAttributes(webRequest, options);
	}

}
