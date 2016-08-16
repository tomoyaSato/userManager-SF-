package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
    private UserInfoService userInfoService;

	@Override
    public void configure(WebSecurity web) throws Exception {
        // セキュリティ設定を無視するリクエスト設定
        // 静的リソース(images、css、javascript)に対するアクセスはセキュリティ設定を無視する
        web.ignoring().antMatchers(
                            "/images/**",
                            "/css/**",
                            "/javascript/**",
                            "/webjars/**");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest().authenticated();

		// ログイン設定
		http.formLogin()
		.loginPage("/login")
//		.defaultSuccessUrl("/userList")
//		.failureHandler(new OriginalAuthenticationFailureHandle())
		.usernameParameter("userid")
		.passwordParameter("password")
		.permitAll();

		// ログアウト設定
		http.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//		.logoutUrl("/logout")
		.logoutSuccessUrl("/login")
		.invalidateHttpSession(true);
	}
	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth
// 			.inMemoryAuthentication()
// 			.withUser("user").password("password").roles("USER").and()
// 			.withUser("admin").password("password").roles("USER","ADMIN");
 			.authenticationProvider(createAuthProvider());
// 			.userDetailsService(userInfoService);
// 			.jdbcAuthentication()
// 				.dataSource(dataSouce);
 	}
	private AuthenticationProvider createAuthProvider(){
		DaoAuthenticationProvider authProvider =
				new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userInfoService);
		authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

		return authProvider;
	}

}


