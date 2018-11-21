package com.lgcsoft.gateway.core.zuul.filter.pre;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;
import com.lgcsoft.gateway.core.zuul.FilterOrder;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public class RateLimiterFilter extends AbstractPreZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterFilter.class);

    /**
     * 每秒允许处理的量是50
     */
    RateLimiter rateLimiter = RateLimiter.create(50);

    @Override
    public int filterOrder() {
        return FilterOrder.RATE_LIMITER_ORDER;
    }

    @Override
    public Object doRun() {
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURI();
        if (rateLimiter.tryAcquire()) {
            return success();
        } else {
            LOGGER.info("rate limit:{}", url);
            return fail(401, String.format("rate limit:{}", url));
        }
    }
}
