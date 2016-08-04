package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService{
	@Autowired
    UserInfoRepository userInfoRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(username == null || username.equals("")){
			throw new UsernameNotFoundException("Username is empty");
		}

		UserInfo userInfo = userInfoRepository.findByUserId(username);
		if(userInfo == null){
			throw new UsernameNotFoundException("User not found for name:"+username);
		}
		return userInfo;
	}



}
