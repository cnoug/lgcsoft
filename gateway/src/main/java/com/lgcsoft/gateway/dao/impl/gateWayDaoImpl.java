package com.lgcsoft.gateway.dao.impl;

import com.lgcsoft.gateway.dao.gateWayUserDao;
import com.lgcsoft.gateway.entity.GateWayUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class gateWayDaoImpl implements gateWayUserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * JdbcTemplate提供的update方法，可以用于新增、修改、删除、执行存储过程等 如果是数据库插入，对于MySQL、SQL
	 * Server等数据库，含有自增序列时，则需要提供一个KeyHolder来存放返回的序列
	 */
	public Integer insertUser(GateWayUser user) {
		String sql = "insert into user(name, department_id, create_time) values(?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				// 指出自增主键的列名
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, user.getName());
				ps.setInt(2, user.getDepartmentId());
				ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public Integer deleteUserById(Integer id) {
		String sql = "delete from user where id = ?";
		return this.jdbcTemplate.update(sql, id);
	}

	public Integer updateUserById(GateWayUser user) {
		String sql = "update user set name = ?, department_id=? where id = ?";
		return jdbcTemplate.update(sql, user.getName(), user.getDepartmentId(), user.getId());
	}

	/**
	 * JdbcTemplate需要一个RowMapper，将查询结果集ResultSet映射成一个对象
	 */
	public GateWayUser getUserById(Integer id) {
		String sql = "select * from user where id = ?";
		return jdbcTemplate.queryForObject(sql, new RowMapper<GateWayUser>() {
			@Override
			public GateWayUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				GateWayUser user = new GateWayUser();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setDepartmentId(rs.getInt("department_id"));
				user.setCreateDate(rs.getDate("create_time"));
				return user;
			}

		}, id);
	}

	/**
	 * 也可以创建一个内部类UserRowMapper作为查询复用
	 * 
	 * @author Administrator
	 *
	 */
	static class UserRowMapper implements RowMapper<GateWayUser> {
		public GateWayUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			GateWayUser user = new GateWayUser();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setDepartmentId(rs.getInt("department_id"));
			user.setCreateDate(rs.getDate("create_time"));
			return user;
		}
	}

	/**
	 * 如果返回的结果为列表，则需要使用query方法
	 */
	public List<GateWayUser> getUserByDepartmentId(Integer departmentId) {
		String sql = "select * from user where department_id = ?";
		List<GateWayUser> userList = jdbcTemplate.query(sql, new UserRowMapper(), departmentId);
		return userList;
	}

	/**
	 * MapSqlParameterSource是一个类似Map风格的类，包括 Key-Value，Key就是SQL中的参数
	 */
	public Integer getUserCountByDepartmentId(Integer departmentId) {
		String sql = "select count(1) from user where department_id = :deptId";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("deptId", departmentId);
		Integer count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
		return count;
	}

	/**
	 * 使用SqlParameterSource来封装任意的JavaBean
	 */
	public Integer newUpdateUserById(GateWayUser user) {
		String sql = "update user set name = :name, department_id = :departmentId where id = :id";
		SqlParameterSource source = new BeanPropertySqlParameterSource(user);
		return namedParameterJdbcTemplate.update(sql, source);
	}

}