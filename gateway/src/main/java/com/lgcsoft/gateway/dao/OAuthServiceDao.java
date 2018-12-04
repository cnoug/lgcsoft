package com.lgcsoft.gateway.dao;

import java.util.List;

import com.lgcsoft.gateway.entity.GateWayUser;

public interface OAuthServiceDao {
	boolean verifyOAuthKey(String plat_key, String secret_key);

	public Integer deleteUserById(Integer id);

	public Integer updateUserById(GateWayUser user);

	public GateWayUser getUserById(Integer id);

	public List<GateWayUser> getUserByDepartmentId(Integer id);

	public Integer getUserCountByDepartmentId(Integer departmentId);

	public Integer newUpdateUserById(GateWayUser user);
	
	public List<GateWayUser> findAllApiUser();
}
