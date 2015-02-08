package com.ulricqin.dash.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import com.ulricqin.dash.config.Config;

public class ServerApi {

	public static String containersOf(String appName) {
		HttpResponse resp;
		try {
			resp = Request.Get(Config.serverUrl + "/app/" + appName).execute()
					.returnResponse();
		} catch (IOException e) {
			return null;
		}
		StatusLine statusLine = resp.getStatusLine();
		if (statusLine.getStatusCode() != 200) {
			return null;
		}

		try {
			return EntityUtils.toString(resp.getEntity());
		} catch (Exception e) {
			return null;
		}
	}
}
