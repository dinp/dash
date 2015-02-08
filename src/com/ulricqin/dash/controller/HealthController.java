package com.ulricqin.dash.controller;

import com.jfinal.core.Controller;

public class HealthController extends Controller {

	public void index() {
		renderText("ok");
	}
	
	public void json() {
		setAttr("name", "Ulric");
		renderJson();
	}
}
