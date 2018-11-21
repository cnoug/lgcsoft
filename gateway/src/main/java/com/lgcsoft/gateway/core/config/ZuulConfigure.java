package com.lgcsoft.gateway.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lgcsoft.gateway.core.ribbon.ServerLoadBalancerRule;
import com.lgcsoft.gateway.core.zuul.filter.pre.RateLimiterFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.TokenAccessFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.UserRightFilter;
import com.lgcsoft.gateway.core.zuul.router.DataBaseRouter;
import com.lgcsoft.gateway.core.zuul.router.PropertiesRouter;
import com.lgcsoft.gateway.zuul.CustomRouteLocator;
import com.netflix.loadbalancer.IRule;
import com.netflix.zuul.ZuulFilter;

/**
 * @author 
 * @date 2018/07/05.
 **/
@Configuration
public class ZuulConfigure {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 动态路由 from properties
     * @return
     */
    /*
    @Bean
    public PropertiesRouter propertiesRouter() {
        return new PropertiesRouter(this.server.getServletPrefix(), this.zuulProperties);
    }
    */
    @Bean
    public DataBaseRouter dataBaseRouter() {
    	DataBaseRouter routeLocator = new DataBaseRouter(this.server.getServletPrefix(), this.zuulProperties);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }
    /**
     * 动态负载
     * @return
     */
    @Bean
    public IRule loadBalance() {
        return new ServerLoadBalancerRule();
    }

    @Bean
    public ZuulFilter userFilter() {
        return new UserRightFilter();
    }

    @Bean
    public ZuulFilter rateLimiterFilter() {
        return new RateLimiterFilter();
    }

    @Bean
    public ZuulFilter tokenAccessFilter() {
        return new TokenAccessFilter();
    }
}
