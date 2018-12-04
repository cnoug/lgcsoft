package com.lgcsoft.gateway.web;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lgcsoft.gateway.common.util.JsonUtils;
import com.lgcsoft.gateway.common.util.RequestUtil;
import com.lgcsoft.gateway.entity.UserInfo;

@RestController
@RequestMapping("/ap")
public class ApiUserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	
	@RequestMapping(value = "/GetToken", method = RequestMethod.GET)
	public String getAuthToken() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		try {
			if (preHandle(request,response,"")) {
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		UserInfo userInfo = null;
		String userInfoJson = request.getHeader("user-info");

		if (StringUtils.isNotBlank(userInfoJson)) {

			try {
				userInfoJson = URLDecoder.decode(userInfoJson, "utf-8");
				userInfo = JsonUtils.json2Object(userInfoJson, UserInfo.class);
			} catch (Exception e) {
				logger.error("userInfo decode error!", e);
			}
			// 将userInfo存储在ThreadLocal中
			RequestUtil.setupUserInfo(userInfoJson, userInfo);
		}

		return true;
	}

}
