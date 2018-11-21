package com.sinosoft.book;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

/**
 * war包发布 SpringBootServletInitializer
 * 构建spring应用（继承SpringBootServletInitializer并重写configure方法）
 * 
 * @author liugc@neusoft.com
 *
 */
@RestController
@SpringBootApplication
public class BookApplication extends SpringBootServletInitializer {
	private final Logger logger = LoggerFactory.getLogger(BookApplication.class);

	@RequestMapping(value = "/available")
	public String available() {
		System.out.println("Spring in Action");
		return "Spring in Action";
	}

	@RequestMapping(value = "/neusoft")
	public String neusoft() {
		System.out.println("neusoft in Action");
		return "this is neusoft in Action";
	}

	@RequestMapping(value = "/testJson")
	public JSONObject testJson() {
		System.out.println("testJson in Action");
		JSONObject jo = new JSONObject();
		jo.put("id", new Random(10).nextInt(100000) + " ");
		jo.put("name", "i am " + new Random(10).nextInt(100000) + " ");
		return jo;
	}

	@RequestMapping(value = "/testJsonParm/{name}")
	public JSONObject testJsonParm(@PathVariable String name) {
		System.out.println("testJsonParm in Action" + ":: " + name);
		JSONObject jo = new JSONObject();
		jo.put("id", new Random(10).nextInt(100000) + " ");
		jo.put("name", "i am " + name + " ");
		return jo;
	}

	class User {
		int id;
		String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@RequestMapping(value = "/testJsonPost", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject testJsonPost(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "pwd", required = true) String pwd) {

		System.out.println("testJsonParm in Action" + ":: " + name);
		JSONObject jo = new JSONObject();
		jo.put("name", name + " ");
		jo.put("pwd", "i am " + "" + " " + pwd);
		return jo;
	}

	@RequestMapping(value = "/checked-out")
	public String checkedOut() {
		return "Spring Boot in Action";
	}

	@RequestMapping(value = "/zuulTest")
	public String zuulTest() {
		return "zuulTest";
	}

	@RequestMapping(value = "/book")
	public String book() {
		return "book。。。。。。。";
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

	@RequestMapping(value = "/getbook/{id}", method = RequestMethod.GET)
	public String getbook5(@PathVariable("id") Integer id) throws InterruptedException {
		System.out.println(">>>>>>>>/getbook/" + id);
		int i = new Random().nextInt(20);
		TimeUnit.SECONDS.sleep(i);
		System.out.println("SLEEP=" + i + ">>>>>>>>/getbook/" + id);
		if (id == 1) {
			return new String(id + "人民文学出版社");
		} else if (id == 2) {
			return new String(id + "清华大学出版社");
		}
		return new String(id + "文学改良刍议");
	}

	@GetMapping(value = "/find/{id}")
	public String find(@PathVariable("id") String id, HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		System.out.println("服务端端口：" + request.getLocalPort() + "    接收到请求。。。。");
		// 实际项目中，这里可以使用JSONObject，返回json字符串
		// 为了便于测试消费者app的负载均衡，返回服务端端口
		// try {
		// Thread.sleep(5000);//休眠5秒钟，测试网关超时
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		long endTime = System.currentTimeMillis();
		String s = "张三" + "     服务端端口：" + request.getLocalPort() + "   耗时毫秒=" + (endTime - startTime);

		System.out.println("服务端端口：" + request.getLocalPort() + "    返回数据。。。。。耗时毫秒=" + (endTime - startTime));
		return s;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.web.support.SpringBootServletInitializer#configure(
	 * org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

		return builder.sources(BookApplication.class);

	}
}