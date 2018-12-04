package com.lgcsoft.gateway.core.zuul.filter.post;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgcsoft.gateway.core.zuul.FilterType;
import com.lgcsoft.gateway.core.zuul.filter.AbstractZuulFilter;
import com.netflix.zuul.context.RequestContext;

import net.sf.json.JSONObject;

/**
 * @author liugc@neusoft.com
 *
 */
public class PostFilter extends AbstractZuulFilter {

	private static Logger log = LoggerFactory.getLogger(PostFilter.class);

	@Override
	public String filterType() {
		// 后置过滤器
		return FilterType.post.name();
	}

	@Override
	public int filterOrder() {
		// 优先级，数字越大，优先级越低
		return -2;
	}

	@Override
	public boolean shouldFilter() {
		// 是否执行该过滤器，true代表需要过滤
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		log.info("hi, comming post filter !");
		JSONObject jo = new JSONObject();
		if (StringUtils.isEmpty(ctx.getResponseBody()) && ctx.getResponseStatusCode() != 200) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(200);
			jo.put("status", ctx.getResponseStatusCode());
			jo.put("result", "false");
			jo.put("message", "后端服务异常");
			// 为使得中文字符不乱码
			ctx.getResponse().setCharacterEncoding("UTF-8");
			ctx.getResponse().setContentType("application/json;charset=utf-8");
			ctx.setResponseBody(jo.toString());
		} else {
			log.info("返回结果:::" + ctx.getResponseStatusCode());
		}
		return null;

	}

	@Override
	public Object doRun() {
		// TODO Auto-generated method stub
		return run();
	}

}
