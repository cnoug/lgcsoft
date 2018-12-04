package com.lgcsoft.gateway.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lgcsoft.gateway.dao.OAuthServiceDao;
import com.lgcsoft.gateway.entity.GateWayUser;

/**
 * @author liugc@neusoft.com
 *
 */
@Repository
public class OAuthServiceDaoImpl implements OAuthServiceDao {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean verifyOAuthKey(String plat_key, String secret_key) {
		// TODO Auto-generated method stub
		String sql = "select * from gateway_api_user where plat_key =? and secret_key =?";
		GateWayUser gwu = null;
		try {
			gwu = jdbcTemplate.queryForObject(sql, new Object[] { plat_key, secret_key }, new UserRowMapper());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		return gwu == null ? false : true;
	}

	@Override
	public Integer deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		String sql = "delete from gateway_api_user where id=?";
		Object[] params = new Object[] { id };
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public Integer updateUserById(GateWayUser user) {
		// TODO Auto-generated method stub
		String sql = "update gateway_api_user set name=? where id=?";
		Object[] params = new Object[] { user.getName(), user.getId() };
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public GateWayUser getUserById(Integer id) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select * from gateway_api_user where id=?", new Object[] { id },
				new UserRowMapper());
	}

	@Override
	public List<GateWayUser> getUserByDepartmentId(Integer id) {
		// TODO Auto-generated method stub
		String sql = "select * from gateway_api_user";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	@Override
	public Integer getUserCountByDepartmentId(Integer departmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer newUpdateUserById(GateWayUser user) {
		// TODO Auto-generated method stub
		String sql = "insert into gateway_api_user values(null,?,?)";
		Object[] params = new Object[] { user.getAccount() };
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public List<GateWayUser> findAllApiUser() {
		// TODO Auto-generated method stub
		String sql = "select * from gateway_api_user";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	class UserRowMapper implements RowMapper<GateWayUser> {
		@Override
		public GateWayUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			GateWayUser user = new GateWayUser();
			if(rs != null)
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setAccount(rs.getString("account"));
				user.setCreateTime(rs.getDate("create_time"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setIps(rs.getString("ips"));
				user.setJwtToken(rs.getString("jwt_Token"));
				user.setLastrefreshtime(rs.getDate("lastRefreshTime"));
				user.setPswd(rs.getString("pswd"));
				user.setPlatKey(rs.getString("plat_key"));
				user.setSecretKey(rs.getString("secret_key"));
			return user;
		}
	}
}
