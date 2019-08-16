package com.rhy.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rhy.blog.config.PermissionService;


@Configuration
/* 开启Spring Security的功能 */

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
    UserDetailsService permissionService(){
        return new PermissionService();
    }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(permissionService())
            //启动BCrypt加密
            .passwordEncoder(passwordEncoder());
            	
            	
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()// 对请求做授权
		
				.antMatchers("/","/index","/aboutme","/categories")// 不需要任何认证就可以访问
				.permitAll()
				.antMatchers("/editor").hasAnyRole("USER")// 需要身份认证
				
				.antMatchers("/superadmin").hasAnyRole("SUPERADMIN")
				
				.and()
				.formLogin().loginPage("/login") // 当需要用户登录时候，转到的登录页面。
				.loginProcessingUrl("/login") // 自定义的登录接口
				.failureUrl("/login?error").defaultSuccessUrl("/")
				.and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		
		http.csrf().disable();
	}

}