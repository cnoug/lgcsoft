package com.sinosoft.gateway.service;


import java.util.List;

import com.sinosoft.gateway.entity.ServerAddress;

public interface ServerService {
    /**
     * get alive server
     * @return
     */
    List<ServerAddress> getAvailableServer();
}
