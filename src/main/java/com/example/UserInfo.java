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

	@Size(max = 50)
	@Column(name="user_id" ,nullable = false)
	protected String userId;

	@Size(max = 60)
	@Column(name="password")
    protected String password;

	@Size(max = 32)
	@Column(name="name")
    protected String name;


    @Column(name="create_timestamp")
    protected Timestamp create_timestamp;


    @Column(name="update_timestamp")
    protected Timestamp update_timestamp;

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

	public Timestamp getCreate_timestamp() {
		return create_timestamp;
	}

	public void setCreate_timestamp(Timestamp create_timestamp) {
		this.create_timestamp = create_timestamp;
	}

	public Timestamp getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(Timestamp update_timestamp) {
		this.update_timestamp = update_timestamp;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public boolean getDeleteFlg() {
		return deleteFlg;
	}

	public void setDelete_flg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

    public UserInfo() {
    	super();
    }

      public UserInfo(String password,String name, Timestamp create_timestamp,Timestamp update_timestamp,boolean deleteFlg) {
        super();
        this.password = password;
        this.name = name;
        this.create_timestamp = create_timestamp;
        this.update_timestamp = update_timestamp;
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
