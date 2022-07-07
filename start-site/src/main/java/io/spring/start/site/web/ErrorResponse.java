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

/**
 * Error Response.
 *
 * @author 10xtechie
 */
public class ErrorResponse {

	private String status;

	private String path;

	private String errorMessage;

	private String timeStamp;

	private String trace;

	public ErrorResponse(Map<String, Object> aErrorAttributes) {
		Object[] allAttributes = aErrorAttributes.values().toArray();
		this.status = allAttributes[1].toString();
		this.path = allAttributes[3].toString();
		this.errorMessage = allAttributes[2].toString();
		this.timeStamp = allAttributes[0].toString();
		this.trace = allAttributes[0].toString();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

}