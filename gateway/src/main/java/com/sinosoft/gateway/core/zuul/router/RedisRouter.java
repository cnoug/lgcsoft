package com.sinosoft.gateway.core.zuul.router;

import java.util.List;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import com.sinosoft.gateway.core.zuul.entity.BasicRoute;

/**
 * @author liugc@neusoft.com
 *
 */
public class RedisRouter extends AbstractDynamicRouter {

    public RedisRouter(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    protected List<BasicRoute> readRoutes() {
        return null;
    }
}
