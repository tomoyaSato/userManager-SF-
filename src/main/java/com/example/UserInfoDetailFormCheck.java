package com.example;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.validate.HalfWidth;
import com.validate.order.First;
import com.validate.order.Second;
import com.validate.order.Third;

public class UserInfoDetailFormCheck {
	@NotBlank(groups = First.class)
	@Size(max = 50,groups = Second.class)
	@HalfWidth(groups = Third.class)
	protected String userId;

	@NotBlank(groups = First.class)
	@Size(min = 8 ,max = 60, groups = Second.class)
	@HalfWidth(groups = Third.class)
    protected String password;

	@NotBlank(groups = First.class)
	@Size(max = 32,groups = Second.class)
    protected String name;

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
}
