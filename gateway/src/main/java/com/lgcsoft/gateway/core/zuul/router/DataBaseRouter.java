package com.lgcsoft.gateway.core.zuul.router;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lgcsoft.gateway.core.zuul.entity.BasicRoute;

/**
 * Created by shenyuhang on 2018/07/02.
 **/
public class DataBaseRouter extends AbstractDynamicRouter {

	public final static Logger logger = LoggerFactory.getLogger(DataBaseRouter.class);
	public DataBaseRouter(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }
	private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    protected List<BasicRoute> readRoutes() {
    	Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        List<BasicRoute> results = jdbcTemplate.query("select * from gateway_api_define where enabled = true ",new BeanPropertyRowMapper<>(BasicRoute.class));
        for (BasicRoute result : results) {
            if(StringUtils.isBlank(result.getPath()) || StringUtils.isBlank(result.getUrl()) ){
                continue;
            }
            ZuulRoute zuulRoute = new ZuulRoute();
            try {
                org.springframework.beans.BeanUtils.copyProperties(result,zuulRoute);
            } catch (Exception e) {
                logger.error("=============load zuul route info from db with error==============",e);
            }
            routes.put(zuulRoute.getPath(),zuulRoute);
        }
        return results;
    }
}
