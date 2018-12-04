package com.lgcsoft.gateway.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.lgcsoft.gateway.core.zuul.router.AbstractDynamicRouter;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@Service
public class RefreshRouteService {
    public final static Logger logger = LoggerFactory.getLogger(RefreshRouteService.class);

    @Autowired
    ApplicationEventPublisher publisher;

    
    //RouteLocator routeLocator;
    @Autowired
    AbstractDynamicRouter routeLocator;

    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
        logger.info("刷新了路由规则......");
    }

}
