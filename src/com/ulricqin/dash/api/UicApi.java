package com.ulricqin.dash.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ulricqin.dash.config.Config;
import com.ulricqin.dash.model.Team;
import com.ulricqin.dash.model.User;
import com.ulricqin.jfinal.ext.plugin.MemcachePlugin;

public class UicApi {

	private static final Log log = LogFactory.getLog(UicApi.class);

	public static String genSig() {
		try {
			HttpResponse resp = Request.Get(Config.uicInternal + "/sso/sig").execute()
					.returnResponse();
			StatusLine statusLine = resp.getStatusLine();
			if (statusLine.getStatusCode() != 200) {
				return null;
			}
			return EntityUtils.toString(resp.getEntity());
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
	
	public static User getUser(String sig) {
		String key = "u:"+sig;
		Object val = MemcachePlugin.get(key);
		if (val == null) {
			User u = getUserFromSSO(sig);
			if (u == null) {
				return null;
			}
			
			MemcachePlugin.set(key, u);
			return u;
		}
		
		return (User)val;
	}
	
	public static User getUserFromSSO(String sig) {
		try {
			HttpResponse resp = Request.Get(Config.uicInternal + "/sso/user/" + sig + "?token=" + Config.token).execute()
					.returnResponse();
			StatusLine statusLine = resp.getStatusLine();
			if (statusLine.getStatusCode() != 200) {
				return null;
			}
			String content =  EntityUtils.toString(resp.getEntity());
			JSONObject jsonObject = JSON.parseObject(content);
			if (!jsonObject.containsKey("user")) {
				return null;
			}
			
			JSONObject userJson = jsonObject.getJSONObject("user");
			if (userJson == null) {
				return null;
			}
			
			User u = JSON.toJavaObject(userJson, User.class);
			return u;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static List<Team> myTeams(int uid) {
		try {
			HttpResponse resp = Request.Get(Config.uicInternal + "/team/mine/" + uid + "?token=" + Config.token).execute()
					.returnResponse();
			StatusLine statusLine = resp.getStatusLine();
			if (statusLine.getStatusCode() != 200) {
				return Collections.emptyList();
			}
			
			String content =  EntityUtils.toString(resp.getEntity());
			JSONObject jsonObject = JSON.parseObject(content);
			if (!jsonObject.containsKey("teams")) {
				return Collections.emptyList();
			}
			
			JSONArray jsonArray = jsonObject.getJSONArray("teams");
			if (jsonArray == null || jsonArray.size() == 0) {
				return Collections.emptyList();
			}
			
			List<Team> list = new ArrayList<Team>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonTeam = jsonArray.getJSONObject(i);
				list.add(new Team(jsonTeam.getIntValue("id"), jsonTeam.getString("name")));
			}
			
			return list;
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}
	
	public static void main(String[] args) {
		// sig: de2b8faabdfd4ff58f35e209a13473d4
		System.out.println(getUserFromSSO("de2b8faabdfd4ff58f35e209a13473d4"));
	}
}
