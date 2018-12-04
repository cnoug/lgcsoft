package com.lgcsoft.gateway.core.zuul.filter.pre;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.lgcsoft.gateway.common.util.TokenUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import io.jsonwebtoken.Claims;

@Component
public class MyHeaderFilter extends ZuulFilter {

	public static final String GATE_SUBJECT_USER = "jwts_user";// 用户登录信息
	public static final String GATE_ACCESSTOKEN = "AccessToken";//
	public static final String GATE_SECRETKEY = "jwts_key_181118";
	
	private static Logger log = LoggerFactory.getLogger(MyHeaderFilter.class);


	@Override
	public String filterType() {
		//这里是三个字符串：pre，route，post
		return "pre";
	}

	@Override
	public int filterOrder() {
		//过滤器优先级，同一级别的过滤，数字小的优先执行
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		// 下面这行是核心代码，所有的参数都是间接或直接通过RequestContext获取
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		String uri = request.getRequestURI().toString();
		String method = request.getMethod();

		log.info("url :{}  method: {} ", uri, method);
		
		// 这里可以对url进行判断，以确定是否进入过滤方法
		// 返回true会进入下面的run方法，返回false会跳过
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		// 1. clear userInfo from HTTP header to avoid fraud attack
		ctx.addZuulRequestHeader("user-info", "");
		
		// 2. verify the passed user token
		String accessToken = request.getHeader(GATE_ACCESSTOKEN);
		log.info("AccessToken: {}", accessToken);
		Claims claims = null;
		if (StringUtils.isNotBlank(accessToken)) {
			try {
				claims = TokenUtil.parseJWT(accessToken, GATE_SECRETKEY);
				if (claims == null) {
					this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED,
							"Error AccessToken for the API (" + request.getRequestURI().toString() + ")");
					return null;
				}
				log.info("claims is:{}", claims);
				if (claims.getSubject().equals(GATE_SUBJECT_USER)){
					String userInfo = (String) claims.get("userInfo");
					log.info("userInfo:{}", userInfo);
					// 3. set userInfo to HTTP header
					String encodeUserInfo = userInfo;
					try {
						encodeUserInfo = URLEncoder.encode(userInfo, "UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					ctx.addZuulRequestHeader("user-info", encodeUserInfo);
					log.info("ZuulRequestHeaders userInfo: {}", ctx.getZuulRequestHeaders().get("user-info"));

				}
			} catch (Exception e) {
				this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED,
						"Error AccessToken for the API (" + request.getRequestURI().toString() + ")");
				return null;
			}
		} else {
			this.stopZuulRoutingWithError(ctx, HttpStatus.UNAUTHORIZED,
					"AccessToken is needed for the API (" + request.getRequestURI().toString() + ")");
		}
		//这个方法的返回值目前没什么作用，我们直接返回null就可以
		return null;
	}

	private void stopZuulRoutingWithError(RequestContext ctx, HttpStatus status, String responseText) {

		ctx.removeRouteHost();
		ctx.setResponseStatusCode(status.value());
		ctx.setResponseBody(responseText);
		//zuul通过sendfalse来中断请求
		ctx.setSendZuulResponse(false);
		
	}
}
