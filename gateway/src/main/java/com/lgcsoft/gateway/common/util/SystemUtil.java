package com.lgcsoft.gateway.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public class SystemUtil {

    public static List<String> ipList(){
        Enumeration allNetInterfaces;
        InetAddress ip;
        List<String> ipList = new ArrayList<>();
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            ipList.add("127.0.0.1");
            return ipList;
        }
        while (allNetInterfaces.hasMoreElements()) {
            java.net.NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    if(!"127.0.0.1".equals(ip.getHostAddress())){
                        ipList.add(ip.getHostAddress());
                    }
                }
            }
        }
        return ipList;
    }
}
