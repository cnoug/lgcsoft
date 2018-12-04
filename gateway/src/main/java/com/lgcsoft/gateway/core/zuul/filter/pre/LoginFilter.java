package com.lgcsoft.gateway.core.zuul.filter.pre;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.netflix.zuul.context.RequestContext;


/**
 * Created by Skye on 2018/1/11.
 */
public class LoginFilter extends AbstractPreZuulFilter {

    /**
     * 指定该Filter的类型
     * ERROR_TYPE = "error";
     * POST_TYPE = "post";
     * PRE_TYPE = "pre";
     * ROUTE_TYPE = "route";
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 指定该Filter执行的顺序（Filter从小到大执行）
     * DEBUG_FILTER_ORDER = 1;
     * FORM_BODY_WRAPPER_FILTER_ORDER = -1;
     * PRE_DECORATION_FILTER_ORDER = 5;
     * RIBBON_ROUTING_FILTER_ORDER = 10;
     * SEND_ERROR_FILTER_ORDER = 0;
     * SEND_FORWARD_FILTER_ORDER = 500;
     * SEND_RESPONSE_FILTER_ORDER = 1000;
     * SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
     * SERVLET_30_WRAPPER_FILTER_ORDER = -2;
     * SERVLET_DETECTION_FILTER_ORDER = -3;
     */
    @Override
    public int filterOrder() {
        return 5 - 1;
    }

    /**
     * 指定需要执行该Filter的规则
     * 返回true则执行run()
     * 返回false则不执行run()
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestUrl = request.getRequestURL().toString();

        // 请求URL内不包含login或join则需要经过该过滤器，即执行run()
        //return !requestUrl.contains("login") && !requestUrl.contains("join");
        return false;
    }

    /**
     * 该Filter具体的执行活动
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpSession httpSession = request.getSession();

        // 若session中不包含userId，则这次请求视为未登录请求，不给予路由，而提示“请登录”
        if (httpSession.getAttribute("userId") == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            // 为使得中文字符不乱码
            ctx.getResponse().setCharacterEncoding("UTF-8");
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            ctx.setResponseBody("请登录");
            System.out.println("-----" + request.getRequestURL().toString());
        }
        return null;
    }

	@Override
	public Object doRun() {
		// TODO Auto-generated method stub
		return run();
	}
}
