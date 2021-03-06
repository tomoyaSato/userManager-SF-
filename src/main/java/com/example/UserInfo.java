package com.example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="user_info")
public class UserInfo implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;

	@NotNull
	@Size(max = 50)
	@Column(name="user_id" ,nullable = false)
	protected String userId;

	@NotNull
	@Size(min = 8 ,max = 60)
	@Column(name="password")
    protected String password;

	@NotNull
	@Size(max = 32)
	@Column(name="name")
    protected String name;


    @Column(name="create_timestamp")
    protected Timestamp createTimestamp;


    @Column(name="update_timestamp")
    protected Timestamp updateTimestamp;

    @Size(max = 50)
    @Column(name="authority")
    protected String authority;

	@Column(name="delete_flg")
    protected boolean deleteFlg;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority){
		this.authority = authority;
	}

	public boolean getDeleteFlg() {
		return deleteFlg;
	}

	public void setDelete_flg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	public UserInfo(){
		super();
	}
    public UserInfo(UserInfo userInfo) {
    	super();
    	this.id 				= userInfo.getId();
    	this.userId				= userInfo.getUserId();
    	this.password			= userInfo.getPassword();
    	this.name				= userInfo.getName();
    	this.createTimestamp	= userInfo.getCreateTimestamp();
    	this.updateTimestamp	= userInfo.getUpdateTimestamp();
    	this.authority			= userInfo.getAuthority();
    	this.deleteFlg			= userInfo.getDeleteFlg();
    }

      public UserInfo(String password,String name, Timestamp create_timestamp,Timestamp update_timestamp,boolean deleteFlg) {
        super();
        this.password = password;
        this.name = name;
        this.createTimestamp = create_timestamp;
        this.updateTimestamp = update_timestamp;
        this.deleteFlg = deleteFlg;
      }

      // for debug
      public String toString() {
        return "[id:" + id + ", password:" + password + ", name:" + name + "]";
      }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority));
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
