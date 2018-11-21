package com.lgcsoft.gateway.service;


import java.util.List;

import com.lgcsoft.gateway.entity.ServerAddress;

public interface ServerService {
    /**
     * get alive server
     * @return
     */
    List<ServerAddress> getAvailableServer();
}
