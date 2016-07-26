package com.example;

import javax.persistence.Entity;

@Entity
public class MLoginUser implements java.io.Serializable {

    private String loginUserId;
    private String password;

    public MLoginUser() {
    }
    public String getLoginUserId(){
    	return this.loginUserId;
    }
    public String getPassword(){
    	return this.password;
    }
}