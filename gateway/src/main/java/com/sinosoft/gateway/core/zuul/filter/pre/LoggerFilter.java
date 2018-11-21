package com.sinosoft.gateway.core.zuul.filter.pre;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.context.RequestContext;
import com.sinosoft.gateway.core.zuul.FilterType;

@Component
public class LoggerFilter extends AbstractPreZuulFilter {

	@Override
	public String filterType() {
		return FilterType.post.name();
	}

	@Override
	public int filterOrder() {
		return this.filterOrder() - 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		// 请求的类型，post get ..
		String method = request.getMethod();
		// Map<String, String> params = HttpUtils.getParams(request);
		Map<String, String> params = new HashMap<String, String>();
		// 请求的参数
		String paramsStr = params.toString();
		// 请求的开始时间
		long statrtTime = (long) context.get("startTime");
		// 请求的异常，如果有的话
		Throwable throwable = context.getThrowable();
		// 请求的uri
		request.getRequestURI();
		// 请求的iP地址
		// HttpUtils.getIpAddress(request);
		// 请求的状态
		context.getResponseStatusCode();
		// 请求耗时
		long duration = System.currentTimeMillis() - statrtTime;

		return null;
	}

	@Override
	public Object doRun() {
		// TODO Auto-generated method stub
		return null;
	}

}