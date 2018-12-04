package com.lgcsoft.gateway.entity;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 內置用戶（認證白名單）
 * @author liugc@neusoft.com
 *
 */
public class GateWayUser extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 8886402739972726962L;
    
    private int id;
    private String name;
    private int departmentId;
    private String account;
    private String pswd;
    private java.sql.Date createTime;
    private String ips;
    private String flag;
    private String secretKey;
    private String platKey;
    private String jwtToken;
    private java.sql.Date lastrefreshtime;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    private Date createDate;

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", name=" + name + ", departmentId=" + departmentId + ", createDate=" + createDate + "]";
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public java.sql.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Date createTime) {
		this.createTime = createTime;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getPlatKey() {
		return platKey;
	}

	public void setPlatKey(String platKey) {
		this.platKey = platKey;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public java.sql.Date getLastrefreshtime() {
		return lastrefreshtime;
	}

	public void setLastrefreshtime(java.sql.Date lastrefreshtime) {
		this.lastrefreshtime = lastrefreshtime;
	}
	
	
	
}