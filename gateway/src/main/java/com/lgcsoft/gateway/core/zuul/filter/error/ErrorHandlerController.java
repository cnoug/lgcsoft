package com.lgcsoft.gateway.core.zuul.filter.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

/**
 * 
 * @author liugc@neusoft.com
 *
 */
@RestController
public class ErrorHandlerController implements ErrorController {
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

	/**
	 * 出异常后进入该方法，交由下面的方法处理
	 */
	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	public Object error(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = new JSONObject();
		Integer status_code = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String message = (String) request.getAttribute("javax.servlet.error.message");
		String request_uri = (String) request.getAttribute("javax.servlet.error.request_uri");
		String servlet_name = (String) request.getAttribute("javax.servlet.error.servlet_name");
		// return Result.choose(status, status == 404 ? "访问地址不存在" : "内部服务器错误,正在处理");
		jo.put("status_code", status_code + "");
		message = (status_code == 404) ? "访问地址不存在" : message;
		jo.put("message", message);
		jo.put("request_uri", request_uri + "");
		jo.put("servlet_name", servlet_name + "");
		logger.info("请求异常:::" + jo.toString());
		return (jo);
	}
}