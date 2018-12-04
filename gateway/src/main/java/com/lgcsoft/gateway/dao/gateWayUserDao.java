package com.lgcsoft.gateway.dao;
import java.util.List;

import com.lgcsoft.gateway.entity.GateWayUser;

public interface gateWayUserDao {
    
    public Integer insertUser(GateWayUser user);
    
    public Integer deleteUserById(Integer id);
    
    public Integer updateUserById(GateWayUser user);
    
    public GateWayUser getUserById(Integer id);
    
    public List<GateWayUser> getUserByDepartmentId(Integer id);
    
    public Integer getUserCountByDepartmentId(Integer departmentId);
    
    public Integer newUpdateUserById(GateWayUser user);
}