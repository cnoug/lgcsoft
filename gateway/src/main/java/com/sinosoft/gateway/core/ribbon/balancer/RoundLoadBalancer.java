package com.sinosoft.gateway.core.ribbon.balancer;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.sinosoft.gateway.common.Constant;
import com.sinosoft.gateway.core.GlobalCache;
import com.sinosoft.gateway.core.ribbon.LoadBalancerRuleUtil;
import com.sinosoft.gateway.entity.ServerAddress;

import java.util.List;

/**
 * 权重轮询
 * 首次使用取最大权重的服务器。而后通过权重的不断递减，寻找适合的服务器。
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public class RoundLoadBalancer extends AbstractLoadBalancer {

    private Integer currentServer;
    private Integer currentWeight;
    private Integer maxWeight;
    private Integer gcdWeight;

    @Override
    public Server choose(ILoadBalancer loadBalancer) {
        List<ServerAddress> addressList = serverService.getAvailableServer();
        if (addressList != null && !addressList.isEmpty()) {
            maxWeight = LoadBalancerRuleUtil.getMaxWeightForServers(addressList);
            gcdWeight = LoadBalancerRuleUtil.getGCDForServers(addressList);
            currentServer = Integer.parseInt(GlobalCache.instance().getOrDefault(Constant.CURRENT_SERVER_KEY, -1).toString());
            currentWeight = Integer.parseInt(GlobalCache.instance().getOrDefault(Constant.CURRENT_WEIGHT_KEY, 0).toString());

            Integer serverCount = addressList.size();

            if (1 == serverCount) {
                return new Server(addressList.get(0).getHost(), addressList.get(0).getPort());
            } else {
                while (true) {
                    currentServer = (currentServer + 1) % serverCount;
                    if (currentServer == 0) {
                        currentWeight = currentWeight - gcdWeight;
                        if (currentWeight <= 0) {
                            currentWeight = maxWeight;
                            if (currentWeight == 0) {
                                GlobalCache.instance().put(Constant.CURRENT_SERVER_KEY, currentServer);
                                GlobalCache.instance().put(Constant.CURRENT_WEIGHT_KEY, currentWeight);
                                Thread.yield();
                                return null;
                            }
                        }
                    }

                    ServerAddress address = addressList.get(currentServer);
                    if (address.getWeight() >= currentWeight) {
                        GlobalCache.instance().put(Constant.CURRENT_SERVER_KEY, currentServer);
                        GlobalCache.instance().put(Constant.CURRENT_WEIGHT_KEY, currentWeight);
                        return new Server(address.getHost(), address.getPort());
                    }
                }
            }

        }
        return null;
    }
}
