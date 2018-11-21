package com.lgcsoft.gateway.core;

import java.util.HashMap;

/**
 * @author shenyuhang
 * @date 2018/07/06.
 **/
public class GlobalCache extends HashMap<String, Object> {

    private static class Holder {
        private final static GlobalCache CACHE = new GlobalCache();
    }

    public static GlobalCache instance() {
        return Holder.CACHE;
    }
}
