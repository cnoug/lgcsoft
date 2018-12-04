package com.lgcsoft.gateway.web;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.lgcsoft.gateway.dao.gateWayUserDao;
import com.lgcsoft.gateway.entity.GateWayUser;
import com.lgcsoft.gateway.event.RefreshRouteService;

import net.sf.json.JSONObject;

/**
 * Created by liugc on 2018/11/1.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

	private final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	//@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	RefreshRouteService refreshRouteService;

	@RequestMapping("/refreshRoute")
	public JSONObject refreshRoute() {
		refreshRouteService.refreshRoute();
		JSONObject jo = new JSONObject();
		jo.put("refreshRoute", true);
		return jo;
	}

	@Autowired
	ZuulHandlerMapping zuulHandlerMapping;

	@RequestMapping("/watchNowRoute")
	public String watchNowRoute() {
		// 可以用debug模式看里面具体是什么
		Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
		return "watchNowRoute";
	}

	@RequestMapping("/hello")
	public String index(@RequestParam String name) {
		logger.info("request two name is " + name);
		try {
			Thread.sleep(1000000);
		} catch (Exception e) {
			logger.error(" hello two error", e);
		}
		return "hello " + name + "，this is two messge";
	}

	@RequestMapping("/hello2/hello")
	public String index2(@RequestParam String name) {
		logger.info("request two name is " + name);
		return "hello " + name + "，this is two messge";
	}

	@Autowired
	private gateWayUserDao userDao;

	@RequestMapping("/testInsertUser")
	public int testInsertUser() {
		GateWayUser user = new GateWayUser();
		user.setName("Bob");
		user.setDepartmentId(2);
		user.setCreateDate(new Date());

		int result = this.userDao.insertUser(user);
		System.out.println("testInsertUser::" + result);
		return result;
	}
	@RequestMapping(value= {"regist/sms"})
	public ResponseEntity<SendResponse> sms(String mobile){
		Random random = new Random();
		int next = random.nextInt(10000000);
		String code = ""+(10000000-next);
		ResponseEntity<SendResponse> response = doSend(mobile, code);
		return response;
	}
	
	public ResponseEntity<SendResponse> doSend(String  mobile,String code) {
		restTemplate = new RestTemplate();
		final String sendUrl = "http://message-service/message/sms/send";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("mobile", mobile);
		map.add("templateId", "CHECK_CODE");
		map.add("params['code']", code);
		logger.info("发送参数：{}",map);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<SendResponse> response = restTemplate.postForEntity(sendUrl, request, SendResponse.class);
		return response;
	}
	//@Data
	public static class SendResponse {
		/**
		 * 返回消息
		 */
		private String message;
		/**
		 * 返回状态码
		 */
		private String code;
	}
	
	
}
