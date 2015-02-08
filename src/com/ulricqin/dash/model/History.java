package com.ulricqin.dash.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class History extends Model<History>{

	private static final long serialVersionUID = 2247813999236765493L;
	public static final History dao = new History();
	
	public static List<History> getByAppId(long appId) {
		return dao.find("select * from `history` where app_id = ? order by id desc", appId);
	}

}
