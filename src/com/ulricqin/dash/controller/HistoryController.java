package com.ulricqin.dash.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.ulricqin.dash.interceptor.LoginRequiredInterceptor;
import com.ulricqin.dash.model.History;
import com.ulricqin.frame.exception.RenderJsonMsgException;

@Before(LoginRequiredInterceptor.class)
public class HistoryController extends Controller {

	public void delete() {
		int historyId = getParaToInt();
		if (historyId < 1) {
			throw new RenderJsonMsgException("parameter id is invalid");
		}
		
		History.dao.deleteById(historyId);
		
		renderJson();
	}
}
