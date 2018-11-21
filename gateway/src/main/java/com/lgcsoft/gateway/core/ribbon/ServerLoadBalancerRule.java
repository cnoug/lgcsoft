package com.lgcsoft.gateway.core.ribbon;

import com.google.common.base.Preconditions;
import com.lgcsoft.gateway.common.util.SystemUtil;
import com.lgcsoft.gateway.core.ribbon.balancer.LoadBalancer;
import com.lgcsoft.gateway.core.ribbon.balancer.RandomLoadBalancer;
import com.lgcsoft.gateway.core.ribbon.balancer.RoundLoadBalancer;
import com.lgcsoft.gateway.entity.GatewayAddress;
import com.lgcsoft.gateway.service.GatewayService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author shenyuhang
 * @date 2018/07/05.
 **/
public class ServerLoadBalancerRule extends AbstractLoadBalancerRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoadBalancerRule.class);

    @Value("${server.host:127.0.0.1}")
    private String host;
    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private GatewayService gatewayService;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        return getServer(getLoadBalancer(), key);
    }

    private Server getServer(ILoadBalancer loadBalancer, Object key) {
        if (StringUtils.isBlank(host)) {
            host = SystemUtil.ipList().get(0);
        }
        Preconditions.checkArgument(host != null, "server.host must be specify.");
        Preconditions.checkArgument(port != null, "server.port must be specify.");

        GatewayAddress address = gatewayService.getByHostAndPort(host, port);
        if (address == null) {
            LOGGER.error(String.format("must be config a gateway address for the server[%s:%s].", host, String.valueOf(port)));
            return null;
        }

        LoadBalancer balancer = LoadBalancerFactory.build(address.getFkStrategyId());

        return balancer.chooseServer(loadBalancer);
    }

    static class LoadBalancerFactory {

        public static LoadBalancer build(String strategy) {
            GatewayAddress.StrategyType type = GatewayAddress.StrategyType.of(strategy);
            switch (type) {
                case ROUND:
                    return new RoundLoadBalancer();
                case RANDOM:
                    return new RandomLoadBalancer();
                default:
                    return null;
            }
        }
    }
}
