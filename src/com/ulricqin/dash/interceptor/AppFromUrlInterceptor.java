package com.ulricqin.dash.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ulricqin.dash.model.App;
import com.ulricqin.frame.exception.RenderJsonMsgException;

public class AppFromUrlInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		int appId = controller.getParaToInt();
		if (appId < 1) {
			throw new RenderJsonMsgException("parameter id is invalid");
		}
		
		App app = App.dao.findById(appId);
		if (app == null) {
			throw new RenderJsonMsgException("no such app");
		}
		
		controller.setAttr("app", app);
		ai.invoke();
	}

}
