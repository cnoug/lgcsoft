package com.sinosoft.gateway.core.zuul.filter.pre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Strings;
import com.sinosoft.gateway.core.zuul.FilterOrder;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public class UserRightFilter extends AbstractPreZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRightFilter.class);

    @Value("${login.enable}")
    private String login;

    @Override
    public int filterOrder() {
        return FilterOrder.USER_RIGHT_ORDER;
    }

    @Override
    public Object doRun() {

        if (!Strings.isNullOrEmpty(login) && Boolean.TRUE.toString().equals(login)) {
            LOGGER.info("没权限访问");
            return fail(401, "have no permission .");
        }

        return success();
    }
}
