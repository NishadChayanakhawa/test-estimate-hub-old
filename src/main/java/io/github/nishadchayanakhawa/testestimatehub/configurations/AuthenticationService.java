package io.github.nishadchayanakhawa.testestimatehub.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.User;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.UserService;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=this.userService.loadUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException(username);
		} else {
			return user;
		}
	}
}
