package com.example;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {
    private static final long serialVersionUID = -220654011834826964L;
    private UserProfile userProfile;

    public LoginUser(UserProfile userProfile, String[] roles) {
        super(userProfile.getUserName(), userProfile.getPassword(),
                AuthorityUtils.createAuthorityList(roles));
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
