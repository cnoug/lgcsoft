package com.lgcsoft.gateway.core.zuul.filter.pre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.google.common.base.Strings;
import com.lgcsoft.gateway.core.zuul.FilterOrder;
import com.netflix.zuul.context.RequestContext;

/**
 * Created by shenyuhang on 2018/06/29.
 **/
public class TokenAccessFilter extends AbstractPreZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAccessFilter.class);
    
    private static final String PARAM_TOKEN = "token";
    
    @Override
    public int filterOrder() {
        return FilterOrder.TOKEN_ACCESS_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object doRun() {
        HttpServletRequest request = context.getRequest();
        RequestContext ctx = context.getCurrentContext();
        //LOGGER.info("--->>> TokenFilter {},{}", request.getMethod(), request.getRequestURL().toString());
        
        
        
        
        InputStream ins = (InputStream) context.get("requestEntity");
        if (ins == null) {
            try {
				ins = context.getRequest().getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        String token = context.getRequest().getParameter(PARAM_TOKEN);

        LOGGER.info("accessToken:" + token);
        LOGGER.info("params:" + context.getRequestQueryParams());
        LOGGER.info("contentLength:" + context.getRequest().getContentLength());
        LOGGER.info("contentType:" + context.getRequest().getContentType());
        
        
        // 校验token  TODO:参数转换
        if (StringUtils.isNotBlank(token)) {
                // 校验成功
                String body = "";
				try {
					body = StreamUtils.copyToString(ins, Charset.forName("UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                LOGGER.info("body:" + body);
                body = StringUtils.replace(body, PARAM_TOKEN + "=" + token, PARAM_TOKEN + "=" + "authResult.getToken()");
                LOGGER.info("转换后的body：" + body);
        }
        
        
        
        
        if ("PUT".equalsIgnoreCase(request.getMethod())) {
            InputStream is;
            BufferedReader in;
            String param = "";
            try {
                is = request.getInputStream();
                if (is != null) {
                    in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead;
                    StringBuilder resultSb = new StringBuilder("");
                    while ((strRead = in.readLine()) != null) {
                        resultSb.append(strRead);
                    }
                    param = resultSb.toString();
                }

                String[] params = param.split("&");
                if (params.length > 0) {
                    String keyAndValue = Arrays.stream(params).filter(kv -> kv.contains("token")).findAny().orElse("");
                    if (Strings.isNullOrEmpty(keyAndValue)) {
                        LOGGER.info("token为空!");
                        return fail(401, "token must be specify.");
                    } else {
                        String[] tokenKeyAndValue = keyAndValue.split("=");
                        if (tokenKeyAndValue.length < 2 || !"token".equals(tokenKeyAndValue[0])) {
                            LOGGER.info("token为空!");
                            return fail(401, "token must be specify.");
                        } else {
                        	//灰度
                            //RibbonFilterContextHolder.clearCurrentContext();
                            return success();
                        }
                    }
                } else {
                    LOGGER.info("token为空!");
                    return fail(401, "token must be specify.");
                }
            } catch (IOException e) {
                LOGGER.info("获取请求参数时异常:{}", e);
                return fail(401, "exception for get parameter.");
            }
        } else {

            Map<String, String[]> paramArr = request.getParameterMap();
            if (!paramArr.containsKey("token")) {
                LOGGER.info("token为空!");
                return fail(401, "token must be specify.");
            }
        }
        //添加Basic Auth认证信息
        ctx.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials("app01", "*****"));
        return success();
    }
    /**
     * @param username
     * @param password
     * @return
     */
    private String getBase64Credentials(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        return new String(base64CredsBytes);
    }
}
