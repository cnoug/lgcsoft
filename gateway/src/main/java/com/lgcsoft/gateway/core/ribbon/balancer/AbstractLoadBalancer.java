package com.lgcsoft.gateway.core.ribbon.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgcsoft.gateway.core.SpringContext;
import com.lgcsoft.gateway.service.ServerService;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public abstract class AbstractLoadBalancer implements LoadBalancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoadBalancer.class);
    protected ServerService serverService;

    @Override
    public Server chooseServer(ILoadBalancer loadBalancer) {
        this.serverService = SpringContext.getBean(ServerService.class);
        Server server = choose(loadBalancer);
        if (server != null) {
            LOGGER.info(String.format("the server[%s:%s] has been select.", server.getHost(), server.getPort()));
        } else {
            LOGGER.error("could not find server.");
        }
        return server;
    }

    public abstract Server choose(ILoadBalancer loadBalancer);
}
