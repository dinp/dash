package com.ulricqin.dash.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.User;
import com.ulricqin.jfinal.ext.plugin.MemcachePlugin;

@Before(LoginRequiredInterceptor.class)
public class CacheController extends Controller {

	public void flush() {
		User me = getAttr("me");
		if (me.getRole() > 0) {
			renderText("" + MemcachePlugin.flushAll());
		} else {
			renderText("no privilege");
		}
	}
}
