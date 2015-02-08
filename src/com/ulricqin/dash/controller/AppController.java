package com.ulricqin.dash.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.api.UicApi;
import com.ulricqin.dash.config.Config;
import com.ulricqin.dash.interceptor.AppFromUrlInterceptor;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.dash.model.Domain;
import com.ulricqin.dash.model.Env;
import com.ulricqin.dash.model.History;
import com.ulricqin.dash.model.Team;
import com.ulricqin.dash.model.User;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.Checker;
import com.ulricqin.frame.kit.StringKit;

@Before(LoginRequiredInterceptor.class)
public class AppController extends Controller {

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
		setAttr("paasDomain", Config.paasDomain);
		setAttr("title", "create app");
		render("create.html");
	}
	
	private void createPost() {
		String health = getPara("health", "").trim();
		if (StringKit.isNotBlank(health) && !health.startsWith("/")) {
			throw new RenderJsonMsgException("health must starts with: /");
		}
		
		String name = getPara("name", "");
		if (StringKit.isBlank(name)) {
			throw new RenderJsonMsgException("parameter name is blank");
		}
		
		if (!Checker.isIdentifier(name)) {
			throw new RenderJsonMsgException("name should be an identifier: a-zA-Z0-9-_");
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
		if (!App.save(name, tid, tname, me.getId(), health)) {
			throw new RenderJsonMsgException("create app fail");
		}
		
		renderJson();
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void delete() {
		App app = getAttr("app");
		
		User me = getAttr("me");
		if (!app.delete(me)) {
			throw new RenderJsonMsgException("delete app fail");
		}
		
		renderJson();
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void env() {
		App app = getAttr("app");
		
		List<Env> envs = Env.dao.findByAppId(app.getLong("id"));
		setAttr("envs", envs);
		setAttr("app", app);
		render("env.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void domain() {
		App app = getAttr("app");
		
		List<Domain> domains = Domain.dao.findByAppId(app.getLong("id"));
		setAttr("domains", domains);
		setAttr("app", app);
		render("domain.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void scale() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			scaleGet();
		} else {
			scalePost();
		}
	}
	
	private void scalePost() {
		App app = getAttr("app");
		
		int instance = getParaToInt("instance", 0);
		if (!app.scale(instance)) {
			throw new RenderJsonMsgException("occur unknown error");
		}
		
		renderJson();
	}

	private void scaleGet() {
		App app = getAttr("app");
		setAttr("app", app);
		render("scale.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void edit() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			editGet();
		} else {
			editPost();
		}
	}
	
	private void editPost() {
		App app = getAttr("app");
		String health = getPara("health", "").trim();
		if (StringKit.isNotBlank(health) && !health.startsWith("/")) {
			throw new RenderJsonMsgException("health must starts with: /");
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
		
		if (!app.updateProfile(tid, tname, health)) {
			throw new RenderJsonMsgException("update team fail");
		}
		
		renderJson();
	}

	private void editGet() {
		App app = getAttr("app");
		User me = getAttr("me");
		
		List<Team> teams = UicApi.myTeams(me.getId());
		setAttr("ts", teams);
		setAttr("uic", Config.uicExternal);
		setAttr("app", app);
		render("edit.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void history() {
		App app = getAttr("app");
		
		List<History> list = History.getByAppId(app.getLong("id"));
		setAttr("list", list);
		setAttr("app", app);
		setAttr("title", "build history");
		render("history.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void instance() {
		App app = getAttr("app");
		setAttr("app", app);
		setAttr("paasDomain", Config.paasDomain);
		setAttr("title", "instances");
		render("instance.html");
	}
	
	@Before(AppFromUrlInterceptor.class)
	public void deploy() {
		if (getRequest().getMethod().equalsIgnoreCase("GET")) {
			deployGet();
		} else {
			deployPost();
		}
	}
	
	private void deployGet() {
		App app = getAttr("app");
		setAttr("app", app);
		
		int historyId = getParaToInt("history", 0);
		if (historyId > 0) {
			setAttr("history", History.dao.findById(historyId));
		}
		
		setAttr("builder", Config.builder);
		setAttr("title", "deploy");
		render("deploy.html");
	}
	
	private void deployPost() {
		App app = getAttr("app");
		int memory = getParaToInt("memory", 0);
		if (memory < 128 || memory > 10240) {
			throw new RenderJsonMsgException("memory should >=128 or <= 10240");
		}
		
		int instance = getParaToInt("instance", 0);
		String image = getPara("image", "");
		if (StringKit.isBlank(image)) {
			throw new RenderJsonMsgException("image is blank");
		}
		
		String resume = getPara("resume", "");
		if (!app.deploy(memory, instance, image, resume)) {
			throw new RenderJsonMsgException("occur unknown error");
		}
		
		renderJson();
	}
}
