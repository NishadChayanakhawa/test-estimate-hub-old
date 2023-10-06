package io.github.nishadchayanakhawa.testestimatehub.configurations;

//import section
//spring libraries
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import io.github.nishadchayanakhawa.testestimatehub.model.configurations.Role;

//spring-security libraries
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * <b>Class Name</b>: SecurityFilterConfiguration<br>
 * <b>Description</b>: Spring security filter configuration.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Service
public class SecurityFilterConfiguration {
	private static final String H2_CONSOLE_CONTEXT_MATCHER = "/h2-console/**";

	@Bean
	@Order(2)
	@Profile("!dev")
	SecurityFilterChain applicationSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(
				request -> request.requestMatchers(AntPathRequestMatcher.antMatcher("/api/configuration/**"))
						.hasRole(Role.ADMIN.toString()).anyRequest().permitAll())
				.build();
	}

	/**
	 * <b>Method Name</b>: h2ConsoleSecurityFilterChain<br>
	 * <b>Description</b>: Disables csrf and header frame validation and
	 * authentication for h2-console. Access to console will be authenticated by h2
	 * db setup.<br>
	 * 
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(1)
	SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		return
		// for h2-console,
		http.securityMatcher(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)).authorizeHttpRequests(auth ->
		// disable spring security
		auth.requestMatchers(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)).permitAll())
				// disable CSRF
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(H2_CONSOLE_CONTEXT_MATCHER)))
				// disable header frame requirement
				.headers(headers -> headers.frameOptions().disable()).build();
	}
}
