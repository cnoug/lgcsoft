package com.lgcsoft.gateway.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lgcsoft.gateway.entity.GatewayAddress;
import com.lgcsoft.gateway.service.GatewayService;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
@Service
public class GatewayServiceImpl implements GatewayService {

    private List<GatewayAddress> gateways;

    @PostConstruct
    public void createData() {
        gateways = Lists.newArrayListWithExpectedSize(3);
        for (int i = 0; i < 3; i++) {
            GatewayAddress address = new GatewayAddress();
            address.setId(String.valueOf(i));
            address.setHost("127.0.0.1");
            address.setPort(8090 + i);
            address.setName("测试网关组");
            address.setState(1);
            address.setFkStrategyId(String.valueOf(i % 4));
            gateways.add(address);
        }
    }

    @Override
    public GatewayAddress getByHostAndPort(String host, Integer port) {
        return gateways.stream().filter(a -> host.equals(a.getHost()) && port.compareTo(a.getPort()) == 0).findFirst().orElse(null);
    }
}
