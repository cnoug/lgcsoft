package com.lgcsoft.gateway.web;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lgcsoft.gateway.service.OAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.json.JSONObject;

@RestController
@PropertySource({ "classpath:conf.properties" })
public class OAuthController {

	private final static Logger logger = LoggerFactory.getLogger(OAuthController.class);

	public static final String TOKEN_HEADER = "Authorization";

	public static final String TOKEN_PREFIX = "Bearer ";

	// token 密钥
	@Value("${jwt.secret}")
	private static String SECRET;

	@Autowired
	private Environment env;

	private static final String ISS = "echisan";

	// 过期时间是3600秒，既是1个小时
	private static final long EXPIRATION = 3600L;

	// 选择了记住我之后的过期时间为7天
	private static final long EXPIRATION_REMEMBER = 604800L;

	@Autowired
	private OAuthService authService;

	/**
	 * @author liugc
	 * 
	 *         第一部分{"alg":"HS512"}是签名算法 第二部分
	 *         {"exp":1495176357,"username":"admin"}是一些数据, 这里有过期日期和用户名
	 *         第三部分'*******'(l非常重要,是签名Signiture, 服务器会验证这个以防伪造
	 * @RequestBody Map<String, Object> cms
	 * @date 2016年10月18日 下午2:51:38
	 */
	@RequestMapping(value = "/api/OAuth/createToken", method = RequestMethod.POST)
	public JSONObject CreateToken(@RequestParam(value = "plat_key", required = true) String plat_key,
			@RequestParam(value = "secret_key", required = true) String secret_key,
			@RequestParam(value = "isRememberMe", required = false, defaultValue = "true") boolean isRememberMe) {
		logger.info("" + env.getProperty("jwt.secret"));
		SECRET = env.getProperty("jwt.secret");
		long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("plat_key", plat_key);
		claims.put("secret_key", secret_key);

		boolean verfy = authService.verifyOAuthKey(plat_key, secret_key);
		if (!verfy) {
			claims.put("result", false);
			claims.put("message", "无效授权，请核查授权key是否正确!");
			return (JSONObject.fromObject(claims));
		}
		// 头部信息
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");

		String token = Jwts.builder().setClaims(claims).setHeader(headerMap)
				.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
				// 采用什么算法是可以自己选择的，不一定非要采用HS512
				.signWith(SignatureAlgorithm.HS512, SECRET).setSubject(claims.get("plat_key").toString()).setIssuer(ISS)
				.compact();
		logger.debug("Oauth Token is ::: " + token);
		claims.clear();
		claims.put("token", token);
		claims.put("result", true);
		claims.put("message", "success");
		return (JSONObject.fromObject(claims));
	}

	// 从token中获取用户名
	public String getUsername(String token) {
		return getTokenBody(token).getSubject();
	}

	// 是否已过期
	public boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
	}

	/**
	 * 返回一定时间后的日期
	 * 
	 * @param date
	 *            开始计时的时间
	 * @param year
	 *            增加的年
	 * @param month
	 *            增加的月
	 * @param day
	 *            增加的日
	 * @param hour
	 *            增加的小时
	 * @param minute
	 *            增加的分钟
	 * @param second
	 *            增加的秒
	 * @return
	 */
	public Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
		if (date == null) {
			date = new Date();
		}

		Calendar cal = new GregorianCalendar();

		cal.setTime(date);
		if (year != 0) {
			cal.add(Calendar.YEAR, year);
		}
		if (month != 0) {
			cal.add(Calendar.MONTH, month);
		}
		if (day != 0) {
			cal.add(Calendar.DATE, day);
		}
		if (hour != 0) {
			cal.add(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute != 0) {
			cal.add(Calendar.MINUTE, minute);
		}
		if (second != 0) {
			cal.add(Calendar.SECOND, second);
		}
		return cal.getTime();
	}

	@RequestMapping(value = "/api/OAuth/verifyToken", method = RequestMethod.POST)
	@ResponseBody
	public boolean verifyToken(@RequestParam(value = "token", required = true) String token) {
		return authService.verifyOAuthToken(token);
	}

	@RequestMapping(value = "/api/OAuth/getTokenBody", method = RequestMethod.POST)
	public Claims getTokenBody(@RequestParam(value = "token", required = true) String token) {
		// SECRET = env.getProperty("jwt.secret");
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		authService.verifyOAuthToken(token);
		return claims;
	}
}
