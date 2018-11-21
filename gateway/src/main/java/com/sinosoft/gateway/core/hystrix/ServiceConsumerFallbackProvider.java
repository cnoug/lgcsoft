package com.sinosoft.gateway.core.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
 
/**
 * Created by liugc@neusoft.com
 */
@Component
public class ServiceConsumerFallbackProvider implements ZuulFallbackProvider {
 
	private final Logger logger = LoggerFactory.getLogger(ServiceConsumerFallbackProvider.class);
	//指定要处理的 service。
    @Override
    public String getRoute() {
    	// api服务id，如果需要所有调用都支持回退，则return "*"或return null
        return "*";
        //return "service-consumer";
    }
 
    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
        	
        	String responseStr = "{\"code\":10006,\"msg\":\"服务路由异常\"}";
            @Override
            public HttpStatus getStatusCode() throws IOException {
            	/**
                 * 网关向api服务请求是失败了，但是消费者客户端向网关发起的请求是OK的， 不应该把api的404,500等问题抛给客户端
                 * 网关和api服务集群对于客户端来说是黑盒子
                 */
                return HttpStatus.OK;
            }
 
            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }
 
            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }
 
            @Override
            public void close() {
 
            }
 
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(responseStr.getBytes());
            }
 
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                // 和body中的内容编码一致，否则容易乱码
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                return headers;
            }
        };
    }
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            logger.info("Excption {}",reason);
        }
        return fallbackResponse();
    }
}