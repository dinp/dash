package com.ulricqin.dash.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.api.ServerApi;
import com.ulricqin.dash.interceptor.AppFromUrlInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.StringKit;

public class ApiController extends Controller {

	@Before(AppFromUrlInterceptor.class)
	public void instances() {
		App app  = getAttr("app");
		String jsonString = ServerApi.containersOf(app.getStr("name"));
		if (StringKit.isBlank(jsonString)) {
			throw new RenderJsonMsgException("curl server fail");
		}
		
		renderJson(jsonString);
	}
}
