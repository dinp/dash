package com.ulricqin.dash.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.api.UicApi;
import com.ulricqin.dash.config.Config;
import com.ulricqin.dash.interceptor.DomainFromUrlInterceptor;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.dash.model.Domain;
import com.ulricqin.dash.model.Team;
import com.ulricqin.dash.model.User;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.Checker;
import com.ulricqin.frame.kit.StringKit;

@Before(LoginRequiredInterceptor.class)
public class DomainController extends Controller {
	
	public void index() {
		User me = getAttr("me");
		List<Team> ts = UicApi.myTeams(me.getId());
		List<Integer> tids = new ArrayList<Integer>();
		if (ts != null && ts.size() > 0) {
			for (Team t : ts) {
				tids.add(t.getId());
			}
		}
		
		List<Domain> ds = Domain.mine(me.getId(), tids);
		setAttr("ds", ds);
		
		setAttr("title", "Domains");
		render("index.html");
	}
	
	public void create() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			createGet();
		} else {
			createPost();
		}
	}
	
	private void createGet() {
		User me = getAttr("me");
		List<Team> teams = UicApi.myTeams(me.getId());
		setAttr("ts", teams);
		setAttr("uic", Config.uicExternal);
		setAttr("title", "create app");
		render("create.html");
	}
	
	private void createPost() {
		String domain = getPara("domain", "");
		if (StringKit.isBlank(domain)) {
			throw new RenderJsonMsgException("parameter domain is blank");
		}
		
		Domain dObj = Domain.dao.findByName(domain);
		if (dObj != null) {
			throw new RenderJsonMsgException("domain has already existent. creator: " + dObj.getStr("creator_name"));
		}
		
		if (!Checker.isDomain(domain)) {
			throw new RenderJsonMsgException("domain format error");
		}
		
		int tid = 0;
		String tname = "";
		String team_id_name = getPara("team", "");
		if (StringKit.isNotBlank(team_id_name)) {
			int index = team_id_name.indexOf("-");
			if (index < 0) {
				throw new RenderJsonMsgException("parameter team is invalid");
			}
			
			String team_id = team_id_name.substring(0, index);
			tname = team_id_name.substring(index+1);
			tid = Integer.parseInt(team_id);
		}
		
		User me = getAttr("me");
		if (!Domain.save(domain, tid, tname, me)) {
			throw new RenderJsonMsgException("create domain fail");
		}
		
		renderJson();
	}
	
	@Before(DomainFromUrlInterceptor.class)
	public void edit() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			editGet();
		} else {
			editPost();
		}
	}
	
	private void editGet() {
		Domain domain = getAttr("domain");
		User me = getAttr("me");
		
		List<Team> teams = UicApi.myTeams(me.getId());
		setAttr("ts", teams);
		setAttr("uic", Config.uicExternal);
		setAttr("d", domain);
		render("edit.html");
	}
	
	private void editPost() {
		Domain domain = getAttr("domain");
		
		int tid = 0;
		String tname = "";
		String team_id_name = getPara("team", "");
		if (StringKit.isNotBlank(team_id_name)) {
			int index = team_id_name.indexOf("-");
			if (index < 0) {
				throw new RenderJsonMsgException("parameter team is invalid");
			}
			
			String team_id = team_id_name.substring(0, index);
			tname = team_id_name.substring(index+1);
			tid = Integer.parseInt(team_id);
		}
		
		if (!domain.updateTeam(tid, tname)) {
			throw new RenderJsonMsgException("update team fail");
		}
		
		renderJson();
	}
	
	@Before(DomainFromUrlInterceptor.class)
	public void bind() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			bindGet();
		} else {
			bindPost();
		}
	}
	
	private void bindGet() {
		Domain domain = getAttr("domain");
		setAttr("d", domain);
		
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
		render("bind.html");
	}

	private void bindPost() {
		Domain d = getAttr("domain");

		long appId = getParaToInt("app_id", 0);
		if (appId < 1) {
			throw new RenderJsonMsgException("parameter app_id is invalid");
		}

		App app = App.dao.findById(appId);
		if (app == null) {
			throw new RenderJsonMsgException("no such app");
		}

		User me = getAttr("me");

		d.set("app_id", appId);
		d.set("app_name", app.getStr("name"));
		d.set("bind_user_id", me.getId());

		if (!d.update()) {
			throw new RenderJsonMsgException("occur unknown error");
		}

		renderJson();
	}

	@Before(DomainFromUrlInterceptor.class)
	public void delete() {
		Domain d = getAttr("domain");

		if (!d.delete()) {
			throw new RenderJsonMsgException("occur unknown error");
		}

		renderJson();
	}
}
