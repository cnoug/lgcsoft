package com.lgcsoft.gateway.core.zuul.filter.pre;

import com.lgcsoft.gateway.core.zuul.FilterType;
import com.lgcsoft.gateway.core.zuul.filter.AbstractZuulFilter;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public abstract class AbstractPreZuulFilter extends AbstractZuulFilter {
	
	//定义filter的类型，有pre、route、post、error四种
    @Override
    public String filterType() {
        return FilterType.pre.name();
    }
}
