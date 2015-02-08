package com.ulricqin.dash.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.ulricqin.dash.api.UicApi;

public class App extends Model<App>{

	private static final long serialVersionUID = -7308337862247316589L;
	public static final App dao = new App();

	public static boolean save(String name, int team_id, String team_name, int user_id, String health) {
		App a = new App();
		a.set("name", name);
		a.set("team_id", team_id);
		a.set("team_name", team_name);
		a.set("creator", user_id);
		a.set("health", health);
		return a.save();
	}

	public static List<App> mine(int creator, List<Integer> tids) {
		StringBuilder buf = new StringBuilder("select * from `app` where `creator` = ?");
		if (tids.size() > 0) {
			buf.append(" or `team_id` in (").append(StringUtils.join(tids, ',')).append(")");
		}
		buf.append(" order by name");
		
		return dao.find(buf.toString(), creator);
	}

	public boolean delete(User creator) {
		if (!hasPrivilege(creator)) {
			return false;
		}
		
		return deleteInTx(creator);
	}

	@Before(Tx.class)
	private boolean deleteInTx(User creator) {
		boolean success = this.delete();
		if (!success) {
			return false;
		}
		
		Domain.unbindApp(this.getLong("id"));
		Env.deleteByAppId(this.getLong("id"));
		
		return true;
	}

	private boolean hasPrivilege(User creator) {
		boolean hasPrivilege = false;
		if (this.getLong("creator").intValue() == creator.getId()) {
			hasPrivilege = true;
		} else {
			List<Team> teams = UicApi.myTeams(creator.getId());
			for (Team team : teams) {
				if (team.getId() == this.getLong("team_id").intValue() && team.getName().equals(this.getStr("team_name"))) {
					hasPrivilege = true;
				}
			}
		}
		return hasPrivilege;
	}

	public boolean scale(int instance) {
		this.set("instance", instance);
		this.set("status", 0);
		return this.update();
	}

	public boolean updateProfile(int tid, String tname, String health) {
		this.set("team_id", tid);
		this.set("team_name", tname);
		this.set("health", health);
		return this.update();
	}

	@Before(Tx.class)
	public boolean deploy(int memory, int instance, String image,
			String resume) {
		History his = new History();
		his.set("image", image);
		his.set("resume", resume);
		his.set("app_id", this.getLong("id"));
		his.set("app_name", this.getStr("name"));
		
		this.set("memory", memory);
		this.set("instance", instance);
		this.set("image", image);
		this.set("status", 0);
		
		return his.save() && this.update();
	}

}
