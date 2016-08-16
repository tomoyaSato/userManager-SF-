package com.example;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserInfoDetailFormCheck {
	@NotNull
	@Size(max = 50)
	protected String userId;

	@NotNull
	@Size(max = 60)
    protected String password;

	@NotNull
	@Size(max = 32)
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
