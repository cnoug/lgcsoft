package com.lgcsoft.gateway.common.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
	private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
	private static Long timeLimit = 1000 * 60 * 60 * 24l;// 1天
	public static final String PRIVATEKEY = "privateKey";
	public static final String ACCESSTOKEN = "AccessToken";// 公私钥

	// 生成token
	public static String createToken(String subject, Map<String, Object> map, String secretKey){
		try {
			byte[] bytes = Base64.encodeBase64(secretKey.getBytes("utf-8"));
			String userToken = createToken(subject, map, bytes);
			return userToken;
		} catch (Exception e) {
			logger.error("createToken error",e);
		}
		return null;
	}

	private static String createToken(String subject, Map<String, Object> map, byte[] secretKey) {
		String userToken = null;
		JwtBuilder builder = Jwts.builder().setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + timeLimit));
		if (map != null) {
			for (String key : map.keySet()) {
				builder.claim(key, map.get(key));
			}
		}
		userToken = builder.signWith(SignatureAlgorithm.HS512, secretKey).compact();

		return userToken;
	}

	/**
	 * 解密 jwt
	 * 
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJWT(String jwt, String secretKey) throws Exception {
		byte[] bytes = Base64.encodeBase64(secretKey.getBytes("utf-8"));
		Claims claims = Jwts.parser().setSigningKey(bytes).parseClaimsJws(jwt).getBody();
		return claims;
	}

}
