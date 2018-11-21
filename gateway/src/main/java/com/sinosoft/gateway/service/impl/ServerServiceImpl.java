package com.sinosoft.gateway.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sinosoft.gateway.entity.ServerAddress;
import com.sinosoft.gateway.service.ServerService;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
@Service
public class ServerServiceImpl implements ServerService {
    List<ServerAddress> addresses;

    @PostConstruct
    public void createData() {
        addresses = Lists.newArrayListWithExpectedSize(2);
        for (int i = 0; i < 2; i++) {
            ServerAddress address = new ServerAddress();
            address.setHost("127.0.0.1");
            address.setPort(9000 + i);
            address.setWeight(5 + i);
            address.setState(/*(i + 1) % 2*/1);
            addresses.add(address);
        }
    }

    @Override
    public List<ServerAddress> getAvailableServer() {
        return addresses.stream().filter(a -> 1 == a.getState()).collect(Collectors.toList());
    }
}
