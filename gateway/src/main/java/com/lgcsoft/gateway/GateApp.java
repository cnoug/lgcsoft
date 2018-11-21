package com.lgcsoft.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcsoft.gateway.zuul.AccessFilter;

@SpringBootApplication
@Controller
@EnableZuulProxy
@RestController
public class GateApp {
	public static void main(String[] args) {
		new SpringApplicationBuilder(GateApp.class).web(true).run(args);
	}

	@RequestMapping(value = "/title")
	public String title() {
		return "Spring Boot 2.0 Test";
	}

	@RequestMapping(value = "/author")
	public String author() {
		return " ZuulTest";
	}

	/**
	 * 过滤器
	 * @return
	 */
	@RefreshScope
	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}
}
