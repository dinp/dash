package com.ulricqin.dash.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ulricqin.dash.api.UicApi;
import com.ulricqin.dash.config.Config;
import com.ulricqin.dash.model.User;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.StringKit;

public class LoginRequiredInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		String sig = controller.getCookie("sig");
		if (sig == null || sig.equals("")) {
			redirectToSSO(controller);
			return;
		}
		
		User user = UicApi.getUser(sig);
		if (user == null) {
			redirectToSSO(controller);
			return;
		}
		
		if (user.getRole() < 0) {
			redirectToSSO(controller);
			return;
		}
		
		controller.setAttr("me", user);
		ai.invoke();
	}

	private void redirectToSSO(Controller controller) {
		String sig = UicApi.genSig();
		if (StringKit.isBlank(sig)) {
			throw new RenderJsonMsgException("gen sig fail");
		}

		HttpServletRequest req = controller.getRequest();
		String callback = req.getRequestURL().toString();
		String queryString = req.getQueryString();
		if (StringKit.isNotBlank(queryString)) {
			callback += "?" + queryString;
		}
		
		String cb;
		try {
			cb = URLEncoder.encode(callback, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RenderJsonMsgException("url encoding fail:"
					+ e.getMessage());
		}
		
		controller.setCookie(genCookie(sig));

		controller.redirect(Config.uicExternal + "/auth/login?sig=" + sig
				+ "&callback=" + cb);
	}
	
	private Cookie genCookie(String sig) {
		int maxAge = 3600 * 24 * 30;
		Cookie cookie = new Cookie("sig", sig);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(true);
		return cookie;
	}

}
