package com.ulricqin.dash.controller;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;

import com.jfinal.core.Controller;
import com.ulricqin.dash.config.Config;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.StringKit;

public class AuthController extends Controller {

	public void logout() {
		String sig = getCookie("sig");
		if (StringKit.isBlank(sig)) {
			throw new RenderJsonMsgException("u'r not login");
		}
		
		HttpResponse resp = null;
		try {
			resp = Request.Get(Config.uicInternal + "/sso/logout/" + sig  + "?token=" + Config.token).execute()
					.returnResponse();
		} catch (IOException e) {
			throw new RenderJsonMsgException(e.getMessage());
		}
		
		StatusLine statusLine = resp.getStatusLine();
		if (statusLine.getStatusCode() != 200 && statusLine.getStatusCode() != 404) {
			throw new RenderJsonMsgException("curl " + Config.uicInternal + "/sso/logout/" + sig + " fail");
		}
		
		setCookie("sig", "", 0, "/");
		redirect("/");
	}

}
