package com.lgcsoft.gateway.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lgcsoft.gateway.core.ribbon.ServerLoadBalancerRule;
import com.lgcsoft.gateway.core.zuul.filter.post.PostFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.LoginFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.RateLimiterFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.TokenAccessFilter;
import com.lgcsoft.gateway.core.zuul.filter.pre.UserRightFilter;
import com.lgcsoft.gateway.core.zuul.router.DataBaseRouter;
import com.netflix.loadbalancer.IRule;
import com.netflix.zuul.ZuulFilter;

/**
 * @author
 * @date 2018/07/05.
 **/
@Configuration
public class ZuulConfigure {

	@Autowired
	ZuulProperties zuulProperties;
	@Autowired
	ServerProperties server;
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 动态路由 from properties
	 * 
	 * @return
	 */
	/*
	 * @Bean public PropertiesRouter propertiesRouter() { return new
	 * PropertiesRouter(this.server.getServletPrefix(), this.zuulProperties); }
	 */
	@Bean
	public DataBaseRouter dataBaseRouter() {
		DataBaseRouter routeLocator = new DataBaseRouter(this.server.getServletPrefix(), this.zuulProperties);
		routeLocator.setJdbcTemplate(jdbcTemplate);
		return routeLocator;
	}

	/**
	 * 全局配置-实现前端跨域 允许提交请求的方法，*表示全部允许
	 * 
	 * attention:简单跨域就是GET，HEAD和POST请求，但是POST请求的"Content-Type"只能是application/x-www-form-urlencoded,
	 * multipart/form-data 或 text/plain
	 * 反之，就是非简单跨域，此跨域有一个预检机制，说直白点，就是会发两次请求，一次OPTIONS请求，一次真正的请求
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("OPTIONS").allowedMethods("HEAD").allowedMethods("GET")
						.allowedMethods("PUT").allowedMethods("POST").allowedMethods("DELETE").allowedMethods("PATCH")
						// 允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
						.allowedOrigins("*")
						// 允许访问的头信息,*表示全部
						.allowedHeaders("*")
						// 允许cookies跨域
						.allowCredentials(true)
						// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
						.maxAge(18000L);
			}
		};
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addExposedHeader("Content-Type");
		config.addExposedHeader("Authorization");
		config.addExposedHeader("Accept");
		config.addExposedHeader("Origin");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	/**
	 * 动态负载
	 * 
	 * @return
	 */
	@Bean
	public IRule loadBalance() {
		return new ServerLoadBalancerRule();
	}

	@Bean
	public ZuulFilter userFilter() {
		return new UserRightFilter();
	}

	@Bean
	public ZuulFilter rateLimiterFilter() {
		return new RateLimiterFilter();
	}

	@Bean
	public ZuulFilter tokenAccessFilter() {
		return new TokenAccessFilter();
	}

	/**
	 * 返回拦截
	 * 
	 * @return
	 */
	@Bean
	public ZuulFilter PostFilter() {
		return new PostFilter();
	}

	@Bean
	public LoginFilter loginFilter() {
		return new LoginFilter();
	}
}
