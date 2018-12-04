package com.lgcsoft.gateway.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/t")
public class HelloController {

	@RequestMapping("/tt")
	public String index() {
		return "Hello Spring Boot,Index!";
	}

	@RequestMapping(value = "/boot/test", method = RequestMethod.GET)
	public String test() {
		return "GateWay Test Demo!";
	}
}
