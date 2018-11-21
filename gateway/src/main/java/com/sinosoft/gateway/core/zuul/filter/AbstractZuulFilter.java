package com.sinosoft.gateway.core.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sinosoft.gateway.core.zuul.ContantValue;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public abstract class AbstractZuulFilter extends ZuulFilter {
	
	private final Logger logger = LoggerFactory.getLogger(AbstractZuulFilter.class);

    protected RequestContext context;

    //表示是否需要执行该filter，true表示执行，false表示不执行
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) (ctx.getOrDefault(ContantValue.NEXT_FILTER, true));
    }

    //filter需要执行的具体操作
    @Override
    public Object run() {
        context = RequestContext.getCurrentContext();
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("--->>> TokenFilter {},{}", request.getMethod(), request.getRequestURL().toString());
        return doRun();
    }

    public abstract Object doRun();

    //定义filter的顺序，数字越小表示顺序越高，越先执行
    @Override
	public int filterOrder() {
        return 10; 
    }
    public Object fail(Integer code, String message) {
        context.set(ContantValue.NEXT_FILTER, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format("{\"result\":\"%s!\"}", message));
        return null;
    }

    public Object success() {
        context.set(ContantValue.NEXT_FILTER, true);
        return null;
    }
}
