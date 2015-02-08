package com.ulricqin.dash.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.dash.model.Env;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.StringKit;

@Before(LoginRequiredInterceptor.class)
public class EnvController extends Controller {

	public void add() {
		String k = getPara("k", "");
		String v = getPara("v", "");
		long appId = getParaToInt("app_id", 0);
		if (appId < 1) {
			throw new RenderJsonMsgException("parameter app_id is invalid");
		}
		
		App app = App.dao.findById(appId);
		if (app == null) {
			throw new RenderJsonMsgException("no such app");
		}
		
		if (StringKit.isBlank(k)) {
			throw new RenderJsonMsgException("parameter k is blank");
		}
		
		Env env = new Env();
		env.set("k", k);
		env.set("v", v);
		env.set("app_id", appId);
		env.set("app_name", app.getStr("name"));
		if (!env.save()) {
			throw new RenderJsonMsgException("occur unknown error");
		}
		
		renderJson();
	}
	
	public void delete() {
		int id = getParaToInt("id", 0);
		if (id < 1) {
			throw new RenderJsonMsgException("parameter id is invalid");
		}
		
		if (!Env.dao.deleteById(id)) {
			throw new RenderJsonMsgException("occur unknown error");
		}
		
		renderJson();
	}
}
