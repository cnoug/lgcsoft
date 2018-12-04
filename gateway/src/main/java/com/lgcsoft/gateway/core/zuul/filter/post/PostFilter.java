package com.lgcsoft.gateway.core.zuul.filter.post;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgcsoft.gateway.core.zuul.FilterType;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
 
/**
 * @author liugc@neusoft.com
 *
 */
public class PostFilter extends ZuulFilter {
 
    private static Logger log = LoggerFactory.getLogger(PostFilter.class);
 
    @Override
    public String filterType() {
        //后置过滤器
        return FilterType.post.name();
    }
 
    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return -2;
    }
 
    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        return false;
    }
 
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("进入post过滤器");
        if(StringUtils.isEmpty(ctx.getResponseBody())) {
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            // 为使得中文字符不乱码
            ctx.getResponse().setCharacterEncoding("UTF-8");
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            ctx.setResponseBody("后端服务异常");
        }else {
        	log.info("返回结果:::" + ctx.getResponseBody());
        }
        //int i = 1 / 0;
        return null;
 
    }
 
}
