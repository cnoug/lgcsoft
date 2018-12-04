package com.lgcsoft.gateway.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lgcsoft.gateway.common.util.JsonUtils;
import com.lgcsoft.gateway.common.util.RequestUtil;
import com.lgcsoft.gateway.entity.UserInfo;

public class AuthenticationHeaderInterceptor implements HandlerInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(AuthenticationHeaderInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		UserInfo userInfo = null;
		String userInfoJson = request.getHeader("user-info");
    	
    	if (StringUtils.isNotBlank(userInfoJson)) {
    		
    		try {
    			userInfoJson=URLDecoder.decode(userInfoJson,"utf-8");
    			userInfo = JsonUtils.json2Object(userInfoJson, UserInfo.class);
			} catch (Exception e) {
				log.error("userInfo decode error!",e);
			}
    		//将userInfo存储在ThreadLocal中
    		RequestUtil.setupUserInfo(userInfoJson, userInfo);
    	}
    	
        return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
