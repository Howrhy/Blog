package com.rhy.blog.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.rhy.blog.entity.user.RoleInfo;
import com.rhy.blog.entity.user.UserInfo;
import com.rhy.blog.service.impl.UserService;

@Component
public class PermissionService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserInfo userInfo = userService.findUser(username);
		if (userInfo == null) {
			throw new UsernameNotFoundException("用户不存在！");
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = new java.util.Date();
		String strdate = dateFormat.format(date1);

		userInfo.setRecentlyLanded(strdate);
		userService.updateRecentlyLanded(userInfo.getUserName(), strdate);

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		userService.findRole(userInfo);
		
		for (RoleInfo roleInfo:userInfo.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(roleInfo.getRoleName()));
		}
		return new org.springframework.security.core.userdetails.User(userInfo.getUserName(), userInfo.getUserPwd(),
				authorities);
	}

}
