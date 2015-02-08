package com.ulricqin.dash.interceptor;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ulricqin.frame.exception.RenderJsonMsgException;
import com.ulricqin.frame.kit.StringKit;

public class GlobalInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		try {
			ai.invoke();
			String msg = controller.getAttr("msg");
			if (StringKit.isBlank(msg)) {
				controller.setAttr("msg", "");
			}
		} catch (RenderJsonMsgException e) {
			controller.renderJson("msg", e.getMessage());
		} catch (Exception e) {
			String msg = ExceptionUtils.getMessage(e);
			System.out.println(msg);
			System.out.println(ExceptionUtils.getStackTrace(e));
			controller.renderJson("msg", msg);
		}
	}
}
