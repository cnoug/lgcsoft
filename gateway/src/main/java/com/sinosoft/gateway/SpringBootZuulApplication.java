package com.sinosoft.gateway;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sinosoft.gateway.core.zuul.router.PropertiesRouter;

import net.sf.json.JSONObject;

/**
 * Spring -- zuul -- api---gataWay 
 * @author  liugc@neusoft.com
 * @date    2018-11-08
 */
@RestController
@EnableAutoConfiguration
@EnableZuulProxy
@ComponentScan(basePackages = {
        "com.sinosoft.gateway.core",
        "com.sinosoft.gateway.service"
})
public class SpringBootZuulApplication implements CommandLineRunner {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    RouteLocator routeLocator;
    @Autowired
	ZuulHandlerMapping zuulHandlerMapping;

    private ScheduledExecutorService executor;
    private Long lastModified = 0L;
    private boolean instance = true;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootZuulApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("properties read.").build()
        );
        logger.info("###############this is refresh routes methods !!!!!" + new Date());
        executor.scheduleWithFixedDelay(() -> publish(), 0, 1, TimeUnit.SECONDS);
    }

    public void publish() {
        if (isPropertiesModified()) {
            publisher.publishEvent(new RoutesRefreshedEvent(routeLocator));
        }
    }

    public boolean isPropertiesModified() {
        File file = new File(this.getClass().getClassLoader().getResource(PropertiesRouter.PROPERTIES_FILE).getPath());
        if (instance) {
            instance = false;
            return false;
        }
        if (file.lastModified() > lastModified) {
            lastModified = file.lastModified();
            return true;
        }
        return false;
    }
    @RequestMapping("/refreshRoute")
	public String refreshRoute() {
    	publish();
    	logger.info("this is force to refreshRoute infomation !!");
		return "refreshRoute";
	}
	@RequestMapping("/watchNowRoute")
	public String watchNowRoute() {
		// 可以用debug模式看里面具体是什么
		Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
		return "watchNowRoute";
	}
	@RequestMapping("/gateway")
	public JSONObject gateway() {
		JSONObject json = new JSONObject();
		json.put("success", "welcome to gateway!");
		return json;
	}
}
