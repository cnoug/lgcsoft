package com.lgcsoft.gateway.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.lgcsoft.gateway.dao.OAuthServiceDao;
import com.lgcsoft.gateway.entity.GateWayUser;
import com.lgcsoft.gateway.service.OAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
@PropertySource({ "classpath:conf.properties" })
public class OAuthServiceImpl implements OAuthService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired	
	OAuthServiceDao authDAO;	
    
    @Value("${jwt.secret}")
	private static String SECRET;
    
    @Autowired
	private Environment env;
    
	@Override
	public boolean verifyOAuthKey(String plat_key, String secret_key) {
		// TODO Auto-generated method stub
		return authDAO.verifyOAuthKey(plat_key, secret_key);
	}
	@Override
	public List<GateWayUser> findAllApiUser() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer updateUserById(GateWayUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GateWayUser getUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<GateWayUser> getUserByDepartmentId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer getUserCountByDepartmentId(Integer departmentId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer newUpdateUserById(GateWayUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean verifyOAuthToken(String token) {
		// TODO Auto-generated method stub
		try {
			SECRET = env.getProperty("jwt.secret");
			Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			logger.debug("Oauth DesToken is ::: " + claims.toString());
			String plat_key = claims.get("plat_key").toString();
			logger.info("verifyToken is plat_key :::" + plat_key);
		} catch (Exception exception) {
			logger.error("verifyToken err :: " + exception.getMessage());
			return false;
		}
		return true;
	}

}
