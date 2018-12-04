package com.lgcsoft.gateway.common.util;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.lgcsoft.gateway.entity.UserInfo;


public class RequestUtil {
	
	private static Logger log = LoggerFactory.getLogger(RequestUtil.class);

	private static ThreadLocal<Object[]> userInfoKeeper = new ThreadLocal<Object[]>();
	
	public static void setupUserInfo(String userInfoJson, UserInfo userInfo) {
		
		userInfoKeeper.set(new Object[]{userInfoJson, userInfo});
	}
	
	public static UserInfo getUserInfo(HttpServletRequest request) {
		
		Object[] data = (Object[])userInfoKeeper.get();
		UserInfo userInfo = null;
		if (data != null) {
			userInfo = (UserInfo)data[1];
		}
		
    	return userInfo;
	}
	
	public static void setUserInfoHeader(HttpHeaders headers) {
		
		Object[] data = (Object[])userInfoKeeper.get();
		
		if (data != null) {
			String userInfoJson = (String)data[0];
			String encodedUserInfoJson = userInfoJson;
			
			try {
				encodedUserInfoJson = URLEncoder.encode(userInfoJson,"UTF-8");
			} catch (Exception e) {
				log.error("setUserInfoHeader",e);
			}
			headers.set("user-info", encodedUserInfoJson);
		}
	}
	
}
