package com.lgcsoft.gateway.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class JwtUser {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String id;
    private final String username;
    private final String password;
    private final String email;
    //private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetDate;
 
    
    public JwtUser(
            String id,
            String username,
            String password,
            String email,
            List<String> authorities,
            Date lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        //this.authorities = mapToGrantedAuthorities(authorities);
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
    
//    private  List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
//        return authorities.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
    
    //返回分配给用户的角色列表
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
    
    @JsonIgnore
    public String getId() {
        return id;
    }
 
    @JsonIgnore
    public String getPassword() {
        return password;
    }
 
    public String getUsername() {
        return username;
    }
    // 账户是否未过期
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    // 账户是否未锁定
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    // 密码是否未过期
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 账户是否激活
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
    // 这个是自定义的，返回上次密码重置日期
    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
	public String getEmail() {
		return email;
	}
}
