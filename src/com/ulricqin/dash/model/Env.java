package com.ulricqin.dash.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class Env extends Model<Env>{

	private static final long serialVersionUID = -2098540094986122367L;
	public static final Env dao = new Env();

	public static void deleteByAppId(Long appId) {
		Db.update("delete from `env` where app_id = ?", appId);
	}

	public List<Env> findByAppId(long appId) {
		return dao.find("select * from `env` where app_id = ?", appId);
	}

}
