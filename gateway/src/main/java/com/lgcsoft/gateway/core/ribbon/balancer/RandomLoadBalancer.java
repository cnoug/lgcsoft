package com.lgcsoft.gateway.core.ribbon.balancer;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public class RandomLoadBalancer extends AbstractLoadBalancer {
    @Override
    public Server choose(ILoadBalancer loadBalancer) {
        return null;
    }
}
