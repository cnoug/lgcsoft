package com.sinosoft.gateway.service;


import com.sinosoft.gateway.entity.GatewayAddress;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public interface GatewayService {

    /**
     * 获取网关信息
     * @param host
     * @param port
     * @return
     */
    GatewayAddress getByHostAndPort(String host, Integer port);
}
