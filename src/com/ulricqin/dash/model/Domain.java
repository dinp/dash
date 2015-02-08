package com.ulricqin.dash.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class Domain extends Model<Domain>{

	private static final long serialVersionUID = -9021991906913012024L;
	public static final Domain dao = new Domain();

	public static void deleteByAppId(Long appId) {
		Db.update("delete from domain where app_id = ?", appId);
	}
	
	public static void unbindApp(Long appId) {
		Db.update("update domain set app_id = 0, app_name = '' where app_id = ?", appId);
	}
	
	public List<Domain> findByAppId(long appId) {
		return dao.find("select * from `domain` where app_id = ?", appId);
	}
	
	public Domain findByName(String domain) {
		return dao.findFirst("select * from `domain` where `domain` = ?", domain);
	}

	public static List<Domain> mine(int creator, List<Integer> tids) {
		StringBuilder buf = new StringBuilder("select * from `domain` where `creator` = ?");
		if (tids.size() > 0) {
			buf.append(" or `team_id` in (").append(StringUtils.join(tids, ',')).append(")");
		}
		buf.append(" order by domain");
		
		return dao.find(buf.toString(), creator);
	}

	public static boolean save(String domain, int tid, String tname, User u) {
		Domain d = new Domain();
		d.set("domain", domain);
		d.set("team_id", tid);
		d.set("team_name", tname);
		d.set("creator", u.getId());
		d.set("creator_name", u.getName());
		d.set("bind_user_id", u.getId());
		return d.save();
	}

	public boolean updateTeam(int tid, String tname) {
		this.set("team_id", tid);
		this.set("team_name", tname);
		return this.update();
	}

}
