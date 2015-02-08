package com.ulricqin.dash.config;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.ulricqin.dash.controller.ApiController;
import com.ulricqin.dash.controller.AppController;
import com.ulricqin.dash.controller.AuthController;
import com.ulricqin.dash.controller.CacheController;
import com.ulricqin.dash.controller.DomainController;
import com.ulricqin.dash.controller.EnvController;
import com.ulricqin.dash.controller.HealthController;
import com.ulricqin.dash.controller.HistoryController;
import com.ulricqin.dash.controller.MainController;
import com.ulricqin.dash.interceptor.GlobalInterceptor;
import com.ulricqin.dash.model.App;
import com.ulricqin.dash.model.Domain;
import com.ulricqin.dash.model.Env;
import com.ulricqin.dash.model.History;
import com.ulricqin.jfinal.ext.plugin.MemcachePlugin;

public class Config extends JFinalConfig {
	
	public static String uicExternal;
	public static String uicInternal;
	public static String paasDomain;
	public static String serverUrl;
	public static String token;
	public static String builder;

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setBaseViewPath("/WEB-INF/tpl");
		
		uicExternal = getProperty("uicExternal");
		uicInternal = getProperty("uicInternal");
		paasDomain = getProperty("paasDomain");
		serverUrl = getProperty("server");
		token = getProperty("token");
		builder = getProperty("builder");
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", MainController.class);
		me.add("/health", HealthController.class);
		me.add("/cache", CacheController.class);
		me.add("/auth", AuthController.class);
		me.add("/app", AppController.class);
		me.add("/env", EnvController.class);
		me.add("/domain", DomainController.class);
		me.add("/history", HistoryController.class);
		me.add("/api", ApiController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		String jdbcUrl = getProperty("jdbcUrl").trim();
		String jdbcUser = getProperty("user").trim();
		String jdbcPasswd = getProperty("password").trim();

		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, jdbcUser, jdbcPasswd);
		me.add(c3p0Plugin);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setShowSql(getPropertyToBoolean("devMode", false));
		me.add(arp);

		arp.addMapping("app", App.class);
		arp.addMapping("history", History.class);
		arp.addMapping("domain", Domain.class);
		arp.addMapping("env", Env.class);

		String memcacheAddrs = getProperty("memcacheAddrs").trim();
		String[] addrs = StringUtils.split(memcacheAddrs, ',');
		Integer[] weights = new Integer[addrs.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new Integer(10);
		}
		String prefix = getProperty("memcachePrefix").trim();
		MemcachePlugin memcachePlugin = new MemcachePlugin(addrs, weights);
		MemcachePlugin.setPrefix(prefix);
		me.add(memcachePlugin);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new GlobalInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {

	}

	public static void main(String[] args) {
		JFinal.start("web", 8088, "/", 5);
	}
}
