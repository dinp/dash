package com.ulricqin.dash.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.api.UicApi;
import com.ulricqin.dash.config.Config;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.dash.model.Team;
import com.ulricqin.dash.model.User;

@Before(LoginRequiredInterceptor.class)
public class MainController extends Controller {

	public void index() {
		User me = getAttr("me");
		List<Team> ts = UicApi.myTeams(me.getId());
		List<Integer> tids = new ArrayList<Integer>();
		if (ts != null && ts.size() > 0) {
			for (Team t : ts) {
				tids.add(t.getId());
			}
		}
		
		List<App> apps = App.mine(me.getId(), tids);
		setAttr("apps", apps);
		
		setAttr("paasDomain", Config.paasDomain);
		setAttr("title", "PaaS-Dashboard");
		render("index.html");
	}
}
