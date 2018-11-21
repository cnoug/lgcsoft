package com.sinosoft.gateway.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinosoft.gateway.event.RefreshRouteService;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

	private final Logger logger = LoggerFactory.getLogger(DemoController.class);
	@Autowired
	RefreshRouteService refreshRouteService;

	@RequestMapping("/refreshRoute")
	public String refreshRoute() {
		refreshRouteService.refreshRoute();
		return "refreshRoute";
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


}
