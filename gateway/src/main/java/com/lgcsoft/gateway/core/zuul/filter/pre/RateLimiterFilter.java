package com.lgcsoft.gateway.core.zuul.filter.pre;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.google.common.util.concurrent.RateLimiter;
import com.lgcsoft.gateway.core.zuul.FilterOrder;
import com.netflix.zuul.context.RequestContext;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public class RateLimiterFilter extends AbstractPreZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterFilter.class);

	/**
	 * 每秒允许处理的量是50
	 */
	private static final RateLimiter rateLimiter = RateLimiter.create(10);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return FilterOrder.RATE_LIMITER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		// 只对-某一-接口限流
		if ("/apigateway/getorder".equalsIgnoreCase(request.getRequestURI())) {
			return true;
		}
		return false;
	}

	@Override
	public Object doRun() {
		HttpServletRequest request = context.getRequest();
		String url = request.getRequestURI();
		if (rateLimiter.tryAcquire()) {
			return success();
		} else {
			//就相当于每调用一次tryAcquire()方法，令牌数量减1，当1000个用完后，那么后面进来的用户无法访问上面接口
	        //当然这里只写类上面一个接口，可以这么写，实际可以在这里要加一层接口判断。
			LOGGER.info("rate limit:{}", url);
			context.setSendZuulResponse(false);
			// HttpStatus.TOO_MANY_REQUESTS.value()里面有静态代码常量
			context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
			return fail(401, String.format("rate limit:{}", url));
		}
	}
}
