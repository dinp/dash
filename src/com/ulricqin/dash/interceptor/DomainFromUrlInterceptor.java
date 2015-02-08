package com.ulricqin.dash.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.ulricqin.dash.model.Domain;
import com.ulricqin.frame.exception.RenderJsonMsgException;

public class DomainFromUrlInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		int domainId = controller.getParaToInt();
		if (domainId < 1) {
			throw new RenderJsonMsgException("parameter id is invalid");
		}
		
		Domain d = Domain.dao.findById(domainId);
		if (d == null) {
			throw new RenderJsonMsgException("no such domain");
		}
		
		controller.setAttr("domain", d);
		ai.invoke();
	}

}
