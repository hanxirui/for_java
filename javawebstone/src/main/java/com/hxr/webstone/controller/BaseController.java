package com.hxr.webstone.controller;

import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {
	public final static String SUCCESS = "success";
	public final static String FAILED = "failed";
	public final static String JSON_ARRAY_NONE = "[]";
	public final static String REQ_RESULT_TPL = "{\"status\":\"%s\",\"errorMsg\": \"%s\"}";

}
