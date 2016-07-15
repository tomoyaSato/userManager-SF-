package com.example;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * UserDetailsServiceの実装クラス
 * Spring Securityでのユーザー認証に使用する
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    /* (非 Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String login_id)
            throws UsernameNotFoundException {

        // 認証を行うユーザー情報を格納する
        MLoginUser user = null;
        try {
            // 入力したユーザーIDから認証を行うユーザー情報を取得する
            // 処理内容は省略
        } catch (Exception e) {
            // 取得時にExceptionが発生した場合
            throw new UsernameNotFoundException("It can not be acquired User");
        }

        // ユーザー情報を取得できなかった場合
        if(user == null){
            throw new UsernameNotFoundException("User not found for login id: " + login_id);
        }

        // ユーザー情報が取得できたらSpring Securityで認証できる形で戻す
        return new LoginUser(user);
    }

}